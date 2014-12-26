package utillity;

import java.util.ArrayList;

import retrivers.Retriver;
import server.TenantToVoltDBInfo;
import config.HConfig;
import dbInfo.CustomerTable;

public class Mover implements Runnable {
	private ArrayList<TenantToVoltDBInfo> toVoltDBInfos;
	private boolean toVoltDB;

	// private ArrayList<Thread> threads;
	// private ArrayList<Integer> tenantids;

	public Mover(ArrayList<TenantToVoltDBInfo> toVoltDBInfos, boolean toVoltDB) {
		this.toVoltDBInfos = toVoltDBInfos;
		this.toVoltDB = toVoltDB;
		// threads = new ArrayList<>();
		// tenantids = new ArrayList<>();
		// for (int i = 0; i < toVoltDBInfos.size(); i++) {
		// TenantToVoltDBInfo tmp = toVoltDBInfos.get(i);
		// threads.add(new Thread(new DataMover(tmp.getTenantID(), tmp
		// .getVoltDB_id(), tmp.getSender(), true)));
		// tenantids.add(tmp.getTenantID());
		// }
	}

	@Override
	public void run() {
		// int last = -1;
		// for (int i = 0; i < threads.size(); i++) {
		// if (i - last >= HConfig.MOVER_CONCURRENCY
		// || i == threads.size() - 1) {
		// for (int j = last + 1; j <= i; j++) {
		// threads.get(j).start();
		// }
		// for (int j = last + 1; j <= i; j++) {
		// try {
		// threads.get(j).join();
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }
		// last = i;
		// System.out.println("already finish movement for " + (i + 1)
		// + " tenants");
		// }
		// }
		// String[] tables_name = { "customer", "district", "history", "item",
		// "new_orders", "order_line", "orders", "stock", "warehouse" };
		long t1, t2;
		if (toVoltDB) {
			t1 = System.currentTimeMillis();
			String[] tables_name = { "stock", "customer", "new_orders", "item",
					"district", "history", "order_line", "orders", "warehouse" };
			Thread[] threads = new Thread[HConfig.NUMBER_OF_TABLES];
			ArrayList<TenantToVoltDBInfo> tmplist = new ArrayList<>();
			int last = -1;
			Thread t;
			for (int i = 0; i < toVoltDBInfos.size(); i++) {
				if (i - last >= HConfig.MOVER_CONCURRENCY
						|| i == toVoltDBInfos.size() - 1) {
					tmplist.clear();
					for (int j = last + 1; j <= i; j++)
						tmplist.add(toVoltDBInfos.get(j));
					last = i;
					for (int j = 0; j < HConfig.NUMBER_OF_TABLES; j++) {
						threads[j] = new Thread(new TableMover(tables_name[j],
								tmplist));
					}
					int lastT = -1;
					for (int j = 0; j < HConfig.NUMBER_OF_TABLES; j++) {
						if (j - lastT >= HConfig.TABLE_CONCURRENCY
								|| j == HConfig.NUMBER_OF_TABLES - 1) {
							for (int k = lastT + 1; k <= j; k++)
								threads[k].start();
							for (int k = lastT + 1; k <= j; k++)
								try {
									threads[k].join();
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							lastT = j;
						}
						// try {
						// threads[j].join();
						// } catch (InterruptedException e) {
						// // TODO Auto-generated catch block
						// e.printStackTrace();
						// }
					}
					for (int j = 0; j < HConfig.NUMBER_OF_TABLES; j++) {
						try {
							threads[j].join();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					for (int j = 0; j < tmplist.size(); j++) {
						TenantToVoltDBInfo tmp = tmplist.get(j);
						if (tmp.getSender() != null) {
							tmp.getSender().sendMoveToVoltDBMessage(
									tmp.getTenantID(), tmp.getVoltDB_id());
						}
					}
				}
			}
			t2 = System.currentTimeMillis();
			System.out.format("--------Move finished in %.5fs---------%n",
					((double) t2 - t1) / 1000.0);
		} else {
			for (int i = 0; i < toVoltDBInfos.size(); i++) {
				TenantToVoltDBInfo tmp = toVoltDBInfos.get(i);
				new Retriver(tmp.getTenantID(), tmp.getVoltDB_id());
			}
			try {
				t1 = System.currentTimeMillis();
				Retriver.startRetrive();
				t2 = System.currentTimeMillis();
				System.out
						.format("----------------Write back completed in %.5fs-------------%n",
								((double) t2 - t1) / 1000.0);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for (int i = 0; i < toVoltDBInfos.size(); i++) {
				TenantToVoltDBInfo tmp = toVoltDBInfos.get(i);
				if (tmp.getSender() != null) {
					tmp.getSender().sendMoveToMySQLMessage(tmp.getTenantID(),
							tmp.getVoltDB_id());
				}
			}
		}
	}
}
