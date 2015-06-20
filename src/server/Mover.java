package server;

import java.util.LinkedList;
import java.util.Queue;

public class Mover {
	final private HServer mServer;

	private int mConcurrency;
	private int mConcurrencyLimit;
	private Queue<MoverThread> mThreadsQueue;

	public Mover(HServer server) {
		mServer = server;
		mConcurrencyLimit = 0;
		mConcurrency = 0;
		mThreadsQueue = new LinkedList<>();
	}

	public synchronized void updateConcurrencyLimit(int concurrencyLimit) {
		mConcurrencyLimit = concurrencyLimit;
	}

	public synchronized void addThread(MoverThread thread) {
		mThreadsQueue.add(thread);
	}

	public synchronized void completeOne() {
		mConcurrency--;
	}

	public synchronized void trigger() {
		while (mConcurrency < mConcurrencyLimit && !mThreadsQueue.isEmpty()) {
			MoverThread thread = mThreadsQueue.poll();
			synchronized (thread) {
				if (thread.isFinished())
					continue;
				thread.startMovingData();
				mConcurrency++;
				thread.start();
			}
		}
	}

	public HServer getServer() {
		return mServer;
	}

	public synchronized int getNumberOfRunningThreads() {
		return mConcurrency;
	}
}
