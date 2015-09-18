package dbInfo;

import java.util.ArrayList;
import java.util.List;

import newhybrid.NoHConnectionException;
import thrift.ColumnInfo;
import thrift.DType;
import thrift.DbmsException;
import thrift.DbmsInfo;
import thrift.DbmsType;
import thrift.OperationPara;
import thrift.TableInfo;
import thrift.TempTableInfo;
import thrift.TenantInfo;

/**
 * This class behaves like a DBMS and all database operations should be done by
 * this class. Also, this class has a pool implementation ,see HConnectionPool.
 * It's not thread-safe. Extend it to develop your own database connection
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
	 * do whatever the sqlString says,the sqlString only support
	 * select/insert/update/delete operations now
	 * 
	 * @param tenantID
	 *            the ID of the tenant who issues this request
	 * @param sqlString
	 * @return HResult never null
	 */
	public abstract HResult executeSql(int tenantID, String sqlString);

	/**
	 * drop a table if it exists
	 * 
	 * @param tenantID
	 * @param tableInfo
	 * @throws HSQLException
	 *             if database access error or other reasons
	 */
	public abstract void dropTable(int tenantID, TableInfo tableInfo) throws HSQLException;

	/**
	 * return whether the corresponding table exists
	 * 
	 * @param tenantID
	 * @param tableInfo
	 * @return <b>true</b> if exists,<b>false</b> otherwise
	 * @throws HSQLException
	 *             if a database access error occurs
	 */
	public abstract boolean tableExist(int tenantID, TableInfo tableInfo) throws HSQLException;

	/**
	 * create a table if it doesn't exist
	 * 
	 * @param tenantID
	 * @param tableInfo
	 * @return <b>true</b> if the table doesn't exist and was successfully
	 *         created,<b>false</b> if the table already exists
	 * @throws HSQLException
	 *             if a database access error occurs
	 */
	public abstract boolean createTable(int tenantID, TableInfo tableInfo) throws HSQLException;

	/**
	 * export the tenant's table to a file in temporary path using CSV format
	 * <b>locally</b>
	 * 
	 * @param tenantID
	 *            ID of the tenant
	 * @param tableInfo
	 *            table information about the table which will be exported
	 * @param tempPath
	 *            temporary path where the table data will be placed
	 * @throws DbmsException
	 */
	public abstract void exportTempTable(int tenantID, TableInfo tableInfo, String tempPath) throws DbmsException;

	/**
	 * import the tenant's table from a file in temporary path using CSV format
	 * <b>locally</b>
	 * 
	 * @param tenantID
	 *            ID of the tenant
	 * @param tableInfo
	 *            table information about the table which will be imported
	 * @param tempPath
	 *            temporary path where the table data placed
	 * @throws DbmsException
	 */
	public abstract void importTempTable(int tenantID, TableInfo tableInfo, String tempPath) throws DbmsException;

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

	/**
	 * just for test
	 * 
	 * @param args
	 * @throws NoHConnectionException
	 * @throws HSQLException
	 * @throws DbmsException
	 */
	public static void main(String[] args) throws NoHConnectionException, HSQLException, DbmsException {
		DbmsInfo info1, info2, info3, info4;
		HConnectionPool pool = HConnectionPool.getPool();
		info1 = new DbmsInfo(DbmsType.MYSQL, "jdbc:mysql://192.168.0.30/newhybrid", "remote", "remote", 0);
		info2 = new DbmsInfo(DbmsType.MYSQL, "jdbc:mysql://192.168.0.31/newhybrid", "remote", "remote", 0);
		info3 = new DbmsInfo(DbmsType.VOLTDB, "192.168.0.30", null, null, 2000);
		info4 = new DbmsInfo(DbmsType.VOLTDB, "192.168.0.31", null, null, 2000);
		HConnection hConnection = pool.getConnectionByDbmsInfo(info1);
		List<ColumnInfo> columns = new ArrayList<>();
		List<Integer> primary_key_pos = new ArrayList<>();
		columns.add(new ColumnInfo("id", DType.INT));
		columns.add(new ColumnInfo("value1", DType.INT));
		columns.add(new ColumnInfo("value2", DType.FLOAT));
		columns.add(new ColumnInfo("value3", DType.VARCHAR));
		primary_key_pos.add(0);
		TableInfo tableInfo = new TableInfo("stock", columns, primary_key_pos);
	}
}
