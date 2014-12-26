//package utillity;
//
//import java.io.BufferedReader;
//import java.io.FileReader;
//import java.io.IOException;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.SQLTimeoutException;
//import java.sql.Statement;
//
//import org.voltdb.VoltTable;
//import org.voltdb.client.Client;
//import org.voltdb.client.ClientFactory;
//import org.voltdb.client.ClientResponse;
//import org.voltdb.client.NoConnectionsException;
//import org.voltdb.client.ProcCallException;
//import org.voltdb.client.ProcedureCallback;
//
//import config.HConfig;
//
//public class LoadToVoltDB_no_use implements Runnable {
//	private int tenantid;
//	private int voltDB_id;
//	private String procedureName;
//	private String savedFileName;
//	private String saveSQLString;
//	private String tableName;
//	private Connection mySQLConn;
//	private Client voltDBConn;
//
//	// private int tot_inserts = 0;
//	// private boolean waiting = false;
//
//	public LoadToVoltDB_no_use(String tableName, int tenantid, int voltDB_id,
//			String procedureName, String savedFileName, String saveSQLString) {
//		this.tenantid = tenantid;
//		this.voltDB_id = voltDB_id;
//		this.procedureName = procedureName;
//		this.savedFileName = savedFileName;
//		this.saveSQLString = saveSQLString;
//		this.tableName = tableName;
//	}
//
//	private void openMySQLConnection() {
//		try {
//			if (mySQLConn != null && !mySQLConn.isClosed()) {
//				return;
//			}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			// e.printStackTrace();
//			System.out.println(e.getMessage());
//		}
//		try {
//			try {
//				Class.forName("com.mysql.jdbc.Driver").newInstance();
//			} catch (InstantiationException e) {
//				// TODO Auto-generated catch block
//				// e.printStackTrace();
//				System.out.println(e.getMessage());
//			} catch (IllegalAccessException e) {
//				// TODO Auto-generated catch block
//				// e.printStackTrace();
//				System.out.println(e.getMessage());
//			} catch (ClassNotFoundException e) {
//				// TODO Auto-generated catch block
//				// e.printStackTrace();
//				System.out.println(e.getMessage());
//			}
//			while (mySQLConn == null) {
//				mySQLConn = DriverManager.getConnection(HConfig.urlOfMySQL,
//						HConfig.userOfMySQL, HConfig.passwordOfMySQL);
//			}
//		} catch (SQLTimeoutException e) {
//			System.out
//					.println("Can't get a connection!!!!!!!!!-----------------------");
//			System.out.println(e.getMessage());
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			// e.printStackTrace();
//			System.out.println(e.getMessage());
//		}
//	}
//
//	private void closeMySQLConnection() {
//		try {
//			if (mySQLConn == null || mySQLConn.isClosed())
//				return;
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			// e1.printStackTrace();
//			System.out.println(e.getMessage());
//		}
//		try {
//			// mySQLConn.commit();
//			mySQLConn.close();
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			// e.printStackTrace();
//			System.out.println(e.getMessage());
//		} finally {
//			mySQLConn = null;
//		}
//	}
//
//	private void openVoltDBConnection() {
//		if (voltDBConn != null)
//			return;
//		voltDBConn = ClientFactory.createClient();
//		try {
//			voltDBConn.createConnection(HConfig.VOLTDB_SERVER);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//
//	private void closeVoltDBConnection() {
//		if (voltDBConn == null)
//			return;
//		try {
//			// voltDBConn.drain();
//			voltDBConn.close();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} finally {
//			voltDBConn = null;
//		}
//	}
//
//	// public synchronized int getTotInserts() {
//	// return tot_inserts;
//	// }
//	//
//	// private synchronized void addOneInsert() {
//	// tot_inserts++;
//	// }
//	//
//	// public synchronized void finishOneInsert() {
//	// tot_inserts--;
//	// }
//	//
//	// public synchronized boolean isWaiting() {
//	// return waiting;
//	// }
//	//
//	// private synchronized void setWaiting() {
//	// waiting = true;
//	// }
//
//	@Override
//	public void run() {
//		// TODO Auto-generated method stub
//		// long t1, t2;
//		// t1 = System.currentTimeMillis();
//		openMySQLConnection();
//		openVoltDBConnection();
//		Statement stmt = null;
//		BufferedReader breader = null;
//		FileReader reader = null;
//		// VoltTable[] table = null;
//		ClientResponse response = null;
//		try {
//			stmt = mySQLConn.createStatement();
//			stmt.execute(saveSQLString);
//			reader = new FileReader(HConfig.CSVPATH + "/" + savedFileName);
//			breader = new BufferedReader(new FileReader(HConfig.CSVPATH + "/"
//					+ savedFileName));
//			String[] lines = new String[HConfig.BATCH];
//			String str;
//			int count = 0;
//			// LoadToVoltDBListener listener = new LoadToVoltDBListener(this);
//			while ((str = breader.readLine()) != null) {
//				lines[count] = str;
//				count++;
//				if (count == HConfig.BATCH) {
//					// addOneInsert();
//					// voltDBConn.callProcedure((ProcedureCallback) null,
//					// procedureName, tenantid, lines, count);
////					response = voltDBConn.callProcedure(procedureName,
////							tenantid, lines, count);
////					response.getStatus();
////					response.getResults();
//					count = 0;
//				}
//			}
//			if (count > 0) {
//				// addOneInsert();
//				// voltDBConn.callProcedure((ProcedureCallback) null,
//				// procedureName, tenantid, lines, count);
//				// response = voltDBConn.callProcedure(procedureName, tenantid,
//				// lines, count);
//				// response.getStatus();
//				// response.getResults();
//			}
//			// synchronized (this) {
//			// while (getTotInserts() != 0)
//			// try {
//			// setWaiting();
//			// this.wait();
//			// } catch (InterruptedException e) {
//			// // TODO Auto-generated catch block
//			// e.printStackTrace();
//			// }
//			// }
//
//			// Process p = Runtime.getRuntime().exec(
//			// "csvloader " + tableName + voltDB_id + " -f "
//			// + HConfig.CSVPATH + "/" + savedFileName);
//			// try {
//			// p.waitFor();
//			// } catch (InterruptedException e) {
//			// // TODO Auto-generated catch block
//			// e.printStackTrace();
//			// }
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			System.exit(1);
//		} catch (IOException e) {
//			e.printStackTrace();
//			System.exit(1);
//			// } catch (ProcCallException e) {
//			// // TODO Auto-generated catch block
//			// e.printStackTrace();
//		} finally {
//			if (reader != null)
//				try {
//					reader.close();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			if (breader != null) {
//				try {
//					breader.close();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		}
//		closeMySQLConnection();
//		closeVoltDBConnection();
//		// t2 = System.currentTimeMillis();
//		// System.out.println("Move for tenant " + tenantid + " finished in "
//		// + (((double) t2 - t1) / (60.0 * 1000.0)) + " minutes");
//		// System.out.println(procedureName
//		// + " have finished moving task to voltDB. Tenant id:" + tenantid
//		// + " VoltDB_id:" + voltDB_id);
//	}
//}
//
//// class LoadToVoltDBListener implements ProcedureCallback {
//// private LoadToVoltDB loadClass;
////
//// public LoadToVoltDBListener(LoadToVoltDB loadClass) {
//// this.loadClass = loadClass;
//// }
////
//// @Override
//// public void clientCallback(ClientResponse res) throws Exception {
//// // TODO Auto-generated method stub
//// if (res.getStatus() == ClientResponse.SUCCESS) {
//// loadClass.finishOneInsert();
//// if (loadClass.isWaiting() && loadClass.getTotInserts() == 0) {
//// synchronized (loadClass) {
//// loadClass.notify();
//// }
//// }
//// } else {
//// System.out.println("Fail!");
//// System.exit(1);
//// }
//// }
// // }