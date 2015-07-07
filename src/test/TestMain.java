package test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import newhybrid.HException;
import newhybrid.HQueryResult;
import newhybrid.HeartbeatExecutor;
import newhybrid.HeartbeatThread;
import newhybrid.TenantWorkload;

import org.apache.log4j.Logger;

import client.HTenantClient;
import client.ServerClient;
import config.Constants;
import thrift.SplitResult;
import thrift.SuccessQueryResult;
import thrift.TenantResult;
import utillity.WorkloadLoader;

public class TestMain {
	final static private Logger LOG = Logger.getLogger(Constants.LOGGER_NAME);

	public static void main(String[] args) throws HException,
			InterruptedException {
		String usage = "TestMain firstTenantID lastTenantID inputWorkloadFileName outputResultFileName";
		if (args.length < 4) {
			LOG.error("not enough arguments!");
			LOG.info(usage);
			return;
		}
		WorkloadLoader workloadLoader;
		String outputFileName;
		workloadLoader = new WorkloadLoader(Constants.WORKLOAD_DIR + "/"
				+ args[2]);
		outputFileName = args[3];

		if (!workloadLoader.load()) {
			LOG.error("no workload file!");
			return;
		} else {
			int start = 0;
			int end = 0;
			try {
				start = Integer.valueOf(args[0]);
				end = Integer.valueOf(args[1]);
			} catch (NumberFormatException e) {
				LOG.error("wrong number format!");
				LOG.info(usage);
				return;
			}
			if (start > end) {
				LOG.error("firstTenantID bigger than lastTenantID");
				LOG.info(usage);
				return;
			}
			ClientThread[] clientThreads = new ClientThread[end - start + 1];
			HeartbeatThread[] clientWorkloadAdderThreads = new HeartbeatThread[end
					- start + 1];
			TenantWorkload[] clientWorkloads = new TenantWorkload[end - start
					+ 1];
			for (int i = start; i <= end; i++) {
				HTenantClient htc = new HTenantClient(i);
				htc.connect();
				clientThreads[i - start] = new ClientThread(htc, workloadLoader
						.getWorkloadForTenant(i).getWH());
				clientWorkloadAdderThreads[i - start] = null;
				clientWorkloads[i - start] = workloadLoader
						.getWorkloadForTenant(i);
			}
			LOG.info(String.format("Test from tenant %d to %d starts(login)",
					start, end));
			int totThreads = clientThreads.length;
			int splits = workloadLoader.getNumberOfSplits();
			for (int i = 0; i < totThreads; i++) {
				clientThreads[i].start();
			}
			ServerClient serverClient = new ServerClient();
			while (!serverClient.tenantAllLoggedIn()) {
				Thread.sleep(1000);
			}
			serverClient.shutdown();
			for (int i = 0; i < totThreads; i++) {
				clientThreads[i].startQuery();
			}
			LOG.info(String.format("Test from tenant %d to %d starts(start)",
					start, end));
			int violatedCount = 0;
			for (int i = 0; i < splits; i++) {
				violatedCount = 0;
				for (int j = 0; j < totThreads; j++) {
					if (!clientThreads[j].updateSplit())
						violatedCount++;
					if (clientWorkloadAdderThreads[j] != null)
						clientWorkloadAdderThreads[j].shutdown();
					int totCount = clientWorkloads[j]
							.getActualWorkloadAtSplit(i);
					if (totCount > 0) {
						long tickTime = (Constants.SPLIT_TIME - Constants.S * 1)
								/ totCount;
						LOG.debug("In split " + i + " tenant " + (j + start)
								+ " must be add " + totCount
								+ " workloads speed" + (double) tickTime
								/ 1000000000.0);
						clientWorkloadAdderThreads[j] = new HeartbeatThread(
								"client" + (j + start) + "_workloadAdderThread",
								new ClientWorkloadAdderHeartbeatExecutor(
										clientThreads[j], totCount), tickTime);
						clientWorkloadAdderThreads[j].start();
					}
				}
				if (i - 1 >= 0)
					LOG.info("Split " + i + ":violated number " + violatedCount);
				try {
					Thread.sleep(Constants.SPLIT_TIME / 1000000);
				} catch (InterruptedException e) {
					LOG.error("Interrupted while sleeping in split " + i + ":"
							+ e.getMessage());
					return;
				}
			}
			violatedCount = 0;
			for (int i = 0; i < totThreads; i++) {
				if (clientWorkloadAdderThreads[i] != null)
					clientWorkloadAdderThreads[i].shutdown();
				if (!clientThreads[i].updateSplit())
					violatedCount++;
				clientThreads[i].finishQuery();
				synchronized (clientThreads[i]) {
					clientThreads[i].notify();
				}
			}
			LOG.info("Split " + splits + ":violated number " + violatedCount);
			LOG.info(String
					.format("Test from tenant %d to %d ends(stop querying)",
							start, end));
			try {
				for (int i = 0; i < totThreads; i++)
					clientThreads[i].join();
			} catch (InterruptedException e) {
				LOG.error("Interrupted while waiting client threads to die:"
						+ e.getMessage());
				return;
			}
			serverClient = new ServerClient();
			for (int i = 0; i < totThreads; i++) {
				LOG.debug("report result for tenant " + (i + start));
				TenantWorkload workload = workloadLoader.getWorkloadForTenant(i
						+ start);
				TenantResult result = new TenantResult(workload.getID(),
						workload.getSLO(), workload.getDataSize(),
						workload.getWH(), clientThreads[i].getSplitResults(),
						clientThreads[i].getSuccessQueryResults());
				serverClient.serverReportResult(result, outputFileName);
			}
			serverClient.shutdown();
			LOG.info(String.format(
					"Test from tenant %d to %d ends(logged out)", start, end));
		}
	}
}

