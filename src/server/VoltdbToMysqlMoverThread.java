package server;

import newhybrid.HException;

public class VoltdbToMysqlMoverThread extends Thread {
	private HTenant mTenant;
	private boolean mIsInMover;
	private volatile boolean mIsShutdown = false;

	public VoltdbToMysqlMoverThread(HTenant tenant, boolean isInMover)
			throws HException {
		mTenant = tenant;
		mIsInMover = isInMover;
		if (!mTenant.isInVoltdbPure()) {
			throw new HException(
					"VoltdbToMysqlMover ERROR: already in mysql or is being moving to voltdb");
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
		synchronized (mTenant) {
			mTenant.finishMovingToMysql();
			mTenant.getServer().removeVoltdbIDForTenant(mTenant.getID());
			if (mIsInMover) {
				mTenant.getServer().completeOneMoverThread();
				mTenant.getServer().trigger();
			}
		}
	}

	public void shutdown() {
		// TODO shutdown thread for moving data
		mIsShutdown = true;
		synchronized (mTenant) {
			mTenant.cancelMoving();
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
