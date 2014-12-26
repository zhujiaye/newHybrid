package utillity;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.voltdb.client.Client;
import org.voltdb.client.ClientFactory;
import org.voltdb.client.ClientResponse;
import org.voltdb.client.ProcCallException;

import config.HConfig;
import server.TenantToVoltDBInfo;

public class VoltDBImporter implements Runnable {
	private String table_name;
	private ArrayList<TenantToVoltDBInfo> toVoltDBInfos;
	private String masterTableName;
	private String savedCsvFileName;
	private Client conn;
	private ClientResponse res;

	public VoltDBImporter(String table_name,
			ArrayList<TenantToVoltDBInfo> toVoltDBInfos) {
		this.table_name = table_name;
		this.toVoltDBInfos = toVoltDBInfos;
		this.masterTableName = table_name + "0";
		this.savedCsvFileName = HConfig.CSVPATH + "/" + table_name + ".csv";
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		long t1, t2;
		t1 = System.currentTimeMillis();
		try {
			Process p = Runtime.getRuntime().exec(
					"csvloader " + masterTableName + " -f " + savedCsvFileName);
			p.waitFor();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		t2 = System.currentTimeMillis();
		System.out.format("csvloader time for table %s:%f minutes%n",
				table_name, ((double) (t2 - t1)) / (60.0 * 1000.0));
		t1 = t2;
		try {
			conn = ClientFactory.createClient();
			conn.createConnection(HConfig.VOLTDB_SERVER);
			ArrayList<Integer>[] tenantsIDin = new ArrayList[HConfig.NUMBER_OF_ID_IN_VOLTDB];
			for (int i = 0; i < HConfig.NUMBER_OF_ID_IN_VOLTDB; i++)
				tenantsIDin[i] = new ArrayList<>();
			for (int i = 0; i < toVoltDBInfos.size(); i++) {
				TenantToVoltDBInfo tmp = toVoltDBInfos.get(i);
				int v_id = tmp.getVoltDB_id();
				tenantsIDin[v_id].add(tmp.getTenantID());
			}
			for (int i = 1; i < HConfig.NUMBER_OF_ID_IN_VOLTDB; i++) {
				if (tenantsIDin[i].size() == 0)
					continue;
				String condition = "";
				for (int j = 0; j < tenantsIDin[i].size(); j++) {
					if (j > 0)
						condition = condition.concat(" or ");
					condition = condition.concat("tenant_id="
							+ tenantsIDin[i].get(j));
				}
				res = conn.callProcedure("@AdHoc", "insert into " + table_name
						+ (i) + " select * from " + masterTableName + " where "
						+ condition + ";");
				res.getStatus();
				res.getResults();
				res = conn.callProcedure("@AdHoc", "delete from "
						+ masterTableName + " where " + condition + ";");
				res.getStatus();
				res.getResults();
			}
			/*
			 * for (int i = 0; i < toVoltDBInfos.size(); i++) {
			 * TenantToVoltDBInfo tmp = toVoltDBInfos.get(i); if
			 * (tmp.getVoltDB_id() == 0) continue; res = conn .callProcedure(
			 * "@AdHoc", "insert into " + table_name + tmp.getVoltDB_id() +
			 * " select * from " + masterTableName + " where tenant_id=" +
			 * tmp.getTenantID() + ";"); res.getStatus(); res.getResults(); res
			 * = conn.callProcedure("@AdHoc", "delete from " + masterTableName +
			 * " where tenant_id=" + tmp.getTenantID() + ";"); res.getStatus();
			 * res.getResults(); }
			 */
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProcCallException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.exit(1);
				}
		}
		t2 = System.currentTimeMillis();
		System.out.format("Finish partite %s table time:%f minutes%n",
				table_name, ((double) (t2 - t1)) / (60.0 * 1000.0));
	}
}
