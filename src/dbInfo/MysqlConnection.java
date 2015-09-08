package dbInfo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import config.Constants;
import newhybrid.NoHConnectionException;
import thrift.ColumnInfo;
import thrift.DType;
import thrift.DbmsInfo;

public class MysqlConnection extends HConnection {
	static private final int MAX_RETRY = 5;

	/**
	 * get a mysql HConnection by dbms information
	 * 
	 * @param dbmsInfo
	 * @return mysql HConnection,not null
	 * @throws NoHConnectionException
	 */
	static public HConnection getConnection(DbmsInfo dbmsInfo) throws NoHConnectionException {
		int cnt = 0;
		Connection newConnection = null;
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			while (cnt++ < MAX_RETRY && newConnection == null) {
				newConnection = DriverManager.getConnection(dbmsInfo.mCompleteConnectionString, dbmsInfo.mMysqlUsername,
						dbmsInfo.mMysqlPassword);
				if (newConnection.isValid(0))
					break;
			}
			if (newConnection == null || !newConnection.isValid(0)) {
				throw new NoHConnectionException(dbmsInfo,
						"tried " + MAX_RETRY + " times but can not establish connection!");
			}
			HConnection res = new MysqlConnection(dbmsInfo, newConnection);
			return res;
		} catch (SQLTimeoutException e) {
			throw new NoHConnectionException(dbmsInfo, "Time out!");
		} catch (SQLException e) {
			throw new NoHConnectionException(dbmsInfo, "Access error or url error!");
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			throw new NoHConnectionException(dbmsInfo, "No jdbc driver!");
		}
	}

	private Connection mMysqlConnection;

	public MysqlConnection(DbmsInfo dbmsInfo, Connection mysqlConnection) {
		super(dbmsInfo);
		mMysqlConnection = mysqlConnection;
	}

	@Override
	public boolean isUseful() {
		if (mMysqlConnection == null)
			return false;
		try {
			return mMysqlConnection.isValid(0);
		} catch (SQLException e) {
			return false;
		}
	}

	@Override
	public void release() throws HSQLException {
		if (mMysqlConnection == null)
			return;
		try {
			mMysqlConnection.close();
		} catch (SQLException e) {
			throw new HSQLException("database access error:" + e.getMessage());
		} finally {
			mMysqlConnection = null;
		}
	}

	@Override
	public void dropAll() throws HSQLException {
		try {
			ResultSet tables = mMysqlConnection.getMetaData().getTables(null, null, null, null);
			while (tables.next()) {
				String tableName = tables.getString("TABLE_NAME");
				HResult result = _sql("drop table if exists " + tableName);
				if (!result.isSuccess()) {
					throw new HSQLException("failed to drop table " + tableName + ":" + result.getMessage());
				}
			}
		} catch (SQLException e) {
			throw new HSQLException("database access error or called on a closed connection:" + e.getMessage());
		}
	}

	private HResult _sql(String sqlString) {
		//System.out.println(sqlString);
		Statement stmt;
		try {
			stmt = mMysqlConnection.createStatement();
			if (stmt.execute(sqlString)) {
				return new MysqlResult(QueryType.READ, true, "success", stmt.getResultSet());
			} else {
				return new MysqlResult(QueryType.WRITE, true, "success", stmt.getUpdateCount());
			}
		} catch (SQLException e) {
			return new MysqlResult(QueryType.FAILED, false,
					"database access error or statement closed error or others:" + e.getMessage(), -1);
		}
	}

	@Override
	public void dropTable(Table table) throws HSQLException {
		String realTableName = getRealTableName(table);
		HResult result = _sql("drop table if exists " + realTableName);
		if (!result.isSuccess())
			throw new HSQLException("failed to drop table " + realTableName + ":" + result.getMessage());
	}

	@Override
	public boolean createTable(Table table) throws HSQLException {
		if (tableExist(table))
			return false;
		String realTableName = getRealTableName(table);
		StringBuffer createString = new StringBuffer("create table ");
		ArrayList<String> definitions;
		createString.append(realTableName);
		createString.append("(");
		definitions = table.getColumnDefinition();
		for (int i = 0; i < definitions.size(); i++) {
			if (i > 0)
				createString.append(",");
			createString.append(definitions.get(i));
		}
		definitions = table.getConstraintDefinition();
		for (int i = 0; i < definitions.size(); i++) {
			createString.append(",");
			createString.append(definitions.get(i));
		}
		createString.append(")");
		HResult result = _sql(createString.toString());
		if (!result.isSuccess())
			throw new HSQLException(result.getMessage());
		return true;
	}

	@Override
	public ArrayList<String> getAllTableNames() throws HSQLException {
		ArrayList<String> res = new ArrayList<>();
		try {
			ResultSet tables = mMysqlConnection.getMetaData().getTables(null, null, null, null);
			while (tables.next()) {
				String tableName = tables.getString("TABLE_NAME");
				res.add(tableName);
			}
			tables.close();
			return res;
		} catch (SQLException e) {
			throw new HSQLException(e.getMessage());
		}
	}

	@Override
	public boolean tableExist(Table table) throws HSQLException {
		String realTableName = getRealTableName(table);
		ArrayList<String> allNames = getAllTableNames();
		return allNames.contains(realTableName.toLowerCase()) || allNames.contains(realTableName.toUpperCase());
	}

	private String getRealTableName(Table table) {
		return table.getName() + "_" + table.getTenantID();
	}

	private String getRealTableName(String name, int tenantID) {
		return name + "_" + tenantID;
	}

	@Override
	public HResult doRandomSelect(Table table) {
		String realTableName = getRealTableName(table);
		return _sql("select * from " + realTableName + " where " + table.generateWhereClause(true));
	}

	@Override
	public HResult doRandomUpdate(Table table) {
		String realTableName = getRealTableName(table);
		return _sql("update " + realTableName + " set " + table.generateSetClause() + " where "
				+ table.generateWhereClause(true));
	}

	@Override
	public HResult doRandomInsert(Table table) {
		String realTableName = getRealTableName(table);
		String valueString = Table.convertValues(table.generateOneRow());
		return _sql("replace into " + realTableName + " value " + valueString);
	}

	@Override
	public HResult doRandomDelete(Table table) {
		String realTableName = getRealTableName(table);
		return _sql("delete from " + realTableName + " where " + table.generateWhereClause(true));
	}

	@Override
	public HResult executeSql(int tenantID, String sqlString) {
		StringTokenizer tokenizer = new StringTokenizer(sqlString);
		StringBuilder builder = new StringBuilder();
		String current = null, pre = null;
		QueryType type = null;
		int cnt = 0;
		for (; tokenizer.hasMoreTokens();) {
			pre = current;
			current = tokenizer.nextToken();
			cnt++;
			if (cnt == 1) {
				type = QueryType.getByString(current);
				if (type == QueryType.INSERT)
					current = "replace";
			}
			if (pre != null && pre.equals("from") && (type == QueryType.SELECT || type == QueryType.DELETE)) {
				current = getRealTableName(current, tenantID);
			}
			if (pre != null && pre.equals("update") && cnt == 2 && type == QueryType.UPDATE) {
				current = getRealTableName(current, tenantID);
			}
			if (pre != null && pre.equals("into") && cnt == 3 && type == QueryType.INSERT) {
				current = getRealTableName(current, tenantID);
			}
			builder.append(current + " ");
		}
		return _sql(builder.toString());
	}
}
