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

	public HQueryResult sqlRandomSelect() throws HException {
		generateAllColumns();
		if (HTC.isUseMysql()) {
			Connection mysqlConnection;
			Statement stmt;
			ResultSet result;
			long time1 = 0, time2 = 0;
			try {
				mysqlConnection = HTC.getMysqlConnection();
				if (mysqlConnection == null)
					return new HQueryResult(QueryType.SELECT, true, false,
							"no mysql connection!", time1, time2, null, null, 0);
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
				time1 = System.nanoTime();
				result = stmt.executeQuery(queryString.toString());
				time2 = System.nanoTime();
				return new HQueryResult(QueryType.SELECT, true, true,
						"success!", time1, time2, result, null, 0);
			} catch (SQLTimeoutException e) {
				return new HQueryResult(QueryType.SELECT, true, false,
						"time out! " + e.getMessage(), time1, time2, null,
						null, 0);
			} catch (SQLException e) {
				return new HQueryResult(QueryType.SELECT, true, false,
						"access denied! " + e.getMessage(), time1, time2, null,
						null, 0);
			}

		} else {
			Client voltdbConnection;
			ClientResponse response;
			VoltTable[] result;
			voltdbConnection = HTC.getVoltdbConnection();
			long time1 = 0, time2 = 0;
			if (voltdbConnection == null)
				return new HQueryResult(QueryType.SELECT, false, false,
						"no voltdb connection", time1, time2, null, null, 0);
			generateNameInVoltdb();
			generateAllColumnsInVoltdb(0, 0);
			Object[] para = new Object[mPrimaryKeyColumnInVoltdb.length];
			for (int i = 0; i < mPrimaryKeyColumnInVoltdb.length; i++) {
				int c = mPrimaryKeyColumnInVoltdb[i];
				para[i] = mColumnValuesInVoltdb[c];
			}
			try {

				time1 = System.nanoTime();
				response = voltdbConnection.callProcedure(
						mNameInVoltdb.toUpperCase() + ".select", para);
				time2 = System.nanoTime();
				if (response.getStatus() == ClientResponse.SUCCESS) {
					result = response.getResults();
					return new HQueryResult(QueryType.SELECT, false, true,
							"success", time1, time2, null, result, 0);
				} else {
					return new HQueryResult(QueryType.SELECT, false, false,
							"not success:" + response.getStatus() + " "
									+ response.getStatusString(), time1, time2,
							null, null, 0);
				}
			} catch (NoConnectionsException e) {
				return new HQueryResult(QueryType.SELECT, false, false,
						"no connection! " + e.getMessage(), time1, time2, null,
						null, 0);
			} catch (IOException e) {
				return new HQueryResult(QueryType.SELECT, false, false,
						e.getMessage(), time1, time2, null, null, 0);
			} catch (ProcCallException e) {
				return new HQueryResult(QueryType.SELECT, false, false,
						e.getMessage(), time1, time2, null, null, 0);
			}

		}
	}

	public HQueryResult sqlRandomUpdate() throws HException {
		generateAllColumns();
		long time1, time2;
		time1 = time2 = 0;
		if (HTC.isUseMysql()) {
			Connection mysqlConnection;
			Statement stmt;
			int result;
			try {
				mysqlConnection = HTC.getMysqlConnection();
				if (mysqlConnection == null)
					return new HQueryResult(QueryType.UPDATE, true, false,
							"no mysql connection!", time1, time2, null, null, 0);
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

				time1 = System.nanoTime();
				result = stmt.executeUpdate(queryString.toString());
				time2 = System.nanoTime();
				return new HQueryResult(QueryType.UPDATE, true, true,
						"success", time1, time2, null, null, result);
			} catch (SQLTimeoutException e) {
				return new HQueryResult(QueryType.UPDATE, true, false,
						"time out! " + e.getMessage(), time1, time2, null,
						null, 0);
			} catch (SQLException e) {
				return new HQueryResult(QueryType.UPDATE, true, false,
						"access denied! " + e.getMessage(), time1, time2, null,
						null, 0);
			}

		} else {
			Client voltdbConnection;
			ClientResponse response;
			VoltTable[] result;
			voltdbConnection = HTC.getVoltdbConnection();
			if (voltdbConnection == null)
				return new HQueryResult(QueryType.UPDATE, false, false,
						"no voltdb connection", time1, time2, null, null, 0);
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
				time1 = System.nanoTime();
				response = voltdbConnection.callProcedure(
						mNameInVoltdb.toUpperCase() + ".update", para);
				time2 = System.nanoTime();
				if (response.getStatus() == ClientResponse.SUCCESS) {
					result = response.getResults();
					return new HQueryResult(QueryType.UPDATE, false, true,
							"success", time1, time2, null, result, 0);
				} else {
					return new HQueryResult(QueryType.UPDATE, false, false,
							"not success:" + response.getStatus() + " "
									+ response.getStatusString(), time1, time2,
							null, null, 0);
				}
			} catch (NoConnectionsException e) {
				return new HQueryResult(QueryType.UPDATE, false, false,
						"no connection! " + e.getMessage(), time1, time2, null,
						null, 0);
			} catch (IOException e) {
				return new HQueryResult(QueryType.UPDATE, false, false,
						e.getMessage(), time1, time2, null, null, 0);
			} catch (ProcCallException e) {
				return new HQueryResult(QueryType.UPDATE, false, false,
						e.getMessage(), time1, time2, null, null, 0);
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
