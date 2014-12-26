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

import org.voltdb.VoltTable;
import org.voltdb.client.Client;
import org.voltdb.client.ClientFactory;
import org.voltdb.client.ClientResponse;
import org.voltdb.client.NoConnectionsException;
import org.voltdb.client.ProcCallException;

import config.HConfig;
import utillity.Support;

public class CustomerTable extends Table {
	final private static String TABLENAME = "customer";
	private Random ran;

	private int tenantid;
	private String tableNameInMySQL;
	private PreparedStatement selectStmt = null;
	private PreparedStatement updateStmt = null;

	private int voltDB_id;
	private String tableNameInVoltDB;
	private Client voltDBConn;

	public CustomerTable(int tenantid) {
		this.tenantid = tenantid;
		this.tableNameInMySQL = TABLENAME.concat(String.valueOf(tenantid - 1));
		ran = new Random(System.currentTimeMillis());
	}

	public void update(Connection conn) {
		try {
			if (selectStmt != null) {
				selectStmt.close();
			}
			if (updateStmt != null) {
				updateStmt.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setSelectSQLPreparedStmt(conn);
		setUpdateSQLPreparedStmt(conn);
	}

	public void update(Client voltDBConn) {
		this.voltDBConn = voltDBConn;
	}

	public void update(int idInVoltDB) {
		voltDB_id = idInVoltDB;
		tableNameInVoltDB = TABLENAME.concat(String.valueOf(idInVoltDB));
	}

	public ResultSet doSelectInMySQL() {
		ResultSet rs = null;

		if (selectStmt == null) {
			System.out.println("Error:can not do select SQL statements");
			return null;
		}

		try {
			selectStmt.setInt(1,
					Support.RandomNumber(1, TableInfo.CUST_PER_DIST));
			selectStmt.setInt(2,
					Support.RandomNumber(1, TableInfo.DIST_PER_WARE));
			selectStmt.setInt(3, Support.RandomNumber(TableInfo.min_ware,
					TableInfo.max_ware));
			rs = selectStmt.executeQuery();
		} catch (SQLTimeoutException e) {
			System.out.println("Tenant " + tenantid + ": select time out!");
			return null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("@@@@@@@@@@" + e.getMessage() + "&"
					+ e.getErrorCode() + "&" + e.getSQLState()
					+ ".WTF!@@@@@@@@@@@");
			System.exit(1);
			return null;
		}
		return rs;
	}

	public int doUpdateInMySQL() {
		int res;

		if (updateStmt == null) {
			System.out.println("Error:can not do update SQL statements");
			return -1;
		}

		try {
			updateStmt.setString(1, Support.MakeAlphaString(8, 16));
			updateStmt.setString(2, "OE");
			updateStmt.setString(3,
					Support.Lastname(Support.NURand(255, 0, 999)));
			updateStmt.setString(4, Support.MakeAlphaString(10, 20));
			updateStmt.setString(5, Support.MakeAlphaString(10, 20));
			updateStmt.setString(6, Support.MakeAlphaString(10, 20));
			updateStmt.setString(7, Support.MakeAlphaString(2, 2));
			updateStmt.setString(8, Support.MakeNumberString(9, 9));
			updateStmt.setString(9, Support.MakeNumberString(16, 16));
			updateStmt.setString(10, Support.getTimeStamp());
			updateStmt.setString(11, "C0");
			updateStmt.setInt(12, 50000);
			updateStmt.setDouble(13, ran.nextInt(50) / 100.0);
			updateStmt.setDouble(14, 10.0);
			updateStmt.setDouble(15, 10.0);
			updateStmt.setInt(16, 1);
			updateStmt.setInt(17, 0);
			updateStmt.setString(18, Support.MakeAlphaString(300, 500));

			updateStmt.setInt(19,
					Support.RandomNumber(1, TableInfo.CUST_PER_DIST));
			updateStmt.setInt(20,
					Support.RandomNumber(1, TableInfo.DIST_PER_WARE));
			updateStmt.setInt(21, Support.RandomNumber(TableInfo.min_ware,
					TableInfo.max_ware));
			res = updateStmt.executeUpdate();
		} catch (SQLTimeoutException e) {
			System.out.println("Tenant " + tenantid + ": update time out!");
			return -1;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("@@@@@@@@@@" + e.getMessage() + "&"
					+ e.getErrorCode() + "&" + e.getSQLState()
					+ ".WTF!@@@@@@@@@@@");
			System.exit(1);
			return -1;
		}
		return res;
	}

	public int doUpdateInVoltDB() {
		if (voltDBConn == null)
			return -1;
		ClientResponse res = null;
		Object[] para = new Object[28];
		int c_id, c_d_id, c_w_id;
		para[0] = c_id = Support.RandomNumber(1, TableInfo.CUST_PER_DIST);
		para[1] = c_d_id = Support.RandomNumber(1, TableInfo.DIST_PER_WARE);
		para[2] = c_w_id = Support.RandomNumber(TableInfo.min_ware,
				TableInfo.max_ware);
		para[3] = Support.MakeAlphaString(8, 16);
		para[4] = "OE";
		para[5] = Support.Lastname(Support.NURand(255, 0, 999));
		para[6] = Support.MakeAlphaString(10, 20);
		para[7] = Support.MakeAlphaString(10, 20);
		para[8] = Support.MakeAlphaString(10, 20);
		para[9] = Support.MakeAlphaString(2, 2);
		para[10] = Support.MakeNumberString(9, 9);
		para[11] = Support.MakeNumberString(16, 16);
		para[12] = Support.getTimeStamp();
		para[13] = "C0";
		para[14] = 50000;
		para[15] = ran.nextInt(50) / 100.0;
		para[16] = 10.0;
		para[17] = 10.0;
		para[18] = 1;
		para[19] = 0;
		para[20] = Support.MakeAlphaString(300, 500);
		para[21] = tenantid;
		para[22] = 0;
		para[23] = 1;
		para[24] = c_id;
		para[25] = c_w_id;
		para[26] = c_d_id;
		para[27] = tenantid;

		try {
			// voltDBConn.createConnection(HConfig.VOLTDB_SERVER);
			res = voltDBConn.callProcedure(tableNameInVoltDB.toUpperCase()
					+ ".update", para);
			if (res.getStatus() == ClientResponse.SUCCESS) {
				res.getResults();
				return 0;
			} else
				return -1;
		} catch (NoConnectionsException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			System.out
					.println("--------No connections. Try to get a new connection-------");
			try {
				voltDBConn.createConnection(HConfig.VOLTDB_SERVER);
				res = voltDBConn.callProcedure(tableNameInVoltDB.toUpperCase()
						+ ".update", para);

				if (res.getStatus() == ClientResponse.SUCCESS) {
					res.getResults();
					return 0;
				} else {
					System.out.println(res.getStatusString() + "&"
							+ res.getStatus());
					System.exit(1);
					return -1;
				}
			} catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (ProcCallException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProcCallException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}

	public VoltTable[] doSelectInVoltDB() {
		if (voltDBConn == null)
			return null;
		ClientResponse response = null;
		VoltTable[] res = null;
		Object[] para = new Object[4];
		int c_id, c_d_id, c_w_id;
		para[0] = c_id = Support.RandomNumber(1, TableInfo.CUST_PER_DIST);
		para[2] = c_d_id = Support.RandomNumber(1, TableInfo.DIST_PER_WARE);
		para[1] = c_w_id = Support.RandomNumber(TableInfo.min_ware,
				TableInfo.max_ware);
		para[3] = tenantid;
		try {
			// voltDBConn.createConnection(HConfig.VOLTDB_SERVER);
			response = voltDBConn.callProcedure(tableNameInVoltDB.toUpperCase()
					+ ".select", para);
			if (response.getStatus() == ClientResponse.SUCCESS) {
				res = response.getResults();
				return res;
			} else {
				return null;
			}
		} catch (NoConnectionsException e) {
			// TODO Auto-generated catch block
			System.out
					.println("--------No connections. Try to get a new connection-------");
			try {
				voltDBConn.createConnection(HConfig.VOLTDB_SERVER);
				response = voltDBConn.callProcedure(
						tableNameInVoltDB.toUpperCase() + ".select", para);
				if (response.getStatus() == ClientResponse.SUCCESS) {
					res = response.getResults();
					return res;
				} else {
					System.out.println(response.getStatusString() + "&"
							+ response.getStatus());
					System.exit(1);
					return null;
				}
			} catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (ProcCallException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProcCallException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private void setSelectSQLPreparedStmt(Connection conn) {
		if (conn == null)
			return;
		try {
			selectStmt = conn
					.prepareStatement(getSelectSQLPreparedStmtString());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void setUpdateSQLPreparedStmt(Connection conn) {
		if (conn == null)
			return;
		try {
			updateStmt = conn
					.prepareStatement(getUpdateSQLPreParedStmtString());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String getSelectSQLPreparedStmtString() {
		return "SELECT * FROM " + tableNameInMySQL
				+ " WHERE c_id = ? AND c_d_id = ? AND c_w_id = ?";
	}

	private String getUpdateSQLPreParedStmtString() {
		return "UPDATE "
				+ tableNameInMySQL
				+ " SET c_first = ?, c_middle = ?, c_last = ?, c_street_1 = ?, c_street_2 = ?,c_city = ?,"
				+ "c_state = ?,c_zip = ?, c_phone = ?,c_since = ?, c_credit = ?, c_credit_lim = ?, c_discount = ?, c_balance = ?, c_ytd_payment = ?,c_payment_cnt = ?, c_delivery_cnt = ?, c_data = ? "
				+ "WHERE c_id = ? AND c_d_id = ? AND c_w_id = ?";
	}

	private static String getSaveAllSQL(int tenantid) {
		return "select concat(c_id, ',', c_d_id, ',',c_w_id, ',', c_first, ',', c_middle, ',', c_last, ',', c_street_1, ',', c_street_2, ',',c_city, ',',c_state, ',',c_zip, ',', c_phone, ',',c_since, ',', c_credit, ',', c_credit_lim , ',', c_discount, ',', c_balance, ',', c_ytd_payment, ',',c_payment_cnt, ',', c_delivery_cnt, ',', c_data, ',','"
				+ tenantid
				+ "', ',', '0', ',', '0')"
				+ " from customer"
				+ (tenantid - 1)
				+ " into outfile '"
				+ HConfig.CSVPATH
				+ "/customer" + tenantid + ".csv'";
	}

	private static String getSavedFileName(int tenantid) {
		return "/customer" + tenantid + ".csv";
	}

	private static String getProcedureName(int voltDB_id) {
		return "OffloadCustomer_" + voltDB_id;
	}

	public static synchronized void deleteVoltDBID(int voltDB_id) {
		Client client = null;
		ClientResponse response = null;
		client = ClientFactory.createClient();
		try {
			client.createConnection(HConfig.VOLTDB_SERVER);
			try {
				response = client.callProcedure("@AdHoc", "delete from "
						+ TABLENAME + voltDB_id + ";");
			} catch (ProcCallException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (response.getStatus() != ClientResponse.SUCCESS) {
				System.out.println("Failed to delete customer table in "
						+ voltDB_id + "th slot");
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (client != null)
				try {
					client.close();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}

	// public static synchronized Thread getThreadForMoveToVoltDB(int tenantid,
	// int voltDB_id) {
	// Thread res;
	// res = new Thread(new LoadToVoltDB_no_use(TABLENAME,tenantid, voltDB_id,
	// getProcedureName(voltDB_id), getSavedFileName(tenantid),
	// getSaveAllSQL(tenantid)));
	// return res;
	// }
}
