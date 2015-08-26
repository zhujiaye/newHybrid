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

	/**
	 * tell whether the connection is useful to operate
	 * 
	 * @return <b>true</b> if the connection is useful,<b>false</b> if the
	 *         connection is not valid anymore
	 */
	public abstract boolean isUseful();

	/**
	 * try to release resources of the connection
	 * 
	 * @throws HSQLException
	 *             if a database access error occurs
	 * 
	 */
	public abstract void release() throws HSQLException;

	/**
	 * drop all tenants' tables if they exist,this will remove all the data,be
	 * careful to use it
	 * 
	 * 
	 * @throws HSQLException
	 *             if database access error or called on a closed connection or
	 *             only a part of tables were dropped
	 */
	public abstract void dropAll() throws HSQLException;

	/**
	 * drop a table if it exists
	 * 
	 * @param table
	 */
	public abstract void dropTable(Table table);

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
