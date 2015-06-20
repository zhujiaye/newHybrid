package test;

import org.apache.log4j.Logger;

import config.Constants;
import newhybrid.HException;
import newhybrid.HQueryResult;
import newhybrid.HSQLTimeOutException;
import client.HTenantClient;

public class Test {
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
					mClient.start();
				}
				int cnt = 0;
				HQueryResult result = null;
				while (cnt < 200) {
					try {
						cnt++;
						if (cnt < 0)
							result = mClient.sqlRandomSelect();
						else
							result = mClient.sqlRandomUpdate();

						try {
							Thread.sleep(250);
						} catch (InterruptedException e) {
							LOG.error("interrupted!");
						}

					} catch (HSQLTimeOutException e) {
						LOG.info("one query TIMEOUT");
						continue;
					}
					if (result.isSuccess())
						mClient.completeOneQuery();
				}
				System.out.println("Finish");
				mClient.logout();
				mClient.shutdown();

			} catch (HException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) throws HException {
		for (int i = 0; i < 200; i++) {
			new Test.ClientThread(i + 1).start();
		}
	}
}
