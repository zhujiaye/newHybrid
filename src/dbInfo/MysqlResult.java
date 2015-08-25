package dbInfo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class MysqlResult extends HResult {
	private int mUpdatedCount;
	private ResultSet mResult;

	protected MysqlResult(QueryType queryType, boolean success, String message, int updatedCount) {
		super(queryType, success, message);
		mUpdatedCount = updatedCount;
		mResult = null;
	}

	protected MysqlResult(QueryType queryType, boolean success, String message, ResultSet result) {
		super(queryType, success, message);
		mUpdatedCount = -1;
		mResult = result;
	}

	@Override
	public void release() throws HSQLException {
		if (mResult != null) {
			try {
				Statement stmt = mResult.getStatement();
				mResult.close();
				stmt.close();
			} catch (SQLException e) {
				throw new HSQLException("database access error:" + e.getMessage());
			} finally {
				mResult = null;
			}
		}
	}

	@Override
	public int getUpdateCount() throws HSQLException {
		if (!SUCCESS)
			throw new HSQLException("called on a failed result!");
		if (TYPE.isRead())
			return -1;
		else
			return mUpdatedCount;
	}

	@Override
	public boolean hasNext() throws HSQLException {
		if (!SUCCESS)
			throw new HSQLException("called on a failed result!");
		if (TYPE.isWrite())
			return false;
		if (mResult == null)
			throw new HSQLException("called on a released result object!");
		try {
			return mResult.next();
		} catch (SQLException e) {
			throw new HSQLException(e.getMessage());
		}
	}

	@Override
	public ArrayList<String> getColumnNames() throws HSQLException {
		if (!SUCCESS)
			throw new HSQLException("called on a failed result!");
		if (TYPE.isWrite())
			return null;
		if (mResult == null)
			throw new HSQLException("called on a released result object!");
		ArrayList<String> res = new ArrayList<>();
		try {
			int size = mResult.getMetaData().getColumnCount();
			for (int i = 1; i <= size; i++) {
				res.add(mResult.getMetaData().getColumnName(i));
			}
			return res;
		} catch (SQLException e) {
			throw new HSQLException(e.getMessage());
		}
	}

	@Override
	public ArrayList<String> getColumnValues() throws HSQLException {
		if (!SUCCESS)
			throw new HSQLException("called on a failed result!");
		if (TYPE.isWrite())
			return null;
		if (mResult == null)
			throw new HSQLException("called on a released result object!");
		ArrayList<String> res = new ArrayList<>();
		try {
			int size = mResult.getMetaData().getColumnCount();
			for (int i = 1; i <= size; i++) {
				res.add(mResult.getString(i));
			}
			return res;
		} catch (SQLException e) {
			throw new HSQLException(e.getMessage());
		}
	}

}
