package test;

import newhybrid.HException;
import client.HTenantClient;

public class Test {
	static class ClientThread extends Thread {
		private HTenantClient mClient;

		public ClientThread(int id) throws HException {
			mClient = new HTenantClient(id);
		}

		public void run() {
			try {
				if (mClient.login())
					System.out.println("Tenant " + mClient.getID()
							+ " logged in");
				mClient.start();
				// if (mClient.logout())
				// System.out.println("Tenant " + mClient.getID()
				// + " logged out");
			} catch (HException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) throws HException {
		for (int i = 0; i < 10; i++) {
			new Test.ClientThread(i + 1).start();
		}
	}
}
