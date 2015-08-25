package dbInfo;

import java.util.ArrayList;

import org.voltdb.VoltTable;

public class VoltdbResult extends HResult {
	private int mUpdatedCount;
	private VoltTable mResult;

	protected VoltdbResult(QueryType queryType, boolean success, String message, VoltTable[] result) {
		super(queryType, success, message);
		if (TYPE == QueryType.SELECT) {
			mUpdatedCount = -1;
			mResult = result[0];
		} else {
			mResult = null;
			// TODO get updated count according to the VoltTable result
		}
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
		for (int i = 0; i < size; i++)
			res.add(mResult.getString(i));
		return res;
	}

}
