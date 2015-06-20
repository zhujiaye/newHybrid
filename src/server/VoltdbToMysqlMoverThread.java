package server;

import newhybrid.HException;

public class VoltdbToMysqlMoverThread extends MoverThread {

	public VoltdbToMysqlMoverThread(HTenant tenant, boolean isInMover)
			throws HException {
		super(tenant, isInMover);
		if (!mTenant.isInVoltdbPure()) {
			throw new HException(
					"VoltdbToMysqlMover ERROR: already in mysql or is being moving to voltdb");
		}
	}

	@Override
	public void run() {
		synchronized (this) {
			if (mIsFinished)
				return;
			mIsStarted = true;
		}
		// TODO start moving data
		try {
			Thread.sleep(8000);
		} catch (InterruptedException e) {
			return;
		}
		synchronized (this) {
			mIsFinished = true;
			mTenant.finishMovingToMysql();
			mTenant.getServer().removeVoltdbIDForTenant(mTenant.getID());
			if (mIsInMover) {
				mTenant.getServer().completeOneMoverThread();
				mTenant.getServer().trigger();
			}
		}
	}

	public void cancel() {
		// TODO shutdown thread for moving data
		synchronized (this) {
			if (mIsFinished)
				return;
			mIsFinished = true;
			if (mIsStarted) {
				if (mIsInMover) {
					mTenant.getServer().completeOneMoverThread();
					mTenant.getServer().trigger();
				}
				interrupt();
			}
		}
	}
}
