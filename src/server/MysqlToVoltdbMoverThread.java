package server;

import org.omg.CORBA.OMGVMCID;

import newhybrid.HException;

public class MysqlToVoltdbMoverThread extends MoverThread {
	private int mVoltdbID;

	public MysqlToVoltdbMoverThread(HTenant tenant, int voltdbID,
			boolean isInMover) throws HException {
		super(tenant, isInMover);
		mVoltdbID = voltdbID;
		if (!mTenant.isInMysqlPure()) {
			throw new HException(
					"MysqlToVoltdbMover ERROR: already in voltdb or is being moving to mysql");
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
			mTenant.finishMovingToVoltdb(mVoltdbID);
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

	public int getVoltdbID() {
		return mVoltdbID;
	}
}
