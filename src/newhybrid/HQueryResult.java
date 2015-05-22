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

	public void print() throws HException {
		if (mType == null) {
			System.out.println("failed query");
		}
		if (mType.isRead()) {
			if (mMysqlResult != null) {
				int n;
				try {
					n = mMysqlResult.getMetaData().getColumnCount();
					for (int i = 1; i <= n; i++)
						System.out.format("%20s", mMysqlResult.getMetaData()
								.getColumnName(i));
					System.out.println();
					while (mMysqlResult.next()) {
						for (int i = 1; i <= n; i++) {
							System.out
									.format("%20s", mMysqlResult.getString(i));
						}
						System.out.println();
					}
				} catch (SQLException e) {
					throw new HException(e.getMessage());
				}
			}
			if (mVoltdbResult.length > 0) {
				// TODO complete this part
			}
		} else {
			System.out.format("write query:%d tuples inserted/updated/deleted",
					mUpdatedCount);
		}
	}
}
