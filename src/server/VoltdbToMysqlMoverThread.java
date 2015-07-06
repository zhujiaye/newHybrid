package server;

import java.io.InterruptedIOException;

import org.apache.log4j.Logger;

import config.Constants;
import newhybrid.HException;

public class VoltdbToMysqlMoverThread extends MoverThread {
	final static private Logger LOG = Logger
			.getLogger(Constants.LOGGER_NAME_SERVER);

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
		try {
			new VoltdbToMysqlMover(mTenant.getID() - 1,
					mTenant.getIDInVoltdb() - 1).move();
		} catch (HException e) {
			e.printStackTrace();
			LOG.error(e.getMessage());
			return;
		} catch (InterruptedException e) {
			return;
		} catch (InterruptedIOException e) {
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
