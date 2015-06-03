package dbInfo;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.sql.Statement;

import newhybrid.HException;
import newhybrid.HQueryResult;
import newhybrid.HSQLTimeOutException;
import newhybrid.QueryType;

import org.voltdb.VoltTable;
import org.voltdb.client.Client;
import org.voltdb.client.ClientResponse;
import org.voltdb.client.NoConnectionsException;
import org.voltdb.client.ProcCallException;

import config.HConfig;
import client.HTenantClient;

public abstract class Table {
	protected final HConfig mConf;
	protected final HTenantClient HTC;

	protected String mName;
	protected String[] mColumnNames;
	protected String[] mColumnValues;
	protected int[] mPrimaryKeyColumn;

	protected String mNameInMysql;
	protected String[] mColumnNamesInMysql;
	protected String[] mColumnValuesInMysql;
	protected int[] mPrimaryKeyColumnInMysql;

	protected String mNameInVoltdb;
	protected String[] mColumnNamesInVoltdb;
	protected String[] mColumnValuesInVoltdb;
	protected int[] mPrimaryKeyColumnInVoltdb;

	Table(HTenantClient htc) throws HException {
		HTC = htc;
		mConf = HConfig.getConf();
	}

	/*
	 * No use of PreparedStatement public void updatePreparedStatement() throws
	 * HException { updateSelectPreparedStatement();
	 * updateUpdatePreparedStatement();
	 * 
	 * }
	 * 
	 * public void updateSelectPreparedStatement() throws HException {
	 * Connection mysqlConnection; mysqlConnection = HTC.getMysqlConnection();
	 * try { if (mSelectStmt != null) { if (mSelectStmt.getConnection() ==
	 * mysqlConnection) return; mSelectStmt.close(); } } catch (SQLException e)
	 * { throw new HException(e.getMessage()); } if (mysqlConnection == null)
	 * return; mysqlConnection.prepareStatement(sql); }
	 */

	public HQueryResult sqlRandomSelect() throws HException,
			HSQLTimeOutException {
		generateAllColumns();
		if (HTC.isUseMysql()) {
			Connection mysqlConnection;
			Statement stmt;
			ResultSet result;
			try {
				mysqlConnection = HTC.getMysqlConnection();
				if (mysqlConnection == null)
					return new HQueryResult(null);
				generateNameInMysql();
				generateAllColumnsInMysql();
				stmt = mysqlConnection.createStatement();
				StringBuilder queryString = new StringBuilder("SELECT * FROM "
						+ mNameInMysql + " WHERE ");
				for (int i = 0; i < mPrimaryKeyColumnInMysql.length; i++) {
					int c = mPrimaryKeyColumnInMysql[i];
					if (i > 0)
						queryString.append(" AND ");
					queryString.append(mColumnNamesInMysql[c] + "=" + "\""
							+ mColumnValuesInMysql[c] + "\"");
				}
				long time1, time2;
				time1 = System.nanoTime();
				result = stmt.executeQuery(queryString.toString());
				time2 = System.nanoTime();
				return new HQueryResult(QueryType.SELECT, time1, time2, result,
						null, 0);
			} catch (SQLTimeoutException e) {
				throw new HSQLTimeOutException(e.getMessage());
			} catch (SQLException e) {
				throw new HException(e.getMessage());
			}

		} else {
			Client voltdbConnection;
			ClientResponse response;
			VoltTable[] result;
			voltdbConnection = HTC.getVoltdbConnection();
			if (voltdbConnection == null)
				return new HQueryResult(null);
			generateNameInVoltdb();
			generateAllColumnsInVoltdb(0, 0);
			Object[] para = new Object[mPrimaryKeyColumnInVoltdb.length];
			for (int i = 0; i < mPrimaryKeyColumnInVoltdb.length; i++) {
				int c = mPrimaryKeyColumnInVoltdb[i];
				para[i] = mColumnValuesInVoltdb[c];
			}
			try {
				long time1, time2;
				time1 = System.nanoTime();
				response = voltdbConnection.callProcedure(
						mNameInVoltdb.toUpperCase() + ".select", para);
				time2 = System.nanoTime();
				if (response.getStatus() == ClientResponse.SUCCESS) {
					result = response.getResults();
					return new HQueryResult(QueryType.SELECT, time1, time2,
							null, result, 0);
				} else
					return new HQueryResult(null);
			} catch (NoConnectionsException e) {
				throw new HException(e.getMessage());
			} catch (IOException e) {
				throw new HException(e.getMessage());
			} catch (ProcCallException e) {
				throw new HException(e.getMessage());
			}

		}
	}

