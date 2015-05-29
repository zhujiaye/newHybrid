package test;

import utillity.MysqlConnectionPool;
import utillity.VoltdbConnectionPool;
import client.HTenantClient;
import newhybrid.HException;
import newhybrid.HQueryResult;
import newhybrid.HSQLTimeOutException;
import config.HConfig;

public class Main {

	public static void main(String[] args) throws HException {
		HTenantClient htc = new HTenantClient(1);
		for (int i = 0; i < 100; i++) {
			HQueryResult result;
			try {
				if (i % 2 == 0)
					result = htc.sqlRandomUpdate();
				else
					result = htc.sqlRandomSelect();
				if (result.isSuccess()) {
					result.print();
				} else {
					System.out.println("failed");
				}
				Thread.sleep(1);
			} catch (HSQLTimeOutException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("Out of time");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		VoltdbConnectionPool pool = new VoltdbConnectionPool();
		for (int i = 0; i < 100; i++) {
			pool.getConnection();
			System.out.println(i);
		}
	}

}
