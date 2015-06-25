package test;

import java.util.ArrayList;
import java.util.Random;

import newhybrid.HException;
import newhybrid.HQueryResult;
import newhybrid.TenantWorkload;

import org.apache.log4j.Logger;

import client.HTenantClient;
import config.Constants;
import utillity.WorkloadLoader;

public class TestMain {
	final static private Logger LOG = Logger.getLogger(Constants.LOGGER_NAME);

	public static void main(String[] args) throws HException {

		if (args.length < 2) {
			LOG.error("not enough arguments!");
			return;
		}
		WorkloadLoader workloadLoader;
		if (args.length < 3) {
			workloadLoader = new WorkloadLoader(Constants.WORKLOAD_FILE_PATH);
		} else {
			workloadLoader = new WorkloadLoader(Constants.WORKLOAD_DIR + "/"
					 +args[2]);
		}
		if (!workloadLoader.load()) {
			LOG.error("no workload file!");
			return;
		} else {
			int start = Integer.valueOf(args[0]);
			int end = Integer.valueOf(args[1]);
			if (start > end) {
				LOG.error("invalid arguments!");
			}
			ClientThread[] clientThreads = new ClientThread[end - start + 1];
			Object obj = new Object();
			for (int i = start; i <= end; i++) {
				clientThreads[i - start] = new ClientThread(i, workloadLoader
						.getWorkloadForTenant(i).getWH(), obj);
			}
			LOG.info(String.format("Test from tenant %d to %d starts", start,
					end));
			int totThreads = clientThreads.length;
			int splits = workloadLoader.getNumberOfSplits();
			// TODO do login here to try to make sure all clients start querying
			// at
			// the same time
			for (int i = 0; i < totThreads; i++) {
				clientThreads[i].start();
			}
			for (int i = 0; i < splits; i++) {
				for (int j = 0; j < totThreads; j++) {
					clientThreads[j].updateSplit();
					// TODO add workloads for every clients
				}
				try {
					Thread.sleep(Constants.SPLIT_TIME / 1000000);
				} catch (InterruptedException e) {
					LOG.error("Interrupted while sleeping in split " + i + ":"
							+ e.getMessage());
					return;
				}
			}
			for (int i = 0; i < totThreads; i++) {
				clientThreads[i].updateSplit();
				clientThreads[i].shutdown();
			}
			try {
				for (int i = 0; i < totThreads; i++)
					clientThreads[i].join();
			} catch (InterruptedException e) {
				LOG.error("Interrupted while waiting client threads to die:"
						+ e.getMessage());
				return;
			}
			for (int i = 0; i < totThreads; i++)
				clientThreads[i].printResults();
			LOG.info(String
					.format("Test from tenant %d to %d ends", start, end));
		}
	}
}

class ClientThread extends Thread {
	final static private Logger LOG = Logger.getLogger(Constants.LOGGER_NAME);
	final private HTenantClient HTC;
	final private int mWH;

	private Object mObj;
	private int mRemainQueries;
	private int mLastRemainQueries;
	private int mSentQueriesInSplit;
	private int mSuccessQueriesInSplit;
	private int mWorkloadInSplit;
	private int mSplit;
	private volatile boolean mIsShutDown = false;
	private ArrayList<SplitResult> res = null;

	public ClientThread(int ID, int WH, Object obj) throws HException {
		HTC = new HTenantClient(ID);
		mWH = WH;
		mObj = obj;
		mRemainQueries = 0;
		mSentQueriesInSplit = 0;
		mSuccessQueriesInSplit = 0;
		mWorkloadInSplit = 0;
		mSplit = -1;
		res = new ArrayList<>();
	}

	@Override
	public void run() {
		Random random = new Random(System.currentTimeMillis());
		HQueryResult result = null;
		try {
			while (!mIsShutDown) {
				synchronized (this) {
					if (mRemainQueries == 0) {
						synchronized (mObj) {
							mObj.wait();
						}
					} else {
						// if (random.nextInt(100) < 80)
						// result = HTC.sqlRandomSelect();
						// else
						// result = HTC.sqlRandomUpdate();
						synchronized (this) {
							mSentQueriesInSplit++;
							mRemainQueries--;
							// if (result.isSuccess())
							mSuccessQueriesInSplit++;
						}
					}
				}
			}
			// } catch (HException e) {
			// LOG.error(e.getMessage());
			// mIsShutDown = true;
		} catch (InterruptedException e) {
			if (!mIsShutDown) {
				LOG.error("Interrupted while waiting!:" + e.getMessage());
				mIsShutDown = true;
			}
		}
	}

	public synchronized void addWorkload(int num) {
		mWorkloadInSplit += num;
		mRemainQueries += num;
	}

	public synchronized void updateSplit() {
		if (mSplit != -1) {
			SplitResult splitResult = new SplitResult(mSplit,
					mLastRemainQueries, mWorkloadInSplit, mRemainQueries,
					mSentQueriesInSplit, mSuccessQueriesInSplit);
			res.add(splitResult);
		}
		mSplit++;
		mWorkloadInSplit = 0;
		mLastRemainQueries = mRemainQueries;
		mSentQueriesInSplit = 0;
		mSuccessQueriesInSplit = 0;
	}

	public void shutdown() {
		mIsShutDown = true;
		this.interrupt();
	}

	public void printResults() {
		// TODO
	}
}

class SplitResult {
	final private int mSplitID;
	final private int mRemainQueriesBefore;
	final private int mWorkload;
	final private int mRemainQueriesAfter;
	final private int mSentQueries;
	final private int mSuccessQueries;

	public SplitResult(int splitID, int remainQueriesBefore, int workload,
			int remainQueriesAfter, int sentQueries, int successQueries) {
		mSplitID = splitID;
		mRemainQueriesBefore = remainQueriesBefore;
		mRemainQueriesAfter = remainQueriesAfter;
		mWorkload = workload;
		mSentQueries = sentQueries;
		mSuccessQueries = successQueries;
	}

	public int getID() {
		return mSplitID;
	}

	public int getRemainQueriesBefore() {
		return mRemainQueriesBefore;
	}

	public int getWorkload() {
		return mWorkload;
	}

	public int getRemainQueriesAfter() {
		return mRemainQueriesAfter;
	}

	public int getSentQueries() {
		return mSentQueries;
	}

	public int getSuccessQueries() {
		return mSuccessQueries;
	}
}
