package dbInfo;

public enum QueryType {
	SELECT(0), UPDATE(1), INSERT(2), DELETE(3), READ(4), WRITE(5);

	private final int mValue;

	private QueryType(int value) {
		mValue = value;
	}

	public int getValue() {
		return mValue;
	}

	public boolean isRead() {
		return mValue == SELECT.mValue || mValue == READ.mValue;
	}

	public boolean isWrite() {
		return mValue == UPDATE.mValue || mValue == INSERT.mValue || mValue == DELETE.mValue || mValue == WRITE.mValue;
	}
}
