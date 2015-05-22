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

public class OrdersTable extends Table {
	public OrdersTable(HTenantClient htc) throws HException {
		super(htc);
		mName = "orders";
		mColumnNames = new String[] { "o_id", "o_d_id", " o_w_id", "o_c_id",
				"o_entry_d", "o_carrier_id", "o_ol_cnt", "o_all_local" };

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
	 * + tableNameInMySQL + " WHERE o_w_id = ? AND o_d_id = ? AND o_id = ?"; }
	 * 
	 * private String getUpdateSQLPreParedStmtString() { return "UPDATE " +
	 * tableNameInMySQL +
	 * " SET o_c_id = ?,o_entry_d = ?,o_carrier_id = ?,o_ol_cnt = ?, o_all_local = ? WHERE o_w_id = ? AND o_d_id = ? AND o_id = ?"
	 * ;
	 * 
	 * }
	 * 
	 * private static String getSaveAllSQL(int tenantid) { return
	 * "select concat(o_id, ',', o_d_id, ',', o_w_id, ',', o_c_id, ',', o_entry_d, ',', o_carrier_id, ',', o_ol_cnt, ',', o_all_local, ',', '"
	 * + tenantid + "', ',', '0', ',', '0') " + "from " + TABLENAME + tenantid +
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
	public void generateAllColumns() {
		if (mColumnValues == null)
			mColumnValues = new String[mColumnNames.length];
		Random ran = new Random(System.currentTimeMillis());
		mColumnValues[0] = String.valueOf(ValueGenerator.RandomNumber(1,
				Constants.ORD_PER_DIST));
		mColumnValues[1] = String.valueOf(ValueGenerator.RandomNumber(1,
				Constants.DIST_PER_WARE));
		mColumnValues[2] = String.valueOf(ValueGenerator.RandomNumber(
				Constants.min_ware, Constants.max_ware));
		mColumnValues[3] = String.valueOf(ValueGenerator.RandomNumber(1,
				Constants.ORD_PER_DIST));
		mColumnValues[4] = String.valueOf(ValueGenerator.getTimeStamp());
		mColumnValues[5] = String.valueOf(ValueGenerator.RandomNumber(1, 10));
		mColumnValues[6] = String.valueOf(ValueGenerator.RandomNumber(5, 15));
		mColumnValues[7] = String.valueOf(ran.nextInt(100) == 1 ? 0 : 1);

	}

}
