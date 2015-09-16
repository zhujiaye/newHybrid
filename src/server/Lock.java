package server;

public class Lock {
	private boolean mIsUsed = false;
	private Object mLock = new Object();

	public void lock() throws InterruptedException {
		synchronized (mLock) {
			while (mIsUsed){
				mLock.wait();
			}
			mIsUsed=true;
		}
	}

	public void release() {
		synchronized (mLock) {
			mIsUsed=false;
			mLock.notifyAll();
		}
	}
}
