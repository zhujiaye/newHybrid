package newhybrid;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.voltdb.VoltTable;

public class HQueryResult {
	private long mStartTime;
	private long mEndTime;
	private QueryType mType;
	private ResultSet mMysqlResult;
	private VoltTable[] mVoltdbResult;
	private int mUpdatedCount;

	public HQueryResult(QueryType queryType) {
		mType = queryType;
	}

	public HQueryResult(QueryType queryType, long time1, long time2,
			ResultSet mysqlResult, VoltTable[] voltdbResult, int updatedCount) {
		mType = queryType;
		mStartTime = time1;
		mEndTime = time2;
		mMysqlResult = mysqlResult;
		mVoltdbResult = voltdbResult;
		mUpdatedCount = updatedCount;
	}

	public boolean isSuccess() {
		if (mType == null)
			return false;
		return true;
	}

	public boolean isInMysql() {
		if (mType == null)
			return false;
		if (mType.isRead()) {
			if (mMysqlResult != null)
				return true;
			return false;
		} else {
			if (mVoltdbResult != null)
				return false;
			return true;
		}
	}

	public void print() throws HException {
		if (mType == null) {
			System.out.println("failed query");
		}
		if (mType.isRead()) {
			if (isInMysql()) {
				int n;
				try {
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
			if (isInMysql()) {
				System.out
						.format("%10s write query:%d tuples inserted/updated/deleted%n",
								mType.name(), mUpdatedCount);

			} else {
				// TODO complete this part
			}
		}
	}
}
