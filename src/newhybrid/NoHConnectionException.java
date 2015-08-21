package newhybrid;

import thrift.DbmsInfo;

public class NoHConnectionException extends Exception {

	private DbmsInfo mDbmsInfo;

	public NoHConnectionException(DbmsInfo dbmsInfo, String message) {
		super(message);
		mDbmsInfo = dbmsInfo;
	}

	public DbmsInfo getDbmsInfo(){
		return mDbmsInfo;
	}

}
