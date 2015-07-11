package server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;


import org.voltdb.client.Client;
import org.voltdb.client.ProcCallException;

import config.Constants;
import config.HConfig;

public class MysqlToVoltdbMover {
	final private HConfig mConf;

	public int tenantId;
	public int volumnId;
	public int tableId;
	public Connection conn;
	public Client client;

	/**
	 * 
	 * @param tenantId
	 *            this variable is 0-index based
	 * @param volumnId
	 *            this variable is 0-index based
	 * @param tableId
	 *            this variable is 0-index based
	 * @param conn
	 * @param client
	 */
	public MysqlToVoltdbMover(int tenantId, int volumnId, int tableId,
			Connection conn, Client client) {
		mConf = HConfig.getConf();
		this.tenantId = tenantId;
		this.volumnId = volumnId;
		this.tableId = tableId;
		this.conn = conn;
		this.client = client;
	}

	public final static int CUSTOMER = 0;
	public final static int DISTRICT = 1;
	public final static int HISTORY = 2;
	public final static int ITEM = 3;
	public final static int NEW_ORDERS = 4;
	public final static int ORDER_LINE = 5;
	public final static int ORDERS = 6;
	public final static int STOCK = 7;
	public final static int WAREHOUSE = 8;
	public static final String[] tables = { "customer", "district", "history",
			"item", "new_orders", "order_line", "orders", "stock", "warehouse" };
	public static final String[] Tables = { "Customer", "District", "History",
			"Item", "NewOrders", "OrderLine", "Orders", "Stock", "Warehouse" };