	public HQueryResult sqlRandomUpdate() throws HException,
			HSQLTimeOutException {
		generateAllColumns();
		if (HTC.isUseMysql()) {
			Connection mysqlConnection;
			Statement stmt;
			int result;
			try {
				mysqlConnection = HTC.getMysqlConnection();
				if (mysqlConnection == null)
					return new HQueryResult(null);
				generateNameInMysql();
				generateAllColumnsInMysql();
				stmt = mysqlConnection.createStatement();
				StringBuilder queryString = new StringBuilder("UPDATE "
						+ mNameInMysql + " SET ");
				for (int i = 0; i < mColumnNamesInMysql.length; i++) {
					if (i > 0)
						queryString.append(" , ");
					queryString.append(mColumnNamesInMysql[i] + "=" + "\""
							+ mColumnValuesInMysql[i] + "\"");
				}
				queryString.append(" WHERE ");
				for (int i = 0; i < mPrimaryKeyColumnInMysql.length; i++) {
					int c = mPrimaryKeyColumnInMysql[i];
					if (i > 0)
						queryString.append(" AND ");
					queryString.append(mColumnNamesInMysql[c] + "=" + "\""
							+ mColumnValuesInMysql[c] + "\"");
				}
				long time1, time2;
				time1 = System.nanoTime();
				result = stmt.executeUpdate(queryString.toString());
				time2 = System.nanoTime();
				return new HQueryResult(QueryType.UPDATE, time1, time2, null,
						null, result);
			} catch (SQLTimeoutException e) {
				throw new HSQLTimeOutException(e.getMessage());
			} catch (SQLException e) {
				throw new HException(e.getMessage());
			}

		} else {
			Client voltdbConnection;
			ClientResponse response;
			VoltTable[] result;
			voltdbConnection = HTC.getVoltdbConnection();
			if (voltdbConnection == null)
				return new HQueryResult(null);
			generateNameInVoltdb();
			generateAllColumnsInVoltdb(0, 1);
			Object[] para = new Object[mColumnValuesInVoltdb.length
					+ mPrimaryKeyColumnInVoltdb.length];
			for (int i = 0; i < mColumnValuesInVoltdb.length; i++) {
				para[i] = mColumnValuesInVoltdb[i];
			}
			for (int i = 0; i < mPrimaryKeyColumnInVoltdb.length; i++) {
				int c = mPrimaryKeyColumnInVoltdb[i];
				para[mColumnValuesInVoltdb.length + i] = mColumnValuesInVoltdb[c];
			}
			try {
				long time1, time2;
				time1 = System.nanoTime();
				response = voltdbConnection.callProcedure(
						mNameInVoltdb.toUpperCase() + ".update", para);
				time2 = System.nanoTime();
				if (response.getStatus() == ClientResponse.SUCCESS) {
					result = response.getResults();
					return new HQueryResult(QueryType.UPDATE, time1, time2,
							null, result, 0);
				} else
					return new HQueryResult(null);
			} catch (NoConnectionsException e) {
				throw new HException(e.getMessage());
			} catch (IOException e) {
				throw new HException(e.getMessage());
			} catch (ProcCallException e) {
				throw new HException(e.getMessage());
			}

		}
	}

	public void generateNameInMysql() {
		mNameInMysql = mName + (HTC.getID() - 1);
	}

	public void generateNameInVoltdb() throws HException {
		mNameInVoltdb = mName + (HTC.getIDInVoltdb() - 1);
	}

	public abstract void generateAllColumns() throws HException;

	public void generateAllColumnsInMysql() {
		mColumnValuesInMysql = mColumnValues.clone();
	}

	public void generateAllColumnsInVoltdb(int isInsert, int isUpdate) {
		if (mColumnValuesInVoltdb == null) {
			mColumnValuesInVoltdb = new String[mColumnNamesInVoltdb.length];
		}
		int n = mColumnNames.length;
		for (int i = 0; i < n; i++)
			mColumnValuesInVoltdb[i] = mColumnValues[i];
		mColumnValuesInVoltdb[n] = String.valueOf(HTC.getID());
		mColumnValuesInVoltdb[n + 1] = String.valueOf(isInsert);
		mColumnValuesInVoltdb[n + 2] = String.valueOf(isUpdate);

	}
}
