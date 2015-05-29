package server;

import newhybrid.HException;
import config.HConfig;

public class HTenant {
	final private HConfig mConf;
	private HServer mServer;
	private int mID;
	private boolean mLoggedIn;
	private boolean mStarted;

	private long mLogInTime;
	private long mStartTime;

	private boolean isInMysql;

	public HTenant(HServer server, int tenant_id) throws HException {
		mConf = HConfig.getConf();
		mServer = server;
		mID = tenant_id;
		mLoggedIn = false;
		mStarted = false;
	}

	public int getID() {
		return mID;
	}

	public void login() {
		if (mLoggedIn)
			return;
		if (mConf.getInitdb() == "voltdb") {
			// TODO complete this part
		}
		mLoggedIn = true;
		mLogInTime = System.nanoTime();
		System.out.format("Tenant %d logged in server %s:%d%n", mID,
				mServer.getAddress(), mServer.getPort());
	}

	public void logout() {
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

	public void start() {
		if (!mLoggedIn || mStarted)
			return;
		mStarted = true;
		mStartTime = System.nanoTime();
		System.out.format("Tenant %d start to query from server %s:%d%n", mID,
				mServer.getAddress(), mServer.getPort());
	}

	public void stop() {
		if (!mLoggedIn || !mStarted)
			return;
		mStarted = false;
		System.out.format("Tenant %d stop querying from server %s:%d%n", mID,
				mServer.getAddress(), mServer.getPort());
	}
}