	private void clearCSVfile(String filename) throws InterruptedException {
		String[] cmd = { "/bin/sh", "-c", "rm -r " + filename };
		try {
			Process process = Runtime.getRuntime().exec(cmd);
			process.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getSQL(int tableId) {
		String ret = null;
		switch (tableId) {
		case CUSTOMER:
			ret = "select concat(c_id, ',', c_d_id, ',',c_w_id, ',', c_first, ',', c_middle, ',', c_last, ',', c_street_1, ',', c_street_2, ',',c_city, ',',c_state, ',',c_zip, ',', c_phone, ',',c_since, ',', c_credit, ',', c_credit_lim , ',', c_discount, ',', c_balance, ',', c_ytd_payment, ',',c_payment_cnt, ',', c_delivery_cnt, ',', c_data, ',','"
					+ tenantId
					+ "', ',', '0', ',', '0')"
					+ " from customer"
					+ tenantId
					+ " into outfile '"
					+ mConf.getMysqlTempFolder()
					+ "/customer" + tenantId + ".csv'";
			break;
		case DISTRICT:
			ret = "select concat(d_id, ',', d_w_id, ',', d_name, ',', d_street_1, ',', d_street_2, ',', d_city, ',', d_state, ',', d_zip, ',', d_tax, ',', d_ytd, ',', d_next_o_id, ',', '"
					+ tenantId
					+ "', ',', '0', ',', '0') "
					+ "from district"
					+ tenantId
					+ " into outfile '"
					+ mConf.getMysqlTempFolder()
					+ "/district" + tenantId + ".csv'";
			break;
		case HISTORY:
			ret = "select concat(h_c_id , ',', h_c_d_id, ',', h_c_w_id, ',',h_d_id, ',',h_w_id, ',',h_date, ',',h_amount, ',',h_data, ',', '"
					+ tenantId
					+ "', ',', '0', ',', '0' ) "
					+ "from history"
					+ tenantId
					+ " into outfile '"
					+ mConf.getMysqlTempFolder()
					+ "/history" + tenantId + ".csv'";
			break;
		case ITEM:
			ret = "select concat(i_id, ',', i_im_id, ',', i_name, ',', i_price, ',', i_data, ',', '"
					+ tenantId
					+ "', ',', '0', ',', '0') "
					+ "from item"
					+ tenantId
					+ " into outfile '"
					+ mConf.getMysqlTempFolder()
					+ "/item" + tenantId + ".csv'";
			break;
		case NEW_ORDERS:
			ret = "select concat(no_o_id, ',',no_d_id, ',',no_w_id, ',', '"
					+ tenantId + "', ',', '0', ',', '0') " + "from new_orders"
					+ tenantId + " into outfile '" + mConf.getMysqlTempFolder()
					+ "/new_orders" + tenantId + ".csv'";
			break;
		case ORDER_LINE:
			ret = "select concat(ol_o_id, ',', ol_d_id, ',',ol_w_id, ',',ol_number, ',',ol_i_id, ',', ol_supply_w_id, ',',ol_delivery_d, ',', ol_quantity, ',', ol_amount, ',', ol_dist_info, ',', '"
					+ tenantId
					+ "', ',', '0', ',', '0') "
					+ "from order_line"
					+ tenantId
					+ " into outfile '"
					+ mConf.getMysqlTempFolder()
					+ "/order_line" + tenantId + ".csv'";
			break;
		case ORDERS:
			ret = "select concat(o_id, ',', o_d_id, ',', o_w_id, ',', o_c_id, ',', o_entry_d, ',', o_carrier_id, ',', o_ol_cnt, ',', o_all_local, ',', '"
					+ tenantId
					+ "', ',', '0', ',', '0') "
					+ "from orders"
					+ tenantId
					+ " into outfile '"
					+ mConf.getMysqlTempFolder()
					+ "/orders" + tenantId + ".csv'";
			break;
		case STOCK:
			ret = "select concat(s_i_id, ',', s_w_id, ',', s_quantity, ',', s_dist_01, ',', s_dist_02, ',',s_dist_03, ',',s_dist_04, ',', s_dist_05, ',', s_dist_06, ',', s_dist_07, ',', s_dist_08, ',', s_dist_09, ',', s_dist_10, ',', s_ytd, ',', s_order_cnt, ',', s_remote_cnt, ',',s_data, ',', '"
					+ tenantId
					+ "', ',', '0', ',', '0') "
					+ "from stock"
					+ tenantId
					+ " into outfile '"
					+ mConf.getMysqlTempFolder()
					+ "/stock" + tenantId + ".csv'";
			break;
		case WAREHOUSE:
			ret = "select concat(w_id, ',',	w_name, ',',w_street_1, ',',w_street_2, ',',w_city, ',',w_state, ',',w_zip, ',',w_tax, ',',	w_ytd, ',', '"
					+ tenantId
					+ "', ',', '0', ',', '0') "
					+ "from warehouse"
					+ tenantId
					+ " into outfile '"
					+ mConf.getMysqlTempFolder()
					+ "/warehouse" + tenantId + ".csv'";
			break;
		default:
		}
		return ret;
	}

	/**
	 * move data from mysql to voltdb
	 * 
	 * @throws InterruptedException
	 * @throws InterruptedIOException 
	 */
	public void move() throws InterruptedException, InterruptedIOException {
		try {
			Statement stmt = null;
			stmt = conn.createStatement();
			// System.out.println("clearing "+mConf.getMysqlTempFolder() + "/" +
			// tables[tableId]
			// + tenantId + ".csv");
			clearCSVfile(mConf.getMysqlTempFolder() + "/" + tables[tableId]
					+ tenantId + ".csv");
			stmt.execute(getSQL(tableId));
			client.callProcedure("@AdHoc", "delete from " + tables[tableId]
					+ volumnId + " where tenant_id = " + tenantId);

			FileReader filereader = new FileReader(mConf.getMysqlTempFolder()
					+ "/" + tables[tableId] + tenantId + ".csv");
			BufferedReader reader = new BufferedReader(filereader);
			String[] lines = new String[Constants.OFFLOADER_BATCH];
			String str;
			int count = 0;
			while ((str = reader.readLine()) != null) {
				lines[count] = str;
				count++;
				if (count == Constants.OFFLOADER_BATCH) {
					client.callProcedure("Offload" + Tables[tableId] + "_"
							+ volumnId, tenantId, lines, count);
					count = 0;
				}
			}
			if (count > 0)
				client.callProcedure("Offload" + Tables[tableId] + "_"
						+ volumnId, tenantId, lines, count);
			filereader.close();
			reader.close();
			conn.close();
			client.drain();
			client.close();
		} catch (SQLException | IOException | ProcCallException e) {
			if (e instanceof InterruptedIOException)
				throw (InterruptedIOException) e;
			e.printStackTrace();
		}finally{
			clearCSVfile(mConf.getMysqlTempFolder() + "/" + tables[tableId]
					+ tenantId + ".csv");
		}
	}

}
