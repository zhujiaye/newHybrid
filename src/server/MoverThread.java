package server;

public class MoverThread extends Thread {
	protected final HTenant mTenant;
	protected final boolean mIsInMover;
	protected volatile boolean mIsFinished = false;
	protected volatile boolean mIsStarted = false;

	public MoverThread(HTenant tenant, boolean isInMover) {
		mTenant = tenant;
		mIsInMover = isInMover;
	}

	public synchronized void startMovingData() {
		mIsStarted = true;
	}

	public synchronized boolean isFinished() {
		return mIsFinished;
	}
}
