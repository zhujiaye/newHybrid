package newhybrid;

public class TenantWorkload {
	final private int mID;
	final private int mSLO;
	final private int mDataSize;
	final private int mWH;
	final private int mSplits;
	private int[] mWorkloads;
	private boolean[] mIsActive;

	public TenantWorkload(int ID, int SLO, int dataSize, int WH, int splits) {
		mID = ID;
		mSLO = SLO;
		mDataSize = dataSize;
		mWH = WH;
		mSplits = splits;
		mWorkloads = new int[mSplits];
		mIsActive = new boolean[mSplits];
		for (int i = 0; i < mSplits; i++) {
			mWorkloads[i] = 0;
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
		return mWorkloads[split];
	}

	/*
	 * TODO This method is wrong
	 */
	private int getEstimatedWorkloadAtSplit(int split) {
		if (split < 0 || split >= mSplits) {
			return 0;
		}
		if (mIsActive[split])
			return mSLO;
		else
			return 0;
	}

	public void setWorkloadAtSplit(int split, int workload) {
		if (split < 0 || split >= mSplits) {
			return;
		}
		mWorkloads[split] = workload;
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
