package utillity;

import java.util.ArrayList;

import config.HConfig;

public class MySQLExporter implements Runnable {
	private ArrayList<Integer> tenantids;
	private String table_name;

	public MySQLExporter(String table_name, ArrayList<Integer> tenantids) {
		this.table_name = table_name;
		this.tenantids = tenantids;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		int last = -1;
		long t1, t2;
		ArrayList<Thread> threads = new ArrayList<>();
		t1 = System.currentTimeMillis();
		for (int i = 0; i < tenantids.size(); i++) {
			if (i - last >= HConfig.MYSQLEXPORTER_CONCURRENCY
					|| i == tenantids.size() - 1) {
				threads.clear();
				for (int j = last + 1; j <= i; j++) {
					threads.add(new Thread(new MySQLExporterForTenant(
							table_name, tenantids.get(j))));
				}
				last = i;
				for (int j = 0; j < threads.size(); j++) {
					threads.get(j).start();
				}
				for (int j = 0; j < threads.size(); j++) {
					try {
						threads.get(j).join();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				// System.out.println("already finish movement for " + (i + 1)
				// + " tenants");
			}
		}
		t2 = System.currentTimeMillis();
		System.out.format("MySQL Export for table %s finished in %f minutes%n",
				table_name, ((double) (t2 - t1)) / (60.0 * 1000.0));
	}
}
