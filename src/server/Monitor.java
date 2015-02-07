package server;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import utillity.HTenantDynamicInfo;
import utillity.HTimer;
import utillity.MemMonitor;
import utillity.Mover;
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

public class Monitor implements Runnable {
	private boolean started;
	private int readyTenants = 0;

	private HashMap<Receiver, Sender> map;
	private Tenant[] tenants;

	private ArrayList<Sender> senderList;

	private boolean finished;
	private int finishedTenants = 0;

	private VoltDBMonitor voltDBMonitor;

	// private ArrayList<Thread> threads;
	private ArrayList<TenantToVoltDBInfo> toVoltDBInfos;

	public Monitor() {
		voltDBMonitor = new VoltDBMonitor(HConfig.NUMBER_OF_ID_IN_VOLTDB);
		voltDBMonitor.deleteALLTable();
		map = new HashMap<>();
		senderList = new ArrayList();
		tenants = new Tenant[HConfig.TOTTENANTS];
		for (int i = 0; i < HConfig.TOTTENANTS; i++)
			tenants[i] = null;
		readyTenants = 0;
		setStarted(false);
		finishedTenants = 0;
		setFinished(false);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("Monitoring.........");

		// synchronized (this) {
		// while (!isStarted()) {
		// try {
		// System.out.println("wait to start....");
		// this.wait();
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }
		// }
		while (!isStarted())
			;
		System.out.println("Start!");
		new Thread(new HTimer(this, HConfig.SERVER_TIME_GAP)).start();
		// new MemMonitor(HConfig.VOLTDB_SERVER).start();
		long startTime = System.currentTimeMillis();
		// int estimatedInterval = 0;
		int actualInterval = 0;
		while (true) {
			synchronized (this) {
				System.out.println("monitor waiting...");
				System.out
						.println(((double) (System.currentTimeMillis() - startTime))
								/ (60.0 * 1000.0));
				try {
					this.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("monitor continue...");
				int count = 0;
				int countUsingMySQL = 0;
				int countUsingVoltDB = 0;
				int countUsingMySQLandActive = 0;
				int countUsingVoltDBandActive = 0;
				for (int i = 0; i < HConfig.TOTTENANTS; i++) {
					if (tenants[i].getInterval() > actualInterval)
						count++;
					if (tenants[i].isUsingVoltDB()) {
						countUsingVoltDB++;
						if (tenants[i].isActive())
							countUsingVoltDBandActive++;
					} else {
						countUsingMySQL++;
						if (tenants[i].isActive())
							countUsingMySQLandActive++;
					}
				}
				if (count == HConfig.TOTTENANTS) {
					actualInterval++;
					System.out.println("Enter the " + actualInterval
							+ "th interval");
					System.out.println("Number of Tenants using MySQL:"
							+ countUsingMySQL + " and active number:"
							+ countUsingMySQLandActive);
					System.out.println("Number of Tenants using VoltDB:"
							+ countUsingVoltDB + " and active number:"
							+ countUsingVoltDBandActive);
					// for (int i = 0; i < HConfig.TOTTENANTS; i++) {
					// if (tenants[i].isUsingVoltDB() && tenants[i].isActive())
					// {
					// System.out
					// .format("tenant using voltdb and active:id(%d),SLO(%d),dataSize(%d),writeHeavy(%d)%n",
					// tenants[i].getID(),
					// tenants[i].getSLO(),
					// tenants[i].getDataSize(),
					// tenants[i].getWriteHeavy());
					// }
					// }
					System.out
							.println(((double) (System.currentTimeMillis() - startTime))
									/ (60.0 * 1000.0));
					if (actualInterval > HConfig.NUMBER_OF_INTERVAL)
						break;
					if (actualInterval <= HConfig.NUMBER_OF_INTERVAL)
						findTenantsToMoveDB(actualInterval);
				}
			}
			// long tmp = System.currentTimeMillis();
			// if (tmp - startTime >= HConfig.INTERVAL_TIME * estimatedInterval)
			// {
			// estimatedInterval++;
			// if (estimatedInterval > HConfig.NUMBER_OF_INTERVAL)
			// break;
			// if (estimatedInterval < HConfig.NUMBER_OF_INTERVAL) {
			// findTenantsToMoveDB(estimatedInterval);
			// }
			// }
		}
		while (!isFinished()) {
		}
		;
		System.out.println("Stop!");
	}

