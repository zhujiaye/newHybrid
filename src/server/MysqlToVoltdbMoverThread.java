package server;

import java.io.InterruptedIOException;
import java.sql.Connection;

import org.apache.log4j.Logger;
import org.voltdb.client.Client;

import utillity.MysqlConnectionPool;
import utillity.VoltdbConnectionPool;
import config.Constants;

public class MysqlToVoltdbMoverThread extends MoverThread {
	final static private Logger LOG = Logger
			.getLogger(Constants.LOGGER_NAME_SERVER);
	private int mVoltdbID;

	public MysqlToVoltdbMoverThread(HTenant tenant, int voltdbID,
			boolean isInMover) {
		super(tenant, isInMover);
		mVoltdbID = voltdbID;
		if (!mTenant.isInMysqlPure()) {
			LOG.error("MysqlToVoltdbMover ERROR: already in voltdb or is being moving to mysql");
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
				if (mIsFinished)
					return;
				Connection mysqlConnection = null;
				Client voltdbConnection = null;
				mysqlConnection = mysqlPool.getConnection();
				voltdbConnection = voltdbPool.getConnection();
				if (mysqlConnection == null || voltdbConnection == null) {
					LOG.error("No mysql or voltdb connections!");
					return;
				}
				new MysqlToVoltdbMover(mTenant.getID() - 1, mVoltdbID - 1, i,
						mysqlConnection, voltdbConnection).move();
				mysqlPool.putConnection(mysqlConnection);
				voltdbPool.putConnection(voltdbConnection);
			}
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
				//interrupt();
			}
		}
	}

	public int getVoltdbID() {
		return mVoltdbID;
	}
}
