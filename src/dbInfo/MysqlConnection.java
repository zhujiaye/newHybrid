package dbInfo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import config.Constants;
import newhybrid.NoHConnectionException;
import newhybrid.Tenant;
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
				HResult result = doSql("drop table if exists " + tableName);
				if (!result.isSuccess()) {
					throw new HSQLException("failed to drop table " + tableName + ":" + result.getMessage());
				}
			}
		} catch (SQLException e) {
			throw new HSQLException("database access error or called on a closed connection:" + e.getMessage());
		}
	}

	/**
	 * do sql operation,return only the first result
	 * 
	 * @param sqlString
	 * @return the first HResult from mysql connection
	 */
	public HResult doSql(String sqlString) {
		Statement stmt;
		// System.out.println(sqlString);
		try {
			stmt = mMysqlConnection.createStatement();
			if (stmt.execute(sqlString)) {
				return new MysqlResult(QueryType.READ, true, "success", stmt.getResultSet());
			} else {
				return new MysqlResult(QueryType.WRITE, true, "success", stmt.getUpdateCount());
			}
		} catch (SQLException e) {
			return new MysqlResult(null, false,
					"database access error or statement closed error or others:" + e.getMessage(), -1);
		}
	}

	@Override
	public void dropTable(Table table) throws HSQLException {
		String realTableName = getRealTableName(table);
		HResult result = doSql("drop table if exists " + realTableName);
		if (!result.isSuccess())
			throw new HSQLException("failed to drop table " + realTableName + ":" + result.getMessage());
	}

	@Override
	public boolean createTable(Table table) throws HSQLException {
		if (tableExist(table))
			return false;
		String realTableName = getRealTableName(table);
		StringBuffer createString = new StringBuffer("create table ");
		List<ColumnInfo> columns = table.getColumns();
		List<Integer> primary_key_pos = table.getPrimaryKeyPos();
		int columnCount = columns.size();
		createString.append(realTableName);
		createString.append("(");
		for (int i = 0; i < columnCount; i++) {
			ColumnInfo nowColumn = columns.get(i);
			if (i > 0)
				createString.append(",");
			createString.append(nowColumn.mName);
			createString.append(" ");
			if (nowColumn.mDType == DType.INT)
				createString.append("integer");
			else if (nowColumn.mDType == DType.FLOAT)
				createString.append("float");
			else if (nowColumn.mDType == DType.VARCHAR)
				createString.append("varchar(20)");
			else
				throw new HSQLException("unknown data type!");
			createString.append(" not null");
		}
		createString.append(",primary key(");
		for (int i = 0; i < primary_key_pos.size(); i++) {
			String keyName = columns.get(primary_key_pos.get(i)).mName;
			if (i > 0)
				createString.append(",");
			createString.append(keyName);
		}
		createString.append(")");
		createString.append(")");
		HResult result = doSql(createString.toString());
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
		return allNames.contains(realTableName);
	}

	private String getRealTableName(Table table) {
		return table.getName() + "_" + table.getTenant().getID();
	}

	@Override
	public HResult doRandomSelect(Table table) {
		String realTableName = getRealTableName(table);
		return doSql("select * from " + realTableName + " where " + table.generateWhereClause(true));
	}

	@Override
	public HResult doRandomUpdate(Table table) {
		String realTableName = getRealTableName(table);
		return doSql("update " + realTableName + " set " + table.generateSetClause() + " where "
				+ table.generateWhereClause(true));
	}

	@Override
	public HResult doRandomInsert(Table table) {
		String realTableName = getRealTableName(table);
		String valueString = Table.convertValues(table.generateOneRow());
		return doSql("insert ignore into " + realTableName + " value " + valueString);
	}

	@Override
	public HResult doRandomDelete(Table table) {
		String realTableName = getRealTableName(table);
		return doSql("delete from " + realTableName + " where " + table.generateWhereClause(true));
	}
}
