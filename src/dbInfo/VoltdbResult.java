package dbInfo;

import java.util.ArrayList;

import org.voltdb.VoltTable;
import org.voltdb.VoltType;

public class VoltdbResult extends HResult {
	private int mUpdatedCount;
	private VoltTable mResult;

	public VoltdbResult(QueryType queryType, boolean success, String message, VoltTable result) {
		super(queryType, success, message);
		mResult = result;
		mUpdatedCount = -1;
	}

	public VoltdbResult(QueryType queryType, boolean success, String message, int updatedCount) {
		super(queryType, success, message);
		mResult = null;
		mUpdatedCount = updatedCount;
	}

	@Override
	public void release() {
		mResult = null;
	}

	@Override
	public int getUpdateCount() throws HSQLException {
		if (!SUCCESS)
			throw new HSQLException("called on a failed result!");
		if (TYPE.isRead())
			return -1;
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
		return mResult.advanceRow();
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
		int size = mResult.getColumnCount();
		for (int i = 0; i < size; i++)
			res.add(mResult.getColumnName(i));
		return res;
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
		int size = mResult.getColumnCount();
		for (int i = 0; i < size; i++) {
			VoltType type = mResult.getColumnType(i);
			res.add(String.valueOf(mResult.get(i, type)));
		}
		return res;
	}

}
