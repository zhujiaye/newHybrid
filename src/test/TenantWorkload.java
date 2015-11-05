package test;

public class TenantWorkload {
	final private int mID;
	final private int mSLO;
	final private int mDataSize;
	final private int mWH;
	final private int mSplits;
	private int[] mActualWorkloads;
	private int[] mEstimatedWorkloads;
	private boolean[] mIsActive;

	public TenantWorkload(int ID, int SLO, int dataSize, int WH, int splits) {
		mID = ID;
		mSLO = SLO;
		mDataSize = dataSize;
		mWH = WH;
		mSplits = splits;
		mActualWorkloads = new int[mSplits];
		mEstimatedWorkloads = new int[mSplits];
		mIsActive = new boolean[mSplits];
		for (int i = 0; i < mSplits; i++) {
			mActualWorkloads[i] = mEstimatedWorkloads[i] = 0;
			mIsActive[i] = false;
		}
	}

	public int getID() {
		return mID;
	}

	public int getSLO() {
		return mSLO;
	}

	public int getDataSize() {
		return mDataSize;
	}

	public int getWH() {
		return mWH;
	}

	public int getSplits() {
		return mSplits;
	}

	/**
	 * 
	 * @param split
	 *            0-index based
	 * @return
	 */
	public int getActualWorkloadAtSplit(int split) {
		if (split < 0 || split >= mSplits) {
			return 0;
		}
		return mActualWorkloads[split];
	}

	public int getEstimatedWorkloadAtSplit(int split) {
		if (split < 0 || split >= mSplits) {
			return 0;
		}
		return mEstimatedWorkloads[split];
	}

	public void setWorkloadAtSplit(int split, int actualWorkload,
			int estimatedWorkload) {
		if (split < 0 || split >= mSplits) {
			return;
		}
		mActualWorkloads[split] = actualWorkload;
		mEstimatedWorkloads[split] = estimatedWorkload; 
	}

	public boolean isActiveAtSplit(int split) {
		if (split < 0 || split >= mSplits) {
			return false;
		}
		return mIsActive[split];
	}

	public void setActiveAtSplit(int split, boolean isActive) {
		if (split < 0 || split >= mSplits) {
			return;
		}
		mIsActive[split] = isActive;
	}
}
