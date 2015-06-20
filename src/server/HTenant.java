package server;

import org.apache.log4j.Logger;
import newhybrid.HException;
import config.Constants;
import config.HConfig;

public class HTenant implements Comparable<HTenant> {
	final static private Logger LOG = Logger
			.getLogger(Constants.LOGGER_NAME_SERVER);
	final private HConfig mConf;
	final private int mID;
	final private int mDataSize;
	final private HServer mServer;

	private boolean mLoggedIn;
	private boolean mStarted;
	private boolean mBeingMovingToVoltdb;
	private boolean mBeingMovingToMysql;
	private MysqlToVoltdbMoverThread mMovingToVoltdbThread = null;
	private VoltdbToMysqlMoverThread mMovingToMysqlThread = null;
	private long mLogInTime;
	private long mStartTime;

	private boolean mIsInMysql;
	private int mIDInVoltdb = -1;

	private int[] workloads;

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
		mBeingMovingToMysql = false;
		mBeingMovingToVoltdb = false;
		mIsInMysql = true;

		workloads = new int[] { 0 };
		/*
		 * TODO get workload information for the tenant the following is just
		 * for test
		 */
		// if (mID % 2 == 1) {
		// if (mID % 4 == 1)
		// workloads = new int[] { 5, 5, mID * 10, 5, 5, 5, 5, 5 };
		// else
		// workloads = new int[] { 5, 5, mID * 10, 5, 5, mID * 10, 5, 5 };
		// } else {
		// if (mID % 4 == 0)
		// workloads = new int[] { 5, 5, 5, 5, 5, mID * 10, 5, 5 };
		// else
		// workloads = new int[] { 5, 5, 5, 5, 5, 5, 5, 5 };
		// }
	}

	public HServer getServer() {
		return mServer;
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

	public int getIDInVoltdb() {
		return mIDInVoltdb;
	}

	public synchronized void login() {
		if (mLoggedIn)
			return;
		if (mConf.getInitdb().equals(Constants.INITDB_VOLTDB))
			mIsInMysql = false;
		else
			mIsInMysql = true;
		mLoggedIn = true;
		mLogInTime = System.nanoTime();
		LOG.debug(String.format("Tenant %d logged in server %s:%d", mID,
				mServer.getAddress(), mServer.getPort()));
	}

	public synchronized void logout() throws HException {
		if (!mLoggedIn)
			return;
		stop();
		mLoggedIn = false;
		LOG.debug(String.format("Tenant %d logged out server %s:%d", mID,
				mServer.getAddress(), mServer.getPort()));
	}

	public synchronized void start() {
		if (!mLoggedIn || mStarted)
			return;
		mStarted = true;
		mStartTime = System.nanoTime();
		LOG.debug(String.format("Tenant %d start to query from server %s:%d",
				mID, mServer.getAddress(), mServer.getPort()));
	}

	public synchronized void stop() {
		if (!mLoggedIn || !mStarted)
			return;
		mStarted = false;
		LOG.debug(String.format("Tenant %d stop querying from server %s:%d",
				mID, mServer.getAddress(), mServer.getPort()));
	}

	public boolean isLoggedIn() {
		return mLoggedIn;
	}

	public boolean isStarted() {
		if (!mLoggedIn)
			return false;
		return mStarted;
	}

	public boolean isInMysql() {
		return mIsInMysql;
	}

	public synchronized boolean isInMysqlAhead() {
		return (mIsInMysql && !mBeingMovingToVoltdb) || mBeingMovingToMysql;
	}

	public synchronized boolean isInVoltdbAhead() {
		return (!mIsInMysql && !mBeingMovingToMysql) || mBeingMovingToVoltdb;
	}

	public synchronized boolean isInMysqlPure() {
		return mIsInMysql && !mBeingMovingToMysql && !mBeingMovingToVoltdb;
	}

	public synchronized boolean isInVoltdbPure() {
		return !mIsInMysql && !mBeingMovingToMysql && !mBeingMovingToVoltdb;
	}

	public boolean isBeingMovingToVoltdb() {
		return mBeingMovingToVoltdb;
	}

	public boolean isBeingMovingToMysql() {
		return mBeingMovingToMysql;
	}

	public synchronized void finishMovingToMysql() {
		mIsInMysql = true;
		mBeingMovingToMysql = mBeingMovingToVoltdb = false;
	}

	public synchronized void finishMovingToVoltdb(int voltdbID) {
		mIDInVoltdb = voltdbID;
		mIsInMysql = false;
		mBeingMovingToMysql = mBeingMovingToVoltdb = false;
	}

	public synchronized void cancelMoving() {
		mBeingMovingToMysql = false;
		mBeingMovingToVoltdb = false;
	}

	public synchronized void startMovingToVoltdb(MysqlToVoltdbMoverThread thread) {
		mMovingToVoltdbThread = thread;
		mBeingMovingToVoltdb = true;
	}

	public synchronized void startMovingToMysql(VoltdbToMysqlMoverThread thread) {
		mMovingToMysqlThread = thread;
		mBeingMovingToMysql = true;
	}

	public MysqlToVoltdbMoverThread getMovingToVoltdbThread() {
		return mMovingToVoltdbThread;
	}

	public VoltdbToMysqlMoverThread getMovingToMysqlThread() {
		return mMovingToMysqlThread;
	}

	/**
	 * Get workload for the future consideration, and the workload returned is
	 * the max workload in the following several splits
	 * 
	 * @return the workload in the future
	 *
	 */
	public int getWorkloadAhead() {
		long elapsedTime = (System.nanoTime() - mStartTime);
		long split = elapsedTime / Constants.SPLIT_TIME;
		int max = 0;
		for (int i = 1; i <= Constants.NUMBEROF_AHEAD_SPLITS; i++) {
			int tmp;
			if (split + i >= workloads.length)
				tmp = 0;
			else
				tmp = workloads[(int) split + i];
			if (tmp > max)
				max = tmp;
		}
		return max;
	}

	public int getWorkloadNow() {
		long elapsedTime = (System.nanoTime() - mStartTime);
		long split = elapsedTime / Constants.SPLIT_TIME;
		if (split >= workloads.length)
			return 0;
		else
			return workloads[(int) split];
	}

	@Override
	public int compareTo(HTenant o) {
		int x1 = this.getWorkloadAhead();
		int y1 = this.getDataSize();
		int x2 = o.getWorkloadAhead();
		int y2 = o.getDataSize();

		return x2 * y1 - x1 * y2;
	}
}
