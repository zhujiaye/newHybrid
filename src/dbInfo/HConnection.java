package dbInfo;

import thrift.DbmsInfo;

/**
 * This class behaves like a DBMS and all database operations should be done by
 * this class. Also, this class has a pool implementation ,see HConnectionPool.
 * It's not thread-safe.
 * 
 * @author zhujiaye
 *
 */
public abstract class HConnection {
	protected final DbmsInfo DBMSINFO;

	protected HConnection(DbmsInfo dbmsInfo) {
		DBMSINFO = dbmsInfo;
	}

	public abstract boolean isUseful();

	public abstract void release();

	/**
	 * drop all tenants' table,this will remove all the data,be careful to use
	 * it
	 */
	public abstract void dropAll();

	public abstract HResult doRandomSelect(Table table);

	public abstract HResult doRandomUpdate(Table table);

	public boolean match(DbmsInfo dbmsInfo) {
		if (DBMSINFO == null || dbmsInfo == null)
			return false;
		return DBMSINFO.mCompleteConnectionString.equals(dbmsInfo.mCompleteConnectionString);
	}

	/**
	 * just for test,do not use it
	 */
	public void print() {
		System.out.println("----------------------------------");
		System.out.println(DBMSINFO.mType);
		System.out.println(DBMSINFO.mCompleteConnectionString);
		System.out.println(DBMSINFO.mMysqlUsername);
		System.out.println(DBMSINFO.mMysqlPassword);
		System.out.println(DBMSINFO.mVoltdbCapacityMB);
	}
}
