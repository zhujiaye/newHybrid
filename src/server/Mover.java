package server;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Mover {
	final private HServer mServer;

	private int mConcurrency;
	private int mConcurrencyLimit;
	private Queue<Thread> mThreadsQueue;

	public Mover(HServer server) {
		mServer = server;
		mConcurrencyLimit = 0;
		mConcurrency = 0;
		mThreadsQueue = new LinkedList<>();
	}

	public synchronized void updateConcurrencyLimit(int concurrencyLimit) {
		mConcurrencyLimit = concurrencyLimit;
	}

	public synchronized void addThread(Thread thread) {
		mThreadsQueue.add(thread);
	}

	public synchronized void completeOne() {
		mConcurrency--;
	}

	public synchronized void trigger() {
		while (mConcurrency < mConcurrencyLimit && !mThreadsQueue.isEmpty()) {
			mConcurrency++;
			Thread thread = mThreadsQueue.poll();
			thread.start();
		}
	}

	public HServer getServer() {
		return mServer;
	}

}
