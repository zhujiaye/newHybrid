package dbInfo;

import thrift.DbmsInfo;

public abstract class HConnection {
	protected final DbmsInfo DBMSINFO;

	HConnection(DbmsInfo dbmsInfo) {
		DBMSINFO = dbmsInfo;
	}

	public abstract boolean isUseful();

	public abstract void release();

	public boolean match(DbmsInfo dbmsInfo) {
		if (DBMSINFO == null || dbmsInfo == null)
			return false;
		return DBMSINFO.mCompleteConnectionString == dbmsInfo.mCompleteConnectionString;
	}
}
