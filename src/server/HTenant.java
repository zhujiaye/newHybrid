package server;

import org.apache.log4j.Logger;

import newhybrid.TenantWorkload;
import config.Constants;
import config.HConfig;

public class HTenant {
	final static private Logger LOG = Logger
			.getLogger(Constants.LOGGER_NAME_SERVER);
	final private HConfig mConf;
	final private int mID;
	final private int mDataSize;
	final private HServer mServer;

	private volatile boolean mLoggedIn;
	private volatile boolean mStarted;
	private volatile boolean mBeingMovingToVoltdb;
	private volatile boolean mBeingMovingToMysql;
	private MysqlToVoltdbMoverThread mMovingToVoltdbThread = null;
	private VoltdbToMysqlMoverThread mMovingToMysqlThread = null;
	private long mLogInTime;
	private long mStartTime;

	private volatile boolean mIsInMysql;
	private int mIDInVoltdb = -1;

	private TenantWorkload mWorkload;

	public HTenant(HServer server, int tenant_id) {
		this(server, tenant_id, null);
	}

	public HTenant(HServer server, int tenant_id, TenantWorkload workload)
			 {
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

		mWorkload = workload;
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

	public synchronized int getIDInVoltdb() {
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

	public synchronized void logout() {
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

	public synchronized boolean isLoggedIn() {
		return mLoggedIn;
	}

	public synchronized boolean isStarted() {
		if (!mLoggedIn)
			return false;
		return mStarted;
	}

	public synchronized boolean isInMysql() {
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

	public synchronized boolean isBeingMovingToVoltdb() {
		return mBeingMovingToVoltdb;
	}

	public synchronized boolean isBeingMovingToMysql() {
		return mBeingMovingToMysql;
	}

	public synchronized void finishMovingToMysql() {
		mIsInMysql = true;
		mBeingMovingToMysql = mBeingMovingToVoltdb = false;
		mMovingToMysqlThread = null;
	}

	public synchronized void finishMovingToVoltdb(int voltdbID) {
		mIDInVoltdb = voltdbID;
		mIsInMysql = false;
		mBeingMovingToMysql = mBeingMovingToVoltdb = false;
		mMovingToVoltdbThread = null;
	}

	public synchronized void cancelMoving() {
		mBeingMovingToMysql = false;
		mBeingMovingToVoltdb = false;
		mMovingToMysqlThread = null;
		mMovingToVoltdbThread = null;
	}

	public synchronized void startMovingToVoltdb(MysqlToVoltdbMoverThread thread) {
		mMovingToVoltdbThread = thread;
		mBeingMovingToVoltdb = true;
	}

	public synchronized void startMovingToMysql(VoltdbToMysqlMoverThread thread) {
		mMovingToMysqlThread = thread;
		mBeingMovingToMysql = true;
	}

	public synchronized MysqlToVoltdbMoverThread getMovingToVoltdbThread() {
		return mMovingToVoltdbThread;
	}

	public synchronized VoltdbToMysqlMoverThread getMovingToMysqlThread() {
		return mMovingToMysqlThread;
	}

	/**
	 * Get workload for the future consideration
	 * 
	 * @return the workload in the future
	 *
	 */
	// TODO make this better
	public synchronized int getWorkloadAhead() {
		if (mWorkload == null)
			return 0;
		long elapsedTime = (System.nanoTime() - mStartTime);
		long split = elapsedTime / Constants.SPLIT_TIME;
		return mWorkload
				.getActualWorkloadAtSplit((int) (split + Constants.NUMBEROF_AHEAD_SPLITS));
		// int max = 0;
		// for (int i = 1; i <= Constants.NUMBEROF_AHEAD_SPLITS; i++) {
		// int tmp;
		// tmp = mWorkload.getActualWorkloadAtSplit((int) split + i);
		// if (tmp > max)
		// max = tmp;
		// }
		// return max;
	}

	public synchronized int getWorkloadNow() {
		if (mWorkload == null)
			return 0;
		long elapsedTime = (System.nanoTime() - mStartTime);
		long split = elapsedTime / Constants.SPLIT_TIME;
		return mWorkload.getActualWorkloadAtSplit((int) split);
	}

	public synchronized void setWorkload(TenantWorkload workload) {
		mWorkload = workload;
	}

	public synchronized TenantWorkload getWorkload() {
		return mWorkload;
	}

}
