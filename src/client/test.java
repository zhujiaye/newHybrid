package client;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

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
			if (args[0].equals("--burst")) {
				int id = Integer.valueOf(args[1]);
				Tenant tmp = new Tenant(id, null);
				System.out.println("Actual Workload in burst interval:");
				System.out.println(tmp.getActualWorkloadInBurst());
				System.out.println("Estimated Workload in burst interval:");
				System.out.println(tmp.getEstimatedWorkLoadInBurst());
			}
			if (args[0].equals("--testmodel")) {
				tenants = new Tenant[HConfig.TOTTENANTS];
				fastReadTenants();
				// for (int i = 0; i < HConfig.TOTTENANTS; i++) {
				// tenants[i] = new Tenant(i + 1, null);
				// if (i % 300 == 0)
				// System.out.println(i + " tenants ready!");
				// }
				int tot = 0;
				for (int i = 0; i < HConfig.TOTTENANTS; i++)
					tot += tenants[i].getActualWorkloadInBurst();
				System.out.println("tot workload in burst:" + tot);
				HConfig.ISDETERMINED = true;
				getLoadsToVoltDB();
				HConfig.ISDETERMINED = false;
				getLoadsToVoltDB();
			}
		}
	}

	private static void fastReadTenants() {
		for (int i = 0; i < HConfig.TOTTENANTS; i++)
			tenants[i] = new Tenant(i + 1);
		Scanner reader = null;
		try {
			reader = new Scanner(new File(HConfig.INFO_FILE));
			while (reader.hasNextLine()) {
				String str = reader.nextLine();
				String[] strs = str.split(" ");
				int id;
				id = Integer.valueOf(strs[0]);
				tenants[id - 1].setSLO(Integer.valueOf(strs[1]));
				tenants[id - 1].setDataSize(Integer.valueOf(strs[2]));
				tenants[id - 1].setWriteHeavy(Integer.valueOf(strs[3]));
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println(HConfig.INFO_FILE
					+ " does not exist!Please call Procedure.main first!");
		} finally {
			if (reader != null)
				reader.close();
		}
		try {
			reader = new Scanner(new File(HConfig.WL_FILE));
			int c = 0;
			// active information
			while (reader.hasNextLine()) {
				String str = reader.nextLine();
				String[] strs = str.split(" ");
				c++;
				int interval = Integer.valueOf(strs[0]);
				if (interval != c) {
					System.out.println("wrong workload information WTF!!!!!!!");
					System.exit(1);
				}
				for (int i = 1; i < strs.length; i++) {
					int tmpId = Integer.valueOf(strs[i]);
					int id = tmpId + 1;
					tenants[id - 1].setActive(interval, true);
				}
				if (c == HConfig.NUMBER_OF_INTERVAL - 1)
					break;
			}
			for (int i = 0; i < HConfig.TOTTENANTS; i++)
				tenants[i].setActive(1, tenants[i].getActive(2));
			// workload information
			c = 6;
			while (reader.hasNextLine()) {
				String str = reader.nextLine();
				String[] strs = str.split(" ");
				for (int id = 0; id < HConfig.TOTTENANTS; id++)
					tenants[id].setWorkload(c, Integer.valueOf(strs[id + 1]));
				c++;
			}
			if (c != HConfig.NUMBER_OF_INTERVAL
					* HConfig.NUMBER_OF_SPLITS_IN_INTERVAL + 1) {
				System.out.println("wrong workload information WTF!!!!!!!");
				System.exit(1);
			}
			c = 5;
			for (; c > 0;) {
				for (int id = 0; id < HConfig.TOTTENANTS; id++)
					tenants[id].setWorkload(
							c,
							tenants[id].getWorkload(c
									+ HConfig.NUMBER_OF_SPLITS_IN_INTERVAL));
				c--;
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println(HConfig.WL_FILE
					+ " does not exist!Please call Procedure.main first!");
		} finally {
			if (reader != null)
				reader.close();
		}
	}

	private static void getLoadsToVoltDB() {
		int remainVoltDBSize = HConfig.VOLTDB_SIZE;
		ArrayList<Integer> A = new ArrayList<>();
		ArrayList<Integer> B = new ArrayList<>();
		ArrayList<Integer> ID = new ArrayList<>();
		for (int i = 0; i < HConfig.TOTTENANTS; i++) {
			if (!tenants[i].isActiveInBurst())
				continue;
			A.add(tenants[i].getEstimatedDataSizeInVoltDB());
			if (HConfig.ISDETERMINED)
				B.add(tenants[i].getActualWorkloadInBurst());
			else
				B.add(tenants[i].getEstimatedWorkLoadInBurst());
			ID.add(tenants[i].getID());
		}
		int n = A.size();
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
			int index = n - 1, capacity = remainVoltDBSize;
			if (HConfig.ISDETERMINED)
				System.out.println("Determined!");
			else
				System.out.println("Undetermined!");
			System.out.println("Move workload about:" + f[n - 1][capacity]);

			int actual = 0;
			initializeToVoltDBInfoList();
			cnt = 0;
			while (index >= 0) {
				if (pre[index][capacity] == 1) {
					createToVoltDBInfoForTenant(ID.get(index));
					actual += tenants[ID.get(index) - 1]
							.getActualWorkloadInBurst();
					capacity -= A.get(index);
					index--;
				} else
					index--;
			}
			System.out.println("Actual workload moved:" + actual);
			// System.out.println("Remain voltDBSize before movement:" +
			// remainVoltDBSize);
			// System.out.println("Remain voltDBSize after movement:" + now);

		}
	}

	private static void move() {
		for (int i = 0; i < HConfig.TOTTENANTS; i++) {
			tenants[i] = new Tenant(i + 1, null);
		}
		getLoadsToVoltDB();
		if (toVoltDBInfos.size() > 0) {
			new Thread(new Mover(toVoltDBInfos, true)).start();
		}
	}

	private static void initializeToVoltDBInfoList() {
		if (toVoltDBInfos != null)
			toVoltDBInfos.clear();
		toVoltDBInfos = new ArrayList<>();
	}

	private static void createToVoltDBInfoForTenant(int id) {
		int voltDB_id = cnt % 50;
		cnt++;
		// System.out
		// .format("Move tenant to voltDB id(%d):id(%d),SLO(%d),dataSize(%d),writeHeavy(%d),actualworkload(%d)%n",
		// voltDB_id, tenants[id - 1].getID(),
		// tenants[id - 1].getSLO(),
		// tenants[id - 1].getDataSize(),
		// tenants[id - 1].getWriteHeavy(),
		// tenants[id - 1].getActualWorkloadInBurst());
		toVoltDBInfos.add(new TenantToVoltDBInfo(id, voltDB_id, null));
	}
}