	public boolean mapSocket(Receiver receiver, Sender sender) {
		if (isStarted())
			return false;
		map.put(receiver, sender);
		senderList.add(sender);
		return true;
	}

	public synchronized void updateInfoForTenant(int id, HTenantDynamicInfo info) {
		tenants[id - 1].setInterval(info.getInterval());
		tenants[id - 1].setUsingVoltDB(info.getUsingVoltDB());
		tenants[id - 1].setUseVoltDBID(info.getUseVoltDBID());
		tenants[id - 1].setIsActive(info.getIsActive());
		tenants[id - 1].setIsBurst(info.getIsBurst());
		if (HConfig.debug)
			System.out
					.format("Tenant %d: interval(%d) usingVoltDB(%b) useVoltDBID(%d) isActive(%b) isBurst(%b)%n",
							id, info.getInterval(), info.getUsingVoltDB(),
							info.getUseVoltDBID(), info.getIsActive(),
							info.getIsBurst());
	}

	public synchronized void getFinishedForTenant(int id) {
		finishedTenants++;
		if (finishedTenants % 200 == 0) {
			System.out.println("Finished " + finishedTenants + " tenants!");
		}
		tenants[id - 1].setInterval(tenants[id - 1].getInterval() + 1);
		if (finishedTenants == HConfig.TOTTENANTS) {
			for (int i = 0; i < senderList.size(); i++) {
				senderList.get(i).sendAllFinishedMessage();
			}
			setFinished(true);
			System.out.println("set finished!");
		}
	}

