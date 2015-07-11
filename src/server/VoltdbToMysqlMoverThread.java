package server;

import java.io.InterruptedIOException;

import org.apache.log4j.Logger;

import config.Constants;

public class VoltdbToMysqlMoverThread extends MoverThread {
	final static private Logger LOG = Logger
			.getLogger(Constants.LOGGER_NAME_SERVER);

	private VoltdbToMysqlMover mMover;

	public VoltdbToMysqlMoverThread(HTenant tenant, boolean isInMover) {
		super(tenant, isInMover);
		if (!mTenant.isInVoltdbPure()) {
			LOG.error("VoltdbToMysqlMover ERROR: already in mysql or is being moving to voltdb");
		}
	}

	@Override
	public void run() {
		synchronized (this) {
			if (mIsFinished)
				return;
			mIsStarted = true;
		}
		try {
			mMover=new VoltdbToMysqlMover(mTenant.getID() - 1,
					mTenant.getIDInVoltdb() - 1);
			mMover.move();
		} catch (InterruptedException e) {
			LOG.error(e.getMessage());
			return;
		} catch (InterruptedIOException e) {
			LOG.error(e.getMessage());
			return;
		}

		synchronized (this) {
			if (mIsFinished)
				return;
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
		synchronized (this) {
			if (mIsFinished)
				return;
			mIsFinished = true;
			mMover.cancel();
			if (mIsStarted) {
				if (mIsInMover) {
					mTenant.getServer().completeOneMoverThread();
					mTenant.getServer().trigger();
				}
				//interrupt();
			}
		}
	}
}
