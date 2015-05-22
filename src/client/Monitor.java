package client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import utillity.HTenantDynamicInfo;
import utillity.HTimer;
import utillity.MysqlConnectionPool;
import utillity.VoltdbConnectionPool;
import config.HConfig;

public class Monitor implements Runnable {
	final private int fromID, toID;
	final private int number_of_tenants;
	private HTenant[] tenants;
	private Thread[] threads;
	private Sender sender;
	private Receiver receiver;
	private Socket socket;

	private boolean allConfirmed;
	private MysqlConnectionPool mySQLpool;
	private VoltdbConnectionPool voltDBpool;

	private int waiting_tenants;
	private int confirmed = 0;

	public Monitor(int fromID, int toID) {
		this.fromID = fromID;
		this.toID = toID;
		number_of_tenants = toID - fromID + 1;
		tenants = new HTenant[number_of_tenants];
		threads = new Thread[number_of_tenants];
		setAllConfirmed(false);
		voltDBpool = new VoltdbConnectionPool(HConfig.VOLTDBPOOLINIT);
		mySQLpool = new MysqlConnectionPool(HConfig.MYSQLPOOLINIT);
		try {
			socket = new Socket(HConfig.SOCKET_HOST, HConfig.SOCKET_PORT);
			if (socket != null) {
				sender = new Sender(socket);
				receiver = new Receiver(socket, this);
				new Thread(receiver).start();
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		System.out.println("Monitoring........");
		confirmed = 0;
		// TODO Auto-generated method stub
		for (int id = fromID; id <= toID; id++) {
			tenants[id - fromID] = new HTenant(id, this, mySQLpool, voltDBpool);
			threads[id - fromID] = new Thread(tenants[id - fromID]);
			sender.sendReadyMessage(id);
		}
		while (!isAllConfirmed()) {
		}
		;
		System.out.println("Start!");
		// new Thread(new HTimer(this, HConfig.CLIENT_TIME_GAP, true)).start();
		waiting_tenants = 0;
		for (int id = fromID; id <= toID; id++)
			threads[id - fromID].start();
		int interval = 0;
		int split = 0;
		while (true) {
			split++;
			// printQueryingTenants();
			if ((split - 1) % HConfig.NUMBER_OF_SPLITS_IN_INTERVAL == 0) {
				for (int id = fromID; id <= toID; id++)
					tenants[id - fromID].updateIntervalInfo();
				interval++;
			}
			int tmp = getWaitingTenants();
			System.out
					.format("---------last split:%d violation.  Interval:%d Split:%d--------%n",
							number_of_tenants - tmp, interval, split);
			for (int id = fromID; id <= toID; id++)
				tenants[id - fromID].updateSplitInfo();
			synchronized (this) {
				this.notifyAll();
			}
			if (interval > HConfig.NUMBER_OF_INTERVAL)
				break;
			try {
				Thread.sleep(HConfig.SPLIT_TIME);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		for (int id = fromID; id <= toID; id++)
			try {
				threads[id - fromID].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		mySQLpool.clear();
		voltDBpool.clear();
		for (int id = fromID; id <= toID; id++) {
			sender.sendFinishedMessage(id);
		}
		System.out.println("All Tenants finished! Now check the performance!");
		for (int id = fromID; id <= toID; id++) {
			tenants[id - fromID].print();
		}
	}

	// public void confirmTenant(int id) {
	// if (id >= fromID && id <= toID) {
	// confirmedTenants++;
	// System.out.format("Confirmed %d tenants%n", confirmedTenants);
	// }
	// if (confirmedTenants == number_of_tenants)
	// setAllConfirmed(true);
	// }

	public void setConfirmedForTenant(int id) {
		confirmed++;
		if (confirmed % 50 == 0) {
			System.out.println("Confirmed " + confirmed + " tenants");
		}
	}

	public void changeToVoltDBForTenant(int id, int voltDB_id) {
		if (id >= fromID && id <= toID) {
			tenants[id - fromID].setIsToUseVoltDB(true);
			tenants[id - fromID].setIsToUseVoltDBID(voltDB_id);
			// tenants[id - fromID].updateUsingDB();
		} else
			System.out
					.println("Error from changeToVoltDBForTenant: id out of index");
	}

	public void changeToMySQLDBForTenant(int id) {
		if (id >= fromID && id <= toID) {
			tenants[id - fromID].setIsToUseMySQLDB(true);
			// tenants[id - fromID].updateUsingDB();
		} else
			System.out
					.println("Error from changeToMySQLDBForTenant: id out of index");
	}

	public void sendInfoMessageForTenant(int id, HTenantDynamicInfo info) {
		sender.sendInfoMessageForTenant(id, info);
	}

	public synchronized void setAllConfirmed(boolean boo) {
		allConfirmed = boo;
	}

	public synchronized boolean isAllConfirmed() {
		return allConfirmed;
	}

	private synchronized int getWaitingTenants() {
		return waiting_tenants;
	}

	public synchronized void addWatingTenants() {
		waiting_tenants++;
		if (HConfig.debug)
			System.out.println("waiting tenants:" + waiting_tenants);
	}

	public synchronized void removeWatingTenants() {
		waiting_tenants--;
		if (HConfig.debug)
			System.out.println("waiting tenants:" + waiting_tenants);
	}

	public synchronized void printWaitingTenants() {
		System.out.println("waiting tenants:" + waiting_tenants);
	}

	public synchronized void printQueryingTenants() {
		System.out.println("querying tenants:"
				+ (number_of_tenants - waiting_tenants));
	}
}
