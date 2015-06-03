package dbInfo;

import java.io.IOException;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.util.Random;

import newhybrid.HException;

import org.voltdb.VoltTable;
import org.voltdb.client.Client;
import org.voltdb.client.ClientFactory;
import org.voltdb.client.ClientResponse;
import org.voltdb.client.NoConnectionsException;
import org.voltdb.client.ProcCallException;

import client.HTenantClient;
import utillity.ValueGenerator;
import config.Constants;
import config.HConfig;

public class HistoryTable extends Table {
	public HistoryTable(HTenantClient htc) throws HException {
		super(htc);
		mName = "history";
		mColumnNames = new String[] { "h_c_id", "h_c_d_id", "h_c_w_id",
				"h_d_id", "h_w_id", "h_date", "h_amount", "h_data" };

		mPrimaryKeyColumn = new int[] { 0, 1, 2 };
		mColumnNamesInMysql = mColumnNames.clone();
		mPrimaryKeyColumnInMysql = mPrimaryKeyColumn.clone();
		int n = mColumnNames.length;
		mColumnNamesInVoltdb = new String[n + 3];
		for (int i = 0; i < n; i++)
			mColumnNamesInVoltdb[i] = mColumnNames[i];
		mColumnNamesInVoltdb[n] = "tenant_id";
		mColumnNamesInVoltdb[n + 1] = "is_insert";
		mColumnNamesInVoltdb[n + 2] = "is_update";
		mPrimaryKeyColumnInVoltdb = new int[] { 0, 1, 2, n };
	}

	/*
	 * private String getSelectSQLPreparedStmtString() { return "SELECT * FROM "
	 * + tableNameInMySQL +
	 * " WHERE h_c_id = ? AND h_c_d_id = ? AND h_c_w_id = ?"; }
	 * 
	 * private String getUpdateSQLPreParedStmtString() { return "UPDATE " +
	 * tableNameInMySQL +
	 * " SET h_d_id = ?,h_w_id = ?,h_date = ?,h_amount = ?,h_data = ? WHERE h_c_id = ? AND h_c_d_id = ? AND h_c_w_id = ?"
	 * ;
	 * 
	 * }
	 * 
	 * private static String getSaveAllSQL(int tenantid) { return
	 * "select concat(h_c_id , ',', h_c_d_id, ',', h_c_w_id, ',',h_d_id, ',',h_w_id, ',',h_date, ',',h_amount, ',',h_data, ',', '"
	 * + tenantid + "', ',', '0', ',', '0' ) " + "from " + TABLENAME + tenantid
	 * + " into outfile '" + mConf.getMysqlTempFolder() + "/" + TABLENAME +
	 * tenantid + ".csv'";
	 * 
	 * }
	 * 
	 * private static String getSavedFileName(int tenantid) { return "/" +
	 * TABLENAME + tenantid + ".csv"; }
	 * 
	 * private static String getProcedureName(int voltDB_id) { return "Offload"
	 * + PROCENAME + "_" + voltDB_id; }
	 * 
	 * public static synchronized void deleteVoltDBID(int voltDB_id) { Client
	 * client = null; ClientResponse response = null; client =
	 * ClientFactory.createClient(); try {
	 * client.createConnection(mConf.getVoltdbServerAddress()); try { response =
	 * client.callProcedure("@AdHoc", "delete from " + TABLENAME + voltDB_id +
	 * ";"); } catch (ProcCallException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } if (response.getStatus() !=
	 * ClientResponse.SUCCESS) { System.out.println("Failed to delete " +
	 * TABLENAME + " table in " + voltDB_id + "th slot"); } } catch
	 * (UnknownHostException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } catch (IOException e) { // TODO Auto-generated
	 * catch block e.printStackTrace(); } finally { if (client != null) try {
	 * client.close(); } catch (InterruptedException e) { // TODO Auto-generated
	 * catch block e.printStackTrace(); } } }
	 */
	@Override
	public void generateAllColumns() throws HException {
		if (mColumnValues == null)
			mColumnValues = new String[mColumnNames.length];
		int dataSizeKind = HTC.getDataSizeKind();
		mColumnValues[0] = String.valueOf(ValueGenerator.RandomNumber(1,
				Constants.CUST_PER_DIST[dataSizeKind]));
		mColumnValues[1] = String.valueOf(ValueGenerator.RandomNumber(1,
				Constants.DIST_PER_WARE[dataSizeKind]));
		mColumnValues[2] = String.valueOf(ValueGenerator.RandomNumber(
				Constants.min_ware, Constants.max_ware));
		mColumnValues[3] = mColumnValues[1];
		mColumnValues[4] = mColumnValues[2];
		mColumnValues[5] = ValueGenerator.getTimeStamp();
		mColumnValues[6] = String.valueOf(10.0);
		mColumnValues[7] = ValueGenerator.MakeAlphaString(12, 24);
	}

}
