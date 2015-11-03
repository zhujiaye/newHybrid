package test;

import java.util.Random;

import newhybrid.HeartbeatExecutor;
import newhybrid.HeartbeatThread;
import newhybrid.NoServerConnectionException;

import org.apache.log4j.Logger;

import thrift.DbmsException;
import thrift.LockException;
import thrift.NoTenantException;
import thrift.NoWorkerException;
import client.TenantClient;
import config.Constants;
import dbInfo.HResult;
import dbInfo.HSQLException;

class TenantWorkloadAdderExecutor implements HeartbeatExecutor {
	private final TenantThread TENANT_THREAD;
	private int mCount;

	public TenantWorkloadAdderExecutor(TenantThread tenantThread, int totCount) {
		TENANT_THREAD = tenantThread;
		mCount = totCount;
	}

	@Override
	public void heartbeat() {
		if (mCount > 0) {
			mCount--;
			TENANT_THREAD.addWorkload(1);
		}
	}

}

public class TenantThread extends Thread {
	static private final Logger LOG = Logger.getLogger(Constants.LOGGER_NAME);
	private final int ID;
	private final TenantClient TCLIENT;
	private final int SLO;
	private final double LIMIT;

	private HeartbeatThread mAdderThread;

	private int mTotSuccessQueries;
	private int mTotSentQueries;
	private int mRemainQueries;
	private int mSentQueriesInSplit;
	private int mSuccessQueriesInSplit;
	private int mWorkloadInSplit;
	private int mSplit;
	private boolean mIsFinished = false;
	private boolean mIsSuccess = false;

	public TenantThread(int tenantID, String server_address, int server_port,
			int slo) {
		ID = tenantID;
		SLO = slo;
		TCLIENT = new TenantClient(ID, server_address, server_port);
		try {
			TCLIENT.login();
		} catch (NoTenantException | NoServerConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LIMIT = Constants.SPLIT_TIME / (slo * 1.0);
		mTotSentQueries = 0;
		mTotSuccessQueries = 0;
		mRemainQueries = 0;
		mSentQueriesInSplit = 0;
		mSuccessQueriesInSplit = 0;
		mWorkloadInSplit = 0;
		mSplit = -1;
		mAdderThread = null;
	}

	@Override
	public void run() {
		Random random = new Random(System.currentTimeMillis());
		HResult result = null;
		try {
			synchronized (this) {
				while (!mIsFinished) {
					if (mRemainQueries == 0)
						this.wait();
					else {
						long t1 = System.nanoTime();
						TCLIENT.lock_lock();
						try {
							if (random.nextInt(100) < 50)
								result = TCLIENT.selectRandomly();
							else
								result = TCLIENT.updateRandomly();
						} catch (DbmsException e) {
							result = null;
						}
						TCLIENT.lock_release();
						long t2 = System.nanoTime();
						long latency = t2 - t1;
						// System.out.println(latency / 1000000 + " " + LIMIT
						// / 1000000);
						mSentQueriesInSplit++;
						mTotSentQueries++;
						mRemainQueries--;
						if (result == null) {
							LOG.info("tenant " + ID + ":NULL result!");
							continue;
						}
						if (!result.isSuccess()) {
							LOG.info("tenant " + ID + ":" + result.getMessage());
							result.release();
							continue;
						}
						if (latency > LIMIT) {
							// LOG.info("tenant " + ID +
							// " violated a query:run "
							// + latency / 1000000 + "ms limit " + LIMIT
							// / 1000000 + "ms");
							result.release();
							continue;
						}
						mSuccessQueriesInSplit++;
						mTotSuccessQueries++;
						result.release();
					}
				}
				mIsSuccess = true;
			}
		} catch (InterruptedException | NoTenantException
				| NoServerConnectionException | NoWorkerException
				| LockException | HSQLException e) {
			LOG.error(e.getMessage());
			return;
		}
	}

	public synchronized void addWorkload(int num) {
		mWorkloadInSplit += num;
		mRemainQueries += num;
		this.notifyAll();
	}

	/**
	 * 
	 * @return number of query the tenant violated
	 */
	public synchronized SplitResult updateSplitAndGetSplitResult(
			int workloadsInNextSplit) {
		SplitResult result = null;
		if (mSplit != -1) {
			result = new SplitResult(mWorkloadInSplit, mSentQueriesInSplit,
						mSuccessQueriesInSplit);
		}
		mSplit++;
		mWorkloadInSplit = 0;
		// set to 0 means every split is not responsible for others' workload
		mRemainQueries = 0;
		mSentQueriesInSplit = 0;
		mSuccessQueriesInSplit = 0;
		if (workloadsInNextSplit > 0) {
			if (mAdderThread != null) {
				mAdderThread.shutdown();
			}
			long tickTime = (Constants.SPLIT_TIME - Constants.S)
					/ workloadsInNextSplit;
			mAdderThread = new HeartbeatThread(
					"workloadAdder_" + ID,
					new TenantWorkloadAdderExecutor(this, workloadsInNextSplit),
					tickTime);
			mAdderThread.start();
		}
		return result;
	}

	public void shutdown() {
		if (mAdderThread != null) {
			mAdderThread.shutdown();
		}
		synchronized (this) {
			mIsFinished = true;
			this.notifyAll();
		}
	}

	public boolean isSuccess() {
		return mIsSuccess;
	}

	public int getTotSentQuery() {
		return mTotSentQueries;
	}

	public int getTotSuccessQuery() {
		return mTotSuccessQueries;
	}
}
