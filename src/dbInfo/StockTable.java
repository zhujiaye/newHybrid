package dbInfo;

import java.io.IOException;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.util.Random;

import org.voltdb.VoltTable;
import org.voltdb.client.Client;
import org.voltdb.client.ClientFactory;
import org.voltdb.client.ClientResponse;
import org.voltdb.client.NoConnectionsException;
import org.voltdb.client.ProcCallException;

import utillity.Support;
import config.HConfig;

public class StockTable extends Table {
	final private static String TABLENAME = "stock";
	final private static String PROCENAME = "Stock";
	private Random ran;

	private int tenantid;
	private String tableNameInMySQL;
	private PreparedStatement selectStmt = null;
	private PreparedStatement updateStmt = null;

	private int voltDB_id;
	private String tableNameInVoltDB;
	private Client voltDBConn;

	public StockTable(int tenantid) {
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

		Object[] para = new Object[2];
		int s_i_id, s_w_id;
		s_i_id = Support.RandomNumber(1, TableInfo.MAXITEMS);
		s_w_id = Support.RandomNumber(TableInfo.min_ware, TableInfo.max_ware);
		para[0] = s_w_id;
		para[1] = s_i_id;
		try {
			for (int i = 0; i < 2; i++)
				selectStmt.setObject(i + 1, para[i]);
			rs = selectStmt.executeQuery();
		} catch (SQLTimeoutException e) {
			System.out.println("Tenant " + tenantid + ": select time out!");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}

	public int doUpdateInMySQL() {
		int res;

		if (updateStmt == null) {
			System.out.println("Error:can not do update SQL statements");
			return -1;
		}

		int s_i_id, s_w_id;
		Object[] para = new Object[17];
		try {
			s_i_id = Support.RandomNumber(1, TableInfo.MAXITEMS);
			s_w_id = Support.RandomNumber(TableInfo.min_ware,
					TableInfo.max_ware);
			para[0] = Support.RandomNumber(10, 100);
			para[1] = Support.MakeAlphaString(24, 24);
			para[2] = Support.MakeAlphaString(24, 24);
			para[3] = Support.MakeAlphaString(24, 24);
			para[4] = Support.MakeAlphaString(24, 24);
			para[5] = Support.MakeAlphaString(24, 24);
			para[6] = Support.MakeAlphaString(24, 24);
			para[7] = Support.MakeAlphaString(24, 24);
			para[8] = Support.MakeAlphaString(24, 24);
			para[9] = Support.MakeAlphaString(24, 24);
			para[10] = Support.MakeAlphaString(24, 24);
			para[11] = 0;
			para[12] = 0;
			para[13] = 0;
			para[14] = Support.MakeAlphaString(26, 50);
			para[15] = s_w_id;
			para[16] = s_i_id;
			for (int i = 0; i < 17; i++)
				updateStmt.setObject(i + 1, para[i]);
			res = updateStmt.executeUpdate();
		} catch (SQLTimeoutException e) {
			System.out.println("Tenant " + tenantid + ": update time out!");
			return -1;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
		return res;
	}

	public int doUpdateInVoltDB() {
		if (voltDBConn == null)
			return -1;
		ClientResponse res = null;
		Object[] para = new Object[23];
		int s_i_id, s_w_id;
		para[0] = s_i_id = Support.RandomNumber(1, TableInfo.MAXITEMS);
		para[1] = s_w_id = Support.RandomNumber(TableInfo.min_ware,
				TableInfo.max_ware);
		para[2] = Support.RandomNumber(10, 100);
		para[3] = Support.MakeAlphaString(24, 24);
		para[4] = Support.MakeAlphaString(24, 24);
		para[5] = Support.MakeAlphaString(24, 24);
		para[6] = Support.MakeAlphaString(24, 24);
		para[7] = Support.MakeAlphaString(24, 24);
		para[8] = Support.MakeAlphaString(24, 24);
		para[9] = Support.MakeAlphaString(24, 24);
		para[10] = Support.MakeAlphaString(24, 24);
		para[11] = Support.MakeAlphaString(24, 24);
		para[12] = Support.MakeAlphaString(24, 24);
		para[13] = 0;
		para[14] = 0;
		para[15] = 0;
		para[16] = Support.MakeAlphaString(26, 50);
		para[17] = tenantid;
		para[18] = 0;
		para[19] = 1;
		para[20] = s_w_id;
		para[21] = s_i_id;
		para[22] = tenantid;

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
				} else
					return -1;
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
		Object[] para = new Object[3];
		int s_i_id, s_w_id;
		s_i_id = Support.RandomNumber(1, TableInfo.MAXITEMS);
		s_w_id = Support.RandomNumber(TableInfo.min_ware, TableInfo.max_ware);
		para[0] = s_w_id;
		para[1] = s_i_id;
		para[2] = tenantid;
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
				+ " WHERE s_w_id = ? AND s_i_id = ?";
	}

	private String getUpdateSQLPreParedStmtString() {
		return "UPDATE "
				+ tableNameInMySQL
				+ " SET s_quantity = ?, s_dist_01 = ?, s_dist_02 = ?,s_dist_03 = ?,s_dist_04 = ?, s_dist_05 = ?, s_dist_06 = ?, s_dist_07 = ?, s_dist_08 = ?, s_dist_09 = ?, s_dist_10 = ?, s_ytd = ?, s_order_cnt = ?, s_remote_cnt = ?,s_data = ? WHERE s_w_id = ? AND s_i_id = ?";

	}

	private static String getSaveAllSQL(int tenantid) {
		return "select concat(s_i_id, ',', s_w_id, ',', s_quantity, ',', s_dist_01, ',', s_dist_02, ',',s_dist_03, ',',s_dist_04, ',', s_dist_05, ',', s_dist_06, ',', s_dist_07, ',', s_dist_08, ',', s_dist_09, ',', s_dist_10, ',', s_ytd, ',', s_order_cnt, ',', s_remote_cnt, ',',s_data, ',', '"
				+ tenantid
				+ "', ',', '0', ',', '0') "
				+ "from "
				+ TABLENAME
				+ tenantid
				+ " into outfile '"
				+ HConfig.CSVPATH
				+ "/"
				+ TABLENAME + tenantid + ".csv'";

	}

	private static String getSavedFileName(int tenantid) {
		return "/" + TABLENAME + tenantid + ".csv";
	}

	private static String getProcedureName(int voltDB_id) {
		return "Offload" + PROCENAME + "_" + voltDB_id;
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
				System.out.println("Failed to delete " + TABLENAME
						+ " table in " + voltDB_id + "th slot");
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
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
