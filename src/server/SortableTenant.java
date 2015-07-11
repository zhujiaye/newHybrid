package server;

public class SortableTenant implements Comparable<SortableTenant> {

	final private int mID;
	final private int mDataSize;
	final private int mWorkload;

	public SortableTenant(int id, int dataSize, int workload) {
		mID = id;
		mDataSize = dataSize;
		mWorkload = workload;
	}

	public int getID() {
		return mID;
	}

	public int getDataSize() {
		return mDataSize;
	}

	public int getWorkload() {
		return mWorkload;
	}

	@Override
	public int compareTo(SortableTenant o) {
		int x1 = this.getWorkload();
		int y1 = this.getDataSize();
		int x2 = o.getWorkload();
		int y2 = o.getDataSize();

		return x2 * y1 - x1 * y2;
	}
}
