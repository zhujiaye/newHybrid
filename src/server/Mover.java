package server;

import java.util.ArrayList;

public class Mover {
	final private HServer mServer;

	private int mConcurrency;
	private int mConcurrencyLimit;
	private ArrayList<Thread> mThreadsQueue;

	public Mover(HServer server) {
		mServer = server;
		mConcurrencyLimit = 0;
		mConcurrency = 0;
		mThreadsQueue = new ArrayList<>();
	}

	public synchronized void updateConcurrencyLimit(int concurrencyLimit) {
		mConcurrencyLimit = concurrencyLimit;
	}

	public synchronized void addThread(
			MysqlToVoltdbMoverThread mysqlToVoltdbThread) {
		Thread thread = new Thread(mysqlToVoltdbThread);
		mThreadsQueue.add(thread);
	}

	public synchronized void addThread(
			VoltdbToMysqlMoverThread voltdbToMysqlThread) {
		Thread thread = new Thread(voltdbToMysqlThread);
		mThreadsQueue.add(thread);
	}

	public synchronized void completeOne() {
		mConcurrency--;
		trigger();
	}

	public synchronized void trigger() {
		while (mConcurrency < mConcurrencyLimit && !mThreadsQueue.isEmpty()) {
			mConcurrency++;
			Thread thread = mThreadsQueue.get(0);
			thread.start();
		}
	}
}
