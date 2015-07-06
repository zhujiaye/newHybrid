package server;

import java.io.InterruptedIOException;
import java.sql.Connection;

import org.apache.log4j.Logger;
import org.voltdb.client.Client;

import utillity.MysqlConnectionPool;
import utillity.VoltdbConnectionPool;
import config.Constants;
import newhybrid.HException;

public class MysqlToVoltdbMoverThread extends MoverThread {
	final static private Logger LOG = Logger
			.getLogger(Constants.LOGGER_NAME_SERVER);
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
		try {
			MysqlConnectionPool mysqlPool = MysqlConnectionPool.getPool();
			VoltdbConnectionPool voltdbPool = VoltdbConnectionPool.getPool();
			for (int i = 0; i < Constants.NUMBER_OF_TABLES; i++) {
				Connection mysqlConnection = mysqlPool.getConnection();
				Client voltdbConnection = voltdbPool.getConnection();
				new MysqlToVoltdbMover(mTenant.getID() - 1, mVoltdbID - 1, i,
						mysqlConnection, voltdbConnection).move();
				mysqlPool.putConnection(mysqlConnection);
				voltdbPool.putConnection(voltdbConnection);
			}
		} catch (InterruptedException e) {
			return;
		} catch (InterruptedIOException e) {
			return;
		} catch (HException e) {
			LOG.error(e.getMessage());
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
