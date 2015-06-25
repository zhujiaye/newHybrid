package test;

import java.util.Random;

import org.apache.log4j.Logger;

import utillity.MysqlConnectionPool;
import utillity.VoltdbConnectionPool;
import config.Constants;
import newhybrid.HException;
import newhybrid.HQueryResult;
import newhybrid.HSQLTimeOutException;
import client.HTenantClient;

public class TestThroughput {
	final private static Logger LOG = Logger
			.getLogger(Constants.LOGGER_NAME_CLIENT);

	static class ClientThread extends Thread {
		private HTenantClient mClient;

		public ClientThread(int id) throws HException {
			mClient = new HTenantClient(id);
		}

		public void run() {
			try {
				if (mClient.login()) {
					LOG.info("Tenant " + mClient.getID() + " logged in");
					mClient.start();
				}
				int cnt = 0;
				HQueryResult result = null;
				Random ran = new Random(System.currentTimeMillis());
				while (cnt < 960) {
					// System.gc();
					cnt++;
					if (ran.nextInt(100) < 80)
						result = mClient.sqlRandomSelect();
					else
						result = mClient.sqlRandomUpdate();
					if (result.isSuccess())
						mClient.completeOneQuery();
					else
						LOG.info(result.getMessage());
					result.close();
					try {
						Thread.sleep(625);
					} catch (InterruptedException e) {
						LOG.error("interrupted!");
					}

				}
				mClient.stop();
				LOG.info("Tenant " + mClient.getID() + " finish querying");
				mClient.logout();
				LOG.info("Tenant " + mClient.getID() + " logged out");
				mClient.shutdown();

			} catch (HException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) throws HException {
		LOG.info("start");
		for (int i = 0; i < 1000; i++) {
			new TestThroughput.ClientThread(i + 1).start();
		}
	}
}
