package dbInfo;

public enum QueryType {
	SELECT(0), UPDATE(1), INSERT(2), DELETE(3), READ(4), WRITE(5), UNKNOWN(6), FAILED(7);

	private final int mValue;

	static public QueryType getByString(String str){
		if (str.equals("select"))
			return SELECT;
		if (str.equals("update"))
			return UPDATE;
		if (str.equals("insert"))
			return INSERT;
		if (str.equals("delete"))
			return DELETE;
		return UNKNOWN;
	}

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
