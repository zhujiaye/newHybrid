package utillity;

import java.util.ArrayList;

import server.TenantToVoltDBInfo;

public class TableMover implements Runnable {
	private String table_name;
	private ArrayList<TenantToVoltDBInfo> toVoltDBInfos;
	private ArrayList<Integer> tenantids;

	public TableMover(String table_name,
			ArrayList<TenantToVoltDBInfo> toVoltDBInfos) {
		this.table_name = table_name;
		this.toVoltDBInfos = toVoltDBInfos;
		tenantids = new ArrayList<>();
		for (int i = 0; i < toVoltDBInfos.size(); i++) {
			TenantToVoltDBInfo tmp = toVoltDBInfos.get(i);
			tenantids.add(tmp.getTenantID());
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		Thread exporter, combiner, importer;
		exporter = new Thread(new MySQLExporter(table_name, tenantids));
		combiner = new Thread(new TableCombiner(table_name, tenantids));
		importer = new Thread(new VoltDBImporter(table_name, toVoltDBInfos));
		try {
			exporter.start();
			exporter.join();
			combiner.start();
			combiner.join();
			importer.start();
			importer.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
