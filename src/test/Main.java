package test;

import client.HTenantClient;
import newhybrid.HException;
import newhybrid.HSQLTimeOutException;
import config.HConfig;

public class Main {

	public static void main(String[] args) throws HException {
		HTenantClient htc = new HTenantClient(1);
		for (int i = 0; i < 10; i++) {
			boolean success;
			try {
				success = htc.sqlRandomSelect();
				System.out.println(success);
				Thread.sleep(1000);
			} catch (HSQLTimeOutException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("Out of time");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
