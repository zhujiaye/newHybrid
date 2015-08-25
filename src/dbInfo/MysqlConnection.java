package dbInfo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import config.Constants;
import newhybrid.NoHConnectionException;
import thrift.DbmsInfo;

public class MysqlConnection extends HConnection {
	static private final Logger LOG = Logger.getLogger(Constants.LOGGER_NAME);
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
	public void release() {
		if (mMysqlConnection == null)
			return;
		try {
			mMysqlConnection.close();
		} catch (SQLException e) {
			LOG.error(e.getMessage());
		} finally {
			mMysqlConnection = null;
		}
	}

	@Override
	public void dropAll() {
		try {
			ResultSet result = mMysqlConnection.getMetaData().getTables(null, null, null, null);
			while (result.next()) {
				for (int index = 1; index <= 4; index++) {
					System.out.println(result.getString(index) + " ");
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public HResult doRandomSelect(Table table) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HResult doRandomUpdate(Table table) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * do sql operation,return only the first result
	 * 
	 * @param sqlString
	 * @return the first HResult from mysql connection
	 */
	public HResult doSql(String sqlString) {
		Statement stmt;
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
}
