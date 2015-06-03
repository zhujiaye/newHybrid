package server;

import newhybrid.HException;
import config.HConfig;

public class HTenant {
	final private HConfig mConf;
	final private int mID;
	final private int mDataSize;
	final private HServer mServer;

	private boolean mLoggedIn;
	private boolean mStarted;

	private long mLogInTime;
	private long mStartTime;

	private boolean mIsInMysql;

	public HTenant(HServer server, int tenant_id) throws HException {
		mConf = HConfig.getConf();
		mServer = server;
		mID = tenant_id;
		// TODO get right dataszie
		if (mID >= 1 && mID <= 1500) {
			mDataSize = 7;
		} else if (mID > 1500 && mID <= 2400) {
			mDataSize = 16;
		} else if (mID > 2400 && mID <= 3000) {
			mDataSize = 24;
		} else {
			mDataSize = -1;
		}
		mLoggedIn = false;
		mStarted = false;
	}

	public int getID() {
		return mID;
	}

	public int getDataSize() {
		return mDataSize;
	}

	public int getDataSizeKind() {
		if (mID >= 1 && mID <= 1500) {
			return 0;
		} else if (mID > 1500 && mID <= 2400) {
			return 1;
		} else if (mID > 2400 && mID <= 3000) {
			return 2;
		} else {
			return -1;
		}
	}

	public synchronized void login() {
		if (mLoggedIn)
			return;
		if (mConf.getInitdb() == "voltdb") {
			// TODO move contents from mysql to voltdb
			mIsInMysql = false;
		} else
			mIsInMysql = true;
		mLoggedIn = true;
		mLogInTime = System.nanoTime();
		System.out.format("Tenant %d logged in server %s:%d%n", mID,
				mServer.getAddress(), mServer.getPort());
	}

	public synchronized void logout() {
		if (!mLoggedIn)
			return;
		stop();
		/*
		 * TODO before logged out, all updated contents in voltdb must be
		 * writtern to mysql
		 */
		mLoggedIn = false;
		System.out.format("Tenant %d logged out server %s:%d%n", mID,
				mServer.getAddress(), mServer.getPort());
	}

	public synchronized void start() {
		if (!mLoggedIn || mStarted)
			return;
		mStarted = true;
		mStartTime = System.nanoTime();
		System.out.format("Tenant %d start to query from server %s:%d%n", mID,
				mServer.getAddress(), mServer.getPort());
	}

	public synchronized void stop() {
		if (!mLoggedIn || !mStarted)
			return;
		mStarted = false;
		System.out.format("Tenant %d stop querying from server %s:%d%n", mID,
				mServer.getAddress(), mServer.getPort());
	}

	public synchronized boolean isLoggedIn() {
		return mLoggedIn;
	}

	public synchronized boolean isStarted() {
		if (!mLoggedIn)
			return false;
		return mStarted;
	}

	public synchronized boolean isUseMysql() {
		return mIsInMysql;
	}
}
