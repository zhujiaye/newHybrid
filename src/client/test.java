package client;

import java.util.ArrayList;

import org.voltdb.messaging.VoltDbMessageFactory;

import config.HConfig;
import dbInfo.CustomerTable;
import dbInfo.DistrictTable;
import dbInfo.HistoryTable;
import dbInfo.ItemTable;
import dbInfo.NewOrdersTable;
import dbInfo.OrderLineTable;
import dbInfo.OrdersTable;
import dbInfo.StockTable;
import dbInfo.WarehouseTable;
import server.Tenant;
import server.TenantToVoltDBInfo;
import utillity.Mover;
import utillity.MySQLConnectionPool;
import utillity.PossionDistribution;
import utillity.VoltDBConnectionPool;

public class test {
	private static int cnt = 0;
	private static Tenant[] tenants = new Tenant[HConfig.TOTTENANTS];
	private static ArrayList<TenantToVoltDBInfo> toVoltDBInfos = new ArrayList<>();

	public static void main(String[] args) {
		HConfig.load();
		HConfig.print();
		if (args.length > 0) {
			if (args[0].equals("-m")) {
				// for (int i = 0; i < 50; i++) {
				// CustomerTable.deleteVoltDBID(i);
				// DistrictTable.deleteVoltDBID(i);
				// HistoryTable.deleteVoltDBID(i);
				// ItemTable.deleteVoltDBID(i);
				// NewOrdersTable.deleteVoltDBID(i);
				// OrderLineTable.deleteVoltDBID(i);
				// OrdersTable.deleteVoltDBID(i);
				// StockTable.deleteVoltDBID(i);
				// WarehouseTable.deleteVoltDBID(i);
				// }
				// ArrayList<TenantToVoltDBInfo> toVoltDBInfos = new
				// ArrayList<>();
				// int cnt = 0;
				// for (int i = 0; i < HConfig.TOTTENANTS; i++) {
				// Tenant tmp = new Tenant(i + 1, null);
				// if (tmp.getDataSize() == 7 || tmp.getDataSize() == 16) {
				// toVoltDBInfos
				// .add(new TenantToVoltDBInfo(i + 1, cnt % 50, null));
				// cnt++;
				// if (cnt > 100)
				// break;
				// }
				// }
				// new Thread(new Mover(toVoltDBInfos, true)).start();
				// HTenant[] list = new HTenant[HConfig.TOTTENANTS];
				// for (int i = 0; i < HConfig.TOTTENANTS; i++) {
				// list[i] = new HTenant(i + 1, null, null, null);
				// }
				// int tmp = 0;
				// for (int i = 1; i <= HConfig.NUMBER_OF_INTERVAL; i++) {
				// tmp = 0;
				// for (int j = 0; j < HConfig.TOTTENANTS; j++)
				// if (list[j].isActiveInInterval(i))
				// tmp++;
				// System.out.println(tmp);
				// }
				move();
			}
			if (args[0].equals("-cV")) {
				new VoltDBConnectionPool(100, true);

			}
			if (args[0].equals("-cM")) {
				new MySQLConnectionPool(100, true);
			}
			if (args[0].equals("-c")) {
				new MySQLConnectionPool(100, true);
				new VoltDBConnectionPool(100, true);
			}
			if (args[0].equals("-t")) {
				HTenant[] list = new HTenant[HConfig.TOTTENANTS];
				for (int i = 0; i < HConfig.TOTTENANTS; i++) {
					list[i] = new HTenant(i + 1, null, null, null);
				}
				int tmp = 0;
				for (int i = 1; i <= HConfig.NUMBER_OF_INTERVAL; i++) {
					tmp = 0;
					for (int j = 0; j < HConfig.TOTTENANTS; j++)
						if (list[j].isActiveInInterval(i))
							tmp++;
					System.out.println(tmp);
				}
			}
		}
	}

	private static void move() {
		int remainVoltDBSize = HConfig.VOLTDB_SIZE;

		for (int i = 0; i < HConfig.TOTTENANTS; i++) {
			tenants[i] = new Tenant(i + 1, null);
			
		}
		ArrayList<Integer> A = new ArrayList<>();
		ArrayList<Integer> B = new ArrayList<>();
		ArrayList<Integer> ID = new ArrayList<>();
		for (int i = 0; i < HConfig.TOTTENANTS; i++) {
			A.add(tenants[i].getEstimatedDataSizeInVoltDB());
			B.add(tenants[i].getEstimatedWorkLoadInBurst());
			ID.add(tenants[i].getID());
		}
		int n = A.size();
		// for (int i = 0; i < n; i++) {
		// System.out.format("W(%d) V(%d) ID(%d)%n", A.get(i), B.get(i),
		// ID.get(i));
		// }
		if (n > 0) {
			int f[][] = new int[n][remainVoltDBSize + 1];
			int pre[][] = new int[n][remainVoltDBSize + 1];
			for (int i = 0; i < n; i++)
				for (int j = 0; j <= remainVoltDBSize; j++) {
					f[i][j] = 0;
					pre[i][j] = 0;
				}
			for (int i = A.get(0); i <= remainVoltDBSize; i++) {
				f[0][i] = B.get(0);
				pre[0][i] = 1;
			}
			for (int i = 1; i < n; i++)
				for (int j = 0; j <= remainVoltDBSize; j++) {
					f[i][j] = f[i - 1][j];
					pre[i][j] = 0;
					int u = A.get(i), v = B.get(i);
					if (j >= u && f[i - 1][j - u] + v > f[i][j]) {
						f[i][j] = f[i - 1][j - u] + v;
						pre[i][j] = 1;
					}
				}
			int nowid = n - 1, now = remainVoltDBSize;
			System.out.println("Move workload about:" + f[nowid - 1][now]);
			System.out.println("Remain voltDBSize before movement:" + now);
			// if (threads != null)
			// threads.clear();
			// threads = new ArrayList<>();
			toVoltDBInfos.clear();
			toVoltDBInfos = new ArrayList<>();
			cnt = 0;
			while (nowid >= 0) {
				if (pre[nowid][now] == 1) {
					// System.out.println("Move Tenant:"+ID.get(nowid));
					moveToVoltDB(ID.get(nowid));
					now -= A.get(nowid);
					nowid--;
				} else
					nowid--;
			}
			System.out.println("Remain voltDBSize after movement:" + now);
			if (toVoltDBInfos.size() > 0) {
				new Thread(new Mover(toVoltDBInfos, true)).start();
			}
		}
	}

	private static void moveToVoltDB(int id) {
		int voltDB_id = cnt % 50;
		cnt++;
		System.out
				.format("Move tenant to voltDB id(%d):id(%d),SLO(%d),dataSize(%d),writeHeavy(%d)%n",
						voltDB_id, tenants[id - 1].getID(),
						tenants[id - 1].getSLO(),
						tenants[id - 1].getDataSize(),
						tenants[id - 1].getWriteHeavy());
		toVoltDBInfos.add(new TenantToVoltDBInfo(id, voltDB_id, null));
	}
}