class ClientWorkloadAdderHeartbeatExecutor implements HeartbeatExecutor {
	final private ClientThread mClientThread;
	private int mCount;

	public ClientWorkloadAdderHeartbeatExecutor(ClientThread clientThread,
			int totCount) {
		mClientThread = clientThread;
		mCount = totCount;
	}

	@Override
	public void heartbeat() throws HException {
		if (mCount > 0) {
			mCount--;
			mClientThread.addWorkload(1);
			synchronized (mClientThread) {
				mClientThread.notify();
			}
		}
	}

}

class ClientThread extends Thread {
	final static private Logger LOG = Logger.getLogger(Constants.LOGGER_NAME);
	final private HTenantClient HTC;
	final private int mWH;

	private int mTotSuccessQueries;
	private int mRemainQueries;
	private int mLastRemainQueries;
	private int mSentQueriesInSplit;
	private int mSuccessQueriesInSplit;
	private int mWorkloadInSplit;
	private int mSplit;
	private volatile boolean mIsFinished = false;
	private volatile boolean mIsStarted = false;
	private ArrayList<SplitResult> mSplitResults = null;
	private ArrayList<SuccessQueryResult> mSuccessQueryResults = null;

	public ClientThread(HTenantClient htc, int WH) throws HException {
		HTC = htc;
		mWH = WH;
		mTotSuccessQueries = 0;
		mRemainQueries = 0;
		mSentQueriesInSplit = 0;
		mSuccessQueriesInSplit = 0;
		mWorkloadInSplit = 0;
		mSplit = -1;
		mSplitResults = new ArrayList<>();
		mSuccessQueryResults = new ArrayList<>();
	}

	@Override
	public void run() {
		Random random = new Random(System.currentTimeMillis());
		HQueryResult result = null;
		try {
			HTC.login();
			while (!mIsStarted) {
				Thread.sleep(1000);
			}
			HTC.start();
			while (!mIsFinished) {
				if (mRemainQueries == 0) {
					synchronized (this) {
						// HTC.cleanConnect();
						this.wait();
					}
				} else {
					if (random.nextInt(100) < 80)
						result = HTC.sqlRandomSelect();
					else
						result = HTC.sqlRandomUpdate();
					synchronized (this) {
						mSentQueriesInSplit++;
						mRemainQueries--;
						if (result.isSuccess()) {
							mSuccessQueriesInSplit++;
							mTotSuccessQueries++;
							HTC.completeOneQuery();
							SuccessQueryResult queryResult = new SuccessQueryResult(
									mTotSuccessQueries, result.isInMysql(),
									result.isRead(), result.getStartTime(),
									result.getEndTime(), result.getLatency(),
									mSplit);
							mSuccessQueryResults.add(queryResult);
						}
					}
				}
			}
			HTC.stop();
			HTC.logout();
			HTC.shutdown();
		} catch (HException e) {
			LOG.error(e.getMessage());
			mIsFinished = true;
		} catch (InterruptedException e) {
			if (!mIsFinished) {
				LOG.error("Interrupted while waiting!:" + e.getMessage());
				mIsFinished = true;
			}
			return;
		}
	}

	public synchronized void addWorkload(int num) {
		mWorkloadInSplit += num;
		mRemainQueries += num;
	}

	/**
	 * 
	 * @return true if the tenant is not violated
	 */
	public synchronized boolean updateSplit() {
		boolean ok = true;
		if (mSplit != -1) {
			SplitResult splitResult = new SplitResult(mSplit,
					mLastRemainQueries, mWorkloadInSplit, mRemainQueries,
					mSentQueriesInSplit, mSuccessQueriesInSplit);
			mSplitResults.add(splitResult);
			if (mSuccessQueriesInSplit < mWorkloadInSplit)
				ok = false;
			LOG.debug(String.format("%6d%3d%10d%10d%10d%10d%10d", HTC.getID(),
					mSplit, mLastRemainQueries, mWorkloadInSplit,
					mRemainQueries, mSentQueriesInSplit, mSuccessQueriesInSplit));
		}
		mSplit++;
		mWorkloadInSplit = 0;
		// set to 0 means every split is not responsible for others' workload
		mRemainQueries = 0;
		mLastRemainQueries = mRemainQueries;
		mSentQueriesInSplit = 0;
		mSuccessQueriesInSplit = 0;
		return ok;
	}

	public void finishQuery() {
		mIsFinished = true;
	}

	public void startQuery() {
		mIsStarted = true;
	}

	public List<SplitResult> getSplitResults() {
		return mSplitResults;
	}

	public List<SuccessQueryResult> getSuccessQueryResults() {
		return mSuccessQueryResults;
	}
}
