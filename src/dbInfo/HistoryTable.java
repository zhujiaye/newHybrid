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

public class HistoryTable extends Table {
	final private static String TABLENAME = "history";
	final private static String PROCENAME = "History";
	private Random ran;

	private int tenantid;
	private String tableNameInMySQL;
	private PreparedStatement selectStmt = null;
	private PreparedStatement updateStmt = null;

	private int voltDB_id;
	private String tableNameInVoltDB;
	private Client voltDBConn;

	public HistoryTable(int tenantid) {
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

		Object[] para = new Object[3];
		int h_c_id, h_c_d_id, h_c_w_id;
		para[0] = h_c_id = Support.RandomNumber(1, TableInfo.CUST_PER_DIST);
		para[1] = h_c_d_id = Support.RandomNumber(1, TableInfo.DIST_PER_WARE);
		para[2] = h_c_w_id = Support.RandomNumber(TableInfo.min_ware,
				TableInfo.max_ware);
		try {
			for (int i = 0; i < 3; i++)
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

		int h_c_id, h_c_d_id, h_c_w_id;
		Object[] para = new Object[8];
		try {
			h_c_id = Support.RandomNumber(1, TableInfo.CUST_PER_DIST);
			h_c_d_id = Support.RandomNumber(1, TableInfo.DIST_PER_WARE);
			h_c_w_id = Support.RandomNumber(TableInfo.min_ware,
					TableInfo.max_ware);
			para[0] = h_c_d_id;
			para[1] = h_c_w_id;
			para[2] = Support.getTimeStamp();
			para[3] = 10.0;
			para[4] = Support.MakeAlphaString(12, 24);
			para[5] = h_c_id;
			para[6] = h_c_d_id;
			para[7] = h_c_w_id;
			for (int i = 0; i < 8; i++)
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
		Object[] para = new Object[15];
		int h_c_id, h_c_d_id, h_c_w_id;
		para[0] = h_c_id = Support.RandomNumber(1, TableInfo.CUST_PER_DIST);
		para[1] = h_c_d_id = Support.RandomNumber(1, TableInfo.DIST_PER_WARE);
		para[2] = h_c_w_id = Support.RandomNumber(TableInfo.min_ware,
				TableInfo.max_ware);
		para[3] = h_c_d_id;
		para[4] = h_c_w_id;
		para[5] = Support.getTimeStamp();
		para[6] = 10.0;
		para[7] = Support.MakeAlphaString(12, 24);
		para[8] = tenantid;
		para[9] = 0;
		para[10] = 1;
		para[11] = h_c_id;
		para[12] = h_c_d_id;
		para[13] = h_c_w_id;
		para[14] = tenantid;

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
		Object[] para = new Object[4];
		int h_c_id, h_c_d_id, h_c_w_id;
		para[0] = h_c_id = Support.RandomNumber(1, TableInfo.CUST_PER_DIST);
		para[1] = h_c_d_id = Support.RandomNumber(1, TableInfo.DIST_PER_WARE);
		para[2] = h_c_w_id = Support.RandomNumber(TableInfo.min_ware,
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
				+ " WHERE h_c_id = ? AND h_c_d_id = ? AND h_c_w_id = ?";
	}

	private String getUpdateSQLPreParedStmtString() {
		return "UPDATE "
				+ tableNameInMySQL
				+ " SET h_d_id = ?,h_w_id = ?,h_date = ?,h_amount = ?,h_data = ? WHERE h_c_id = ? AND h_c_d_id = ? AND h_c_w_id = ?";

	}

	private static String getSaveAllSQL(int tenantid) {
		return "select concat(h_c_id , ',', h_c_d_id, ',', h_c_w_id, ',',h_d_id, ',',h_w_id, ',',h_date, ',',h_amount, ',',h_data, ',', '"
				+ tenantid
				+ "', ',', '0', ',', '0' ) "
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
