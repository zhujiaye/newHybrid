package dbInfo;

import java.io.IOException;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.sql.Statement;
import java.util.Random;

import newhybrid.HException;

import org.voltdb.VoltTable;
import org.voltdb.client.Client;
import org.voltdb.client.ClientFactory;
import org.voltdb.client.ClientResponse;
import org.voltdb.client.NoConnectionsException;
import org.voltdb.client.ProcCallException;

import client.HTenantClient;
import config.Constants;
import config.HConfig;
import utillity.ValueGenerator;

public class CustomerTable extends Table {

	public CustomerTable(HTenantClient htc) throws HException {
		super(htc);
		mName = "customer";
		mColumnNames = new String[] { "c_id", "c_d_id", "c_w_id", "c_first",
				"c_middle", "c_last", "c_street_1", "c_street_2", "c_city",
				"c_state", "c_zip", "c_phone", "c_since", "c_credit",
				"c_credit_lim ", "c_discount", "c_balance", "c_ytd_payment",
				"c_payment_cnt", "c_delivery_cnt", "c_data" };
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
		mPrimaryKeyColumnInVoltdb = new int[] { 0, 2, 1, n };
	}

	/*
	 * private String getSelectSQLPreparedStmtString() { return "SELECT * FROM "
	 * + tableNameInMySQL + " WHERE c_id = ? AND c_d_id = ? AND c_w_id = ?"; }
	 * 
	 * private String getUpdateSQLPreParedStmtString() { return "UPDATE " +
	 * tableNameInMySQL +
	 * " SET c_first = ?, c_middle = ?, c_last = ?, c_street_1 = ?, c_street_2 = ?,c_city = ?,"
	 * +
	 * "c_state = ?,c_zip = ?, c_phone = ?,c_since = ?, c_credit = ?, c_credit_lim = ?, c_discount = ?, c_balance = ?, c_ytd_payment = ?,c_payment_cnt = ?, c_delivery_cnt = ?, c_data = ? "
	 * + "WHERE c_id = ? AND c_d_id = ? AND c_w_id = ?"; }
	 * 
	 * private static String getSaveAllSQL(int tenantid) { return
	 * "select concat(c_id, ',', c_d_id, ',',c_w_id, ',', c_first, ',', c_middle, ',', c_last, ',', c_street_1, ',', c_street_2, ',',c_city, ',',c_state, ',',c_zip, ',', c_phone, ',',c_since, ',', c_credit, ',', c_credit_lim , ',', c_discount, ',', c_balance, ',', c_ytd_payment, ',',c_payment_cnt, ',', c_delivery_cnt, ',', c_data, ',','"
	 * + tenantid + "', ',', '0', ',', '0')" + " from customer" + (tenantid - 1)
	 * + " into outfile '" + mConf.getMysqlTempFolder() + "/customer" + tenantid
	 * + ".csv'"; }
	 * 
	 * private static String getSavedFileName(int tenantid) { return "/customer"
	 * + tenantid + ".csv"; }
	 * 
	 * private static String getProcedureName(int voltDB_id) { return
	 * "OffloadCustomer_" + voltDB_id; }
	 * 
	 * public static synchronized void deleteVoltDBID(int voltDB_id) { Client
	 * client = null; ClientResponse response = null; client =
	 * ClientFactory.createClient(); try {
	 * client.createConnection(mConf.getVoltdbServerAddress()); try { response =
	 * client.callProcedure("@AdHoc", "delete from " + TABLENAME + voltDB_id +
	 * ";"); } catch (ProcCallException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } if (response.getStatus() !=
	 * ClientResponse.SUCCESS) {
	 * System.out.println("Failed to delete customer table in " + voltDB_id +
	 * "th slot"); } } catch (UnknownHostException e) { // TODO Auto-generated
	 * catch block e.printStackTrace(); } catch (IOException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); } finally { if (client !=
	 * null) try { client.close(); } catch (InterruptedException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); } } }
	 */
	@Override
	public void generateAllColumns() {
		if (mColumnValues == null)
			mColumnValues = new String[mColumnNames.length];
		Random ran = new Random(System.currentTimeMillis());
		mColumnValues[0] = String.valueOf(ValueGenerator.RandomNumber(1,
				Constants.CUST_PER_DIST));
		mColumnValues[1] = String.valueOf(ValueGenerator.RandomNumber(1,
				Constants.DIST_PER_WARE));
		mColumnValues[2] = String.valueOf(ValueGenerator.RandomNumber(
				Constants.min_ware, Constants.max_ware));
		mColumnValues[3] = ValueGenerator.MakeAlphaString(8, 16);
		mColumnValues[4] = "OE";
		mColumnValues[5] = ValueGenerator.Lastname(ValueGenerator.NURand(255,
				0, 999));
		mColumnValues[6] = ValueGenerator.MakeAlphaString(10, 20);
		mColumnValues[7] = ValueGenerator.MakeAlphaString(10, 20);
		mColumnValues[8] = ValueGenerator.MakeAlphaString(10, 20);
		mColumnValues[9] = ValueGenerator.MakeAlphaString(2, 2);
		mColumnValues[10] = ValueGenerator.MakeNumberString(9, 9);
		mColumnValues[11] = ValueGenerator.MakeNumberString(16, 16);
		mColumnValues[12] = ValueGenerator.getTimeStamp();
		mColumnValues[13] = "C0";
		mColumnValues[14] = String.valueOf(50000);
		mColumnValues[15] = String.valueOf(ran.nextInt(50) / 100.0);
		mColumnValues[16] = String.valueOf(10.0);
		mColumnValues[17] = String.valueOf(10.0);
		mColumnValues[18] = String.valueOf(1);
		mColumnValues[19] = String.valueOf(0);
		mColumnValues[20] = ValueGenerator.MakeAlphaString(300, 500);
	}
}
