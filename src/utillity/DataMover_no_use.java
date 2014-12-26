package utillity;
//package utillity;
//
//import java.io.IOException;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.SQLTimeoutException;
//import java.sql.Statement;
//
//import org.voltdb.client.Client;
//import org.voltdb.client.ClientFactory;
//
//import config.HConfig;
//import dbInfo.CustomerTable;
//import dbInfo.DistrictTable;
//import dbInfo.HistoryTable;
//import dbInfo.ItemTable;
//import dbInfo.NewOrdersTable;
//import dbInfo.OrderLineTable;
//import dbInfo.OrdersTable;
//import dbInfo.StockTable;
//import dbInfo.WarehouseTable;
//import server.Sender;
//import server.Tenant;
//
//public class DataMover implements Runnable {
//	private int tenantid;
//	private int voltDB_id;
//	private Sender sender;
//	private boolean mySQL2VoltDB;
//
//	public DataMover(int tenantid, int voltDB_id, Sender sender,
//			boolean mySQL2VoltDB) {
//		this.tenantid = tenantid;
//		this.voltDB_id = voltDB_id;
//		this.sender = sender;
//		this.mySQL2VoltDB = mySQL2VoltDB;
//	}
//
//	@Override
//	public void run() {
//		// TODO Auto-generated method stub
//		long t1, t2;
//		t1 = System.currentTimeMillis();
//		if (mySQL2VoltDB) {
//			// System.out.println("Moving Data to VoltDB for tenant "
//			// + tenant.getID());
//			Thread[] threads = new Thread[HConfig.NUMBER_OF_TABLES];
//			threads[0] = CustomerTable.getThreadForMoveToVoltDB(tenantid,
//					voltDB_id);
//			threads[1] = DistrictTable.getThreadForMoveToVoltDB(tenantid,
//					voltDB_id);
//			threads[2] = HistoryTable.getThreadForMoveToVoltDB(tenantid,
//					voltDB_id);
//			threads[3] = ItemTable
//					.getThreadForMoveToVoltDB(tenantid, voltDB_id);
//			threads[4] = NewOrdersTable.getThreadForMoveToVoltDB(tenantid,
//					voltDB_id);
//			threads[5] = OrderLineTable.getThreadForMoveToVoltDB(tenantid,
//					voltDB_id);
//			threads[6] = OrdersTable.getThreadForMoveToVoltDB(tenantid,
//					voltDB_id);
//			threads[7] = StockTable.getThreadForMoveToVoltDB(tenantid,
//					voltDB_id);
//			threads[8] = WarehouseTable.getThreadForMoveToVoltDB(tenantid,
//					voltDB_id);
//			try {
//				int pre = -1;
//				for (int i = 0; i < HConfig.NUMBER_OF_TABLES; i++) {
//					if (i - pre >= HConfig.TABLE_CONCURRENCY
//							|| i == HConfig.NUMBER_OF_TABLES - 1) {
//						for (int j = pre + 1; j <= i; j++) {
//							threads[j].start();
//						}
//						for (int j = pre + 1; j <= i; j++) {
//							threads[j].join();
//						}
//						pre = i;
//					}
//				}
//
//				// for (int i = 0; i < HConfig.NUMBER_OF_TABLES; i++) {
//				// threads[i].start();
//				// threads[i].join();
//				// }
//				// for (int i = 0; i < HConfig.NUMBER_OF_TABLES; i++)
//				// threads[i].join();
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			t2 = System.currentTimeMillis();
//			System.out.println("----------Tenant " + tenantid
//					+ " movement finished in "
//					+ ((double) (t2 - t1) / (60.0 * 1000.0))
//					+ " minutes--------");
//			//if (sender != null)
//			//	sender.sendMoveToVoltDBMessage(tenantid, voltDB_id);
//		} else {
//
//		}
//	}
// }