	public synchronized void getReadyForTenant(int id, Receiver receiver) {
		if (isStarted())
			return;
		if (tenants[id - 1] != null)
			return;
		// System.out.println("Tenant " + id + " ready!");
		Sender sender = map.get(receiver);
		tenants[id - 1] = new Tenant(id, sender);
		readyTenants++;
		if (readyTenants % 300 == 0) {
			System.out.println(readyTenants + " tenants ready.......");
		}
		sender.sendConfirmedMessage(id);
		if (readyTenants == HConfig.TOTTENANTS) {
			if (HConfig.VOLTDB_TEST) {
				initializeToVoltDBInfoList();
				Scanner reader = null;
				int totSize = 0;
				try {
					reader = new Scanner(new File(HConfig.WL_FILE));
					while (reader.hasNextLine()) {
						String str = reader.nextLine();
						String[] strs = str.split(" ");
						int interval = Integer.valueOf(strs[0]);
						if (interval != 1) {
							System.out
									.println("wrong workload information WTF!!!!!!!");
							System.exit(1);
						}
						for (int i = 1; i < strs.length; i++) {
							int tmpId = Integer.valueOf(strs[i]);
							createToVoltDBInfoForTenant(tmpId + 1);
							totSize += tenants[tmpId].getDataSize();
						}
						break;
					}
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("tot_voltdb_memory_need:" + totSize);
				if (totSize > HConfig.TOT_MEM) {
					System.out
							.println("@@@@@@@@@@@@Too much memory space to hold these tenants!!!@@@@@@@@@@@@@@");
					System.exit(1);
				} else {
					System.out
							.println("Memory space seems work for these tenants");
					System.out.println("Whether to go on?(Y/N)");
					Scanner in = new Scanner(System.in);
					String str = in.next();
					if (str.equals("N")) {
						in.close();
						System.exit(1);
					}
					in.close();
				}
				System.out
						.println("Moving data from MySQL to VoltDB first....");
				Thread t = new Thread(new Mover(toVoltDBInfos, true));
				t.start();
				try {
					t.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			for (int i = 0; i < senderList.size(); i++) {
				senderList.get(i).sendAllConfirmedMessage();
			}
			setStarted(true);
			// for (int i = 0; i < HConfig.TOTTENANTS; i++) {
			// sender = tenants[i].getSender();
			// sender.sendAllConfirmedMessage();
			// }

		}
	}

	private synchronized void setFinished(boolean boo) {
		finished = boo;
	}

	private synchronized boolean isFinished() {
		return finished;
	}

	private synchronized void setStarted(boolean boo) {
		started = boo;
	}

	private synchronized boolean isStarted() {
		return started;
	}

	private boolean isBurstInterval(int interval) {
		// return true;
		return HConfig.ISBURST[interval - 1];
	}

	private void findTenantsToMoveDB(int interval) {
		if (HConfig.VOLTDB_TEST)
			return;
		if (isBurstInterval(interval))
			return;
		if (!HConfig.MOVEDB)
			return;
		int remainVoltDBSize = HConfig.VOLTDB_SIZE;
		for (int i = 0; i < HConfig.TOTTENANTS; i++) {
			if (tenants[i].isUsingVoltDB())
				remainVoltDBSize -= tenants[i].getEstimatedDataSizeInVoltDB();
		}
		if (remainVoltDBSize < 0)
			remainVoltDBSize = 0;
		if (interval + 2 <= HConfig.NUMBER_OF_INTERVAL
				&& !isBurstInterval(interval + 1)
				&& isBurstInterval(interval + 2) && remainVoltDBSize > 0
				&& HConfig.USEVOLTDB) {
			ArrayList<Integer> A = new ArrayList<>();
			ArrayList<Integer> B = new ArrayList<>();
			ArrayList<Integer> ID = new ArrayList<>();
			for (int i = 0; i < HConfig.TOTTENANTS; i++)
				if (tenants[i].isActive() && !tenants[i].isUsingVoltDB()) {
					A.add(tenants[i].getEstimatedDataSizeInVoltDB());
					if (HConfig.ISDETERMINED)
						B.add(tenants[i].getActualWorkloadInBurst());
					else
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
				int index = n - 1, capacity = remainVoltDBSize;
				System.out.println("Move workload about:" + f[n - 1][capacity]);
				System.out.println("Remain voltDBSize before movement:" + capacity);
				// if (threads != null)
				// threads.clear();
				// threads = new ArrayList<>();
				initializeToVoltDBInfoList();
				while (index >= 0) {
					if (pre[index][capacity] == 1) {
						// System.out.println("Move Tenant:"+ID.get(nowid));
						createToVoltDBInfoForTenant(ID.get(index));
						capacity -= A.get(index);
						index--;
					} else
						index--;
				}
				System.out.println("Remain voltDBSize after movement:" + capacity);
				if (toVoltDBInfos.size() > 0) {
					// ArrayList<TenantToVoltDBInfo> tmplist = new
					// ArrayList<>();
					// int last = -1;
					// Thread t;
					// for (int i = 0; i < toVoltDBInfos.size(); i++) {
					// if (i - last >= HConfig.MOVER_CONCURRENCY
					// || i == toVoltDBInfos.size() - 1) {
					// tmplist.clear();
					// for (int j = last + 1; j <= i; j++)
					// tmplist.add(toVoltDBInfos.get(j));
					// last = i;
					// t = new Thread(new Mover(tmplist, true));
					// t.start();
					// try {
					// t.join();
					// } catch (InterruptedException e) {
					// // TODO Auto-generated catch block
					// e.printStackTrace();
					// }
					// }
					// }
					new Thread(new Mover(toVoltDBInfos, true)).start();
				}
				// long t1 = System.currentTimeMillis();
				// long t2;
				// new Thread(new Mover(threads)).start();
				// t2 = System.currentTimeMillis();
				// System.out.println("Move finished in " + ((double) (t2 - t1))
				// / (60.0 * 1000.0) + " minutes");
			}
		} else if (interval > 2 && !isBurstInterval(interval - 1)
				&& isBurstInterval(interval - 2)) {
			ArrayList<TenantToVoltDBInfo> list = voltDBMonitor.getAll();
			voltDBMonitor.removeAll();
			for (int i = 0; i < list.size(); i++) {
				list.get(i).setSender(
						tenants[list.get(i).getTenantID() - 1].getSender());
			}
			new Thread(new Mover(list, false)).start();
		}
	}

	private void initializeToVoltDBInfoList() {
		if (toVoltDBInfos != null)
			toVoltDBInfos.clear();
		toVoltDBInfos = new ArrayList<>();
	}

	private void createToVoltDBInfoForTenant(int id) {
		if (tenants[id - 1].isUsingVoltDB())
			return;
		int voltDB_id = voltDBMonitor.findIdForTenant(id);
		voltDBMonitor.addTenantForID(id, voltDB_id);
		System.out
				.format("Move tenant to voltDB id(%d):id(%d),SLO(%d),dataSize(%d),writeHeavy(%d)%n",
						voltDB_id, tenants[id - 1].getID(),
						tenants[id - 1].getSLO(),
						tenants[id - 1].getDataSize(),
						tenants[id - 1].getWriteHeavy());
		toVoltDBInfos.add(new TenantToVoltDBInfo(id, voltDB_id, tenants[id - 1]
				.getSender()));
		// threads.add(new Thread(new DataMover(tenants[id - 1], voltDB_id,
		// true)));
	}
}

class VoltDBMonitor {
	private int n;
	private ArrayList<Integer>[] tenantsInID;

	public VoltDBMonitor(int n) {
		this.n = n;
		tenantsInID = new ArrayList[n];
		for (int i = 0; i < n; i++)
			tenantsInID[i] = new ArrayList<Integer>();
	}

	public int findIdForTenant(int tenantid) {
		int best = -1, id = 0;
		for (int i = 0; i < n; i++) {
			if (best == -1 || tenantsInID[i].size() < best) {
				best = tenantsInID[i].size();
				id = i;
			}
		}
		return id;
	}

	public void addTenantForID(int tenantid, int id) {
		if (id < 0 || id >= n) {
			System.out.println("Error:index out of id in voltDB!");
		}
		tenantsInID[id].add(tenantid);
	}

	public void removeTenantForID(int tenantid, int id) {
		if (id < 0 || id >= n) {
			System.out.println("Error:index out of id in voltDB!");
		}
		if (!tenantsInID[id].contains(tenantid)) {
			System.out.println("Error:tenant " + tenantid + " not in ID " + id
					+ "th slot!");
		}
		tenantsInID[id].remove(tenantid);
	}

	public ArrayList<TenantToVoltDBInfo> getAll() {
		ArrayList<TenantToVoltDBInfo> list = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < tenantsInID[i].size(); j++) {
				int k = tenantsInID[i].get(j);
				list.add(new TenantToVoltDBInfo(k, i, null));
			}
		}
		return list;
	}

	public void removeAll() {
		tenantsInID = new ArrayList[n];
	}

	public void deleteALLTable() {
		if (!HConfig.USEVOLTDB)
			return;
		for (int i = 0; i < n; i++) {
			CustomerTable.deleteVoltDBID(i);
			DistrictTable.deleteVoltDBID(i);
			HistoryTable.deleteVoltDBID(i);
			ItemTable.deleteVoltDBID(i);
			NewOrdersTable.deleteVoltDBID(i);
			OrderLineTable.deleteVoltDBID(i);
			OrdersTable.deleteVoltDBID(i);
			StockTable.deleteVoltDBID(i);
			WarehouseTable.deleteVoltDBID(i);
		}
	}
}
