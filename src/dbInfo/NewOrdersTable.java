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

public class NewOrdersTable extends Table {
	final private static String TABLENAME = "new_orders";
	final private static String PROCENAME = "NewOrders";
	private Random ran;

	private int tenantid;
	private String tableNameInMySQL;
	private PreparedStatement selectStmt = null;
	private PreparedStatement updateStmt = null;

	private int voltDB_id;
	private String tableNameInVoltDB;
	private Client voltDBConn;

	private static HConfig mConf;

	public NewOrdersTable(HTenantClient htc) throws HException {
		super(htc);
		mName = "new_orders";
		mColumnNames = new String[] { "no_o_id", "no_d_id", "no_w_id" };
		mPrimaryKeyColumn = new int[] { 2, 1, 0 };
		mColumnNamesInMysql = mColumnNames.clone();
		mPrimaryKeyColumnInMysql = mPrimaryKeyColumn.clone();
		int n = mColumnNames.length;
		mColumnNamesInVoltdb = new String[n + 3];
		for (int i = 0; i < n; i++)
			mColumnNamesInVoltdb[i] = mColumnNames[i];
		mColumnNamesInVoltdb[n] = "tenant_id";
		mColumnNamesInVoltdb[n + 1] = "is_insert";
		mColumnNamesInVoltdb[n + 2] = "is_update";
		mPrimaryKeyColumnInVoltdb = new int[] { 2, 1, 0, n };
	}

	/*
	 * private String getSelectSQLPreparedStmtString() { return "SELECT * FROM "
	 * + tableNameInMySQL +
	 * " WHERE no_w_id = ? AND no_d_id = ? AND no_o_id = ?"; }
	 * 
	 * private String getUpdateSQLPreParedStmtString() { return "UPDATE " +
	 * tableNameInMySQL +
	 * " SET no_o_id = ? WHERE no_w_id = ? AND no_d_id = ? AND no_o_id = ?";
	 * 
	 * }
	 * 
	 * private static String getSaveAllSQL(int tenantid) { return
	 * "select concat(no_o_id, ',',no_d_id, ',',no_w_id, ',', '" + tenantid +
	 * "', ',', '0', ',', '0') " + "from " + TABLENAME + tenantid +
	 * " into outfile '" + mConf.getMysqlTempFolder() + "/" + TABLENAME +
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
				Constants.ORD_PER_DIST[dataSizeKind]));
		mColumnValues[1] = String.valueOf(ValueGenerator.RandomNumber(1,
				Constants.DIST_PER_WARE[dataSizeKind]));
		mColumnValues[2] = String.valueOf(ValueGenerator.RandomNumber(
				Constants.min_ware, Constants.max_ware));

	}
}
