package server;

import newhybrid.HException;

public class MysqlToVoltdbMoverThread extends Thread {
	final private HTenant mTenant;
	private int mVoltdbID;
	private boolean mIsInMover;
	private volatile boolean mIsShutdown = false;

	public MysqlToVoltdbMoverThread(HTenant tenant, int voltdbID,
			boolean isInMover) throws HException {
		mTenant = tenant;
		mVoltdbID = voltdbID;
		mIsInMover = isInMover;
		if (!mTenant.isInMysqlPure()) {
			throw new HException(
					"MysqlToVoltdbMover ERROR: already in voltdb or is being moving to mysql");
		}
	}

	@Override
	public void run() {
		if (mIsShutdown) {
			return;
		}
		// TODO start moving data
		try {
			Thread.sleep(8000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		synchronized (this) {
			mTenant.finishMovingToVoltdb(mVoltdbID);
			if (mIsInMover) {
				mTenant.getServer().completeOneMoverThread();
				mTenant.getServer().trigger();
			}
		}
	}

	public void shutdown() {
		// TODO shutdown thread for moving data
		synchronized (this) {
			mTenant.cancelMoving();
			mTenant.getServer().removeVoltdbIDForTenant(mTenant.getID());
			mIsShutdown = true;
			if (isAlive()) {
				if (mIsInMover) {
					mTenant.getServer().completeOneMoverThread();
					mTenant.getServer().trigger();
				}
				interrupt();
			}
		}
	}
}
