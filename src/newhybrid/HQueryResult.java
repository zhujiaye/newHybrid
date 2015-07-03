package newhybrid;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.voltdb.VoltTable;

import config.Constants;

public class HQueryResult {
	final static private Logger LOG = Logger.getLogger(Constants.LOGGER_NAME);
	final private long mStartTime;
	final private long mEndTime;
	final private QueryType mType;
	final private boolean mIsRead;
	final private boolean mIsInMysql;
	final private boolean mIsSuccess;
	final private String mMessage;
	private ResultSet mMysqlResult;
	private VoltTable[] mVoltdbResult;
	final private int mUpdatedCount;

	public HQueryResult(QueryType queryType, boolean isInMysql,
			boolean isSuccess, String message, long time1, long time2,
			ResultSet mysqlResult, VoltTable[] voltdbResult, int updatedCount) {
		if (queryType == null) {
			LOG.error("QueryType is null!");
		}
		mType = queryType;
		mIsInMysql = isInMysql;
		mIsRead = queryType.isRead();
		mIsSuccess = isSuccess;
		mMessage = message;
		mStartTime = time1;
		mEndTime = time2;
		mMysqlResult = mysqlResult;
		mVoltdbResult = voltdbResult;
		mUpdatedCount = updatedCount;
	}

	public boolean isSuccess() {
		return mIsSuccess;
	}

	public String getMessage() {
		return mMessage;
	}

	public boolean isRead() {
		return mIsRead;
	}

	public boolean isInMysql() {
		return mIsInMysql;
	}

	public long getStartTime() {
		return mStartTime;
	}

	public long getEndTime() {
		return mEndTime;
	}

	public long getLatency() {
		return mEndTime - mStartTime;
	}

	/**
	 * Release result resources
	 */
	public void close() {
		if (mMysqlResult != null) {
			try {
				mMysqlResult.close();
			} catch (SQLException e) {
				LOG.error("Access denied!");
			}
			mMysqlResult = null;
		}
		if (mVoltdbResult != null)
			mVoltdbResult = null;
	}

	public void print() throws HException {
		if (mType.isRead()) {
			if (mIsInMysql) {
				int n;
				try {
					if (mMysqlResult == null || mMysqlResult.isClosed()) {
						LOG.warn("MySQL result is not accessible!");
						return;
					}
					n = mMysqlResult.getMetaData().getColumnCount();
					System.out.format("%10s%10s", mMysqlResult.getMetaData()
							.getTableName(1), mType.name());
					for (int i = 1; i <= n; i++)
						System.out.format("%20s", mMysqlResult.getMetaData()
								.getColumnName(i));
					System.out.println();
					while (mMysqlResult.next()) {
						System.out.format("%20s", "");
						for (int i = 1; i <= n; i++) {
							System.out
									.format("%20s", mMysqlResult.getString(i));
						}
						System.out.println();
					}
				} catch (SQLException e) {
					throw new HException(e.getMessage());
				}
			} else {
				if (mVoltdbResult == null) {
					LOG.warn("VoltDB result is not accessible!");
					return;
				}
				if (mVoltdbResult.length > 0) {
					VoltTable table = mVoltdbResult[0];
					int n = table.getColumnCount();
					for (int i = 1; i <= n; i++)
						System.out.format("%20s", table.getColumnName(i - 1));
					System.out.println();
					while (table.advanceRow()) {
						for (int i = 1; i <= n; i++) {
							System.out.format("%20s", table.getString(i - 1));
						}
						System.out.println();
					}
				}
			}
		} else {
			if (mIsInMysql) {
				System.out
						.format("%10s write query:%d tuples inserted/updated/deleted%n",
								mType.name(), mUpdatedCount);

			} else {
				// TODO complete this part
			}
		}
	}
}
