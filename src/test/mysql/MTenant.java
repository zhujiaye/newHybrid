package test.mysql;

import java.util.Random;

import utillity.MysqlConnectionPool;
import utillity.VoltdbConnectionPool;
import client.HTenantClient;
import dbInfo.HResult;
import dbInfo.HSQLTimeOutException;

public class MTenant extends Thread {
	public static MysqlConnectionPool mPool;
	public static VoltdbConnectionPool vPool;

	public static void resetPool() throws InterruptedException {
		mPool = MysqlConnectionPool.getPool();
		vPool = VoltdbConnectionPool.getPool();
	}

	private int id;
	protected final double writePercent = 0.2;
	protected int queryThisInterval = 0;
	protected int readThisInterval = 0;
	protected int writeThisInterval = 0;

	public synchronized int queryNumber(int n) {
		if (n < 0) {
			queryThisInterval = 0;
		} else {
			queryThisInterval += n;
		}
		return queryThisInterval;
	}

	public synchronized int readNumber(int n) {
		if (n < 0) {
			readThisInterval = 0;
		} else {
			readThisInterval += n;
		}
		return readThisInterval;
	}

	public synchronized int writeNumber(int n) {
		if (n < 0) {
			writeThisInterval = 0;
		} else {
			writeThisInterval += n;
		}
		return writeThisInterval;
	}

	public MTenant() {
	}

	public MTenant(int id) {
		this.id = id;
	}

	public void init() {
		htc = new HTenantClient(id);
		htc.login();
		htc.start();
	}

	public void init_pool() {
		htc = new HTenantClient(id);
		htc.login();
		htc.start();
	}

	public void clean() {
		htc.stop();
		htc.logout();
		htc.shutdown();
	}

	public HTenantClient htc;

	public void run() {

		Random rand = new Random(System.nanoTime());
		HResult result;
		boolean isWrite;
		while (MTestMain.checkIsActive()) {
			if (rand.nextDouble() < this.writePercent) {
				isWrite = true;
				result = htc.sqlRandomUpdate();
			} else {
				isWrite = false;
				result = htc.sqlRandomSelect();
			}
			if (result != null && result.isSuccess()) {
				this.queryNumber(1);
				if (isWrite)
					this.writeNumber(1);
				else
					this.readNumber(1);
			}
		}

	}

	/**
	 * used by MVirtualTenant
	 * 
	 * @return
	 * @throws HSQLTimeOutException
	 */
	public boolean doSelect() throws HSQLTimeOutException {
		HResult result = htc.sqlRandomSelect();
		if (result != null && result.isSuccess())
			return true;
		return false;
	}

	/**
	 * used by MVirtualTenant
	 * 
	 * @return
	 * @throws HSQLTimeOutException
	 */
	public boolean doUpdate() throws HSQLTimeOutException {
		HResult result = htc.sqlRandomUpdate();
		if (result != null && result.isSuccess())
			return true;
		return false;
	}

}
