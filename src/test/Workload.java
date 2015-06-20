package test;

import utillity.MysqlConnectionPool;
import utillity.VoltdbConnectionPool;
import client.HTenantClient;
import newhybrid.HException;
import newhybrid.HQueryResult;
import newhybrid.HSQLTimeOutException;
import config.HConfig;

public class Workload {

	public static void main(String[] args) throws HException {
		HTenantClient htc = new HTenantClient(3000);
		htc.login();
		htc.start();
		for (int i = 0; i < 10; i++) {
			HQueryResult result;
			try {
				if (i % 2 == 0)
					result = htc.sqlRandomUpdate();
				else
					result = htc.sqlRandomSelect();
				if (result != null && result.isSuccess()) {
					result.print();
				} else {
					System.out.println("failed");
				}
				Thread.sleep(10000);
			} catch (HSQLTimeOutException e) {
				e.printStackTrace();
				System.out.println("Out of time");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		htc.stop();
		htc.logout();
		htc.shutdown();
	}

}
