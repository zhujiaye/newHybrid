package dbInfo;

import java.util.ArrayList;
import java.util.List;

import newhybrid.NoHConnectionException;
import newhybrid.Tenant;
import thrift.ColumnInfo;
import thrift.DType;
import thrift.DbmsInfo;
import thrift.DbmsType;
import thrift.TableInfo;
import thrift.TenantInfo;

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
	 * @throws HSQLException
	 *             if database access error or other reasons
	 */
	public abstract void dropTable(Table table) throws HSQLException;

	/**
	 * get all tables' name in the dbms
	 * 
	 * @return a string list containing all the tables' name
	 * @throws HSQLException
	 *             if a database access error occurs
	 */
	public abstract ArrayList<String> getAllTableNames() throws HSQLException;

	/**
	 * return whether the corresponding table exists
	 * 
	 * @param table
	 * @return <b>true</b> if exists,<b>false</b> otherwise
	 * @throws HSQLException
	 *             if a database access error occurs
	 */
	public abstract boolean tableExist(Table table) throws HSQLException;

	/**
	 * create a table if it doesn't exist
	 * 
	 * @param table
	 *            all information the new created table needed
	 * @return <b>true</b> if the table doesn't exist and was successfully
	 *         created,<b>false</b> if the table already exists
	 * @throws HSQLException
	 *             if a database access error occurs
	 */
	public abstract boolean createTable(Table table) throws HSQLException;

	/**
	 * execute a select operation randomly from a table
	 * 
	 * @param table
	 * @return HResult
	 */
	public abstract HResult doRandomSelect(Table table);

	public abstract HResult doRandomUpdate(Table table);

	public abstract HResult doRandomInsert(Table table);

	public abstract HResult doRandomDelete(Table table);

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
	 */
	public static void main(String[] args) throws NoHConnectionException, HSQLException {
		DbmsInfo info1, info2, info3, info4;
		info1 = new DbmsInfo(DbmsType.MYSQL, "jdbc:mysql://192.168.0.30/newhybrid", "remote", "remote", 0);
		info2 = new DbmsInfo(DbmsType.MYSQL, "jdbc:mysql://192.168.0.31/newhybrid", "remote", "remote", 0);
		info3 = new DbmsInfo(DbmsType.VOLTDB, "192.168.0.30", null, null, 2000);
		info4 = new DbmsInfo(DbmsType.VOLTDB, "192.168.0.31", null, null, 2000);
		HConnection hConnection = MysqlConnection.getConnection(info1);
		List<ColumnInfo> columns = new ArrayList<>();
		List<Integer> primary_key_pos = new ArrayList<>();
		columns.add(new ColumnInfo("id", DType.INT));
		columns.add(new ColumnInfo("value1", DType.INT));
		columns.add(new ColumnInfo("value2", DType.FLOAT));
		columns.add(new ColumnInfo("value3", DType.VARCHAR));
		primary_key_pos.add(0);
		TableInfo tableInfo = new TableInfo("customer", columns, primary_key_pos);
		Table table = new Table(new Tenant(new TenantInfo(1)), tableInfo);
		for (int i = 3; i <= 100; i++) {
			((MysqlConnection) hConnection).doSql("insert into customer_1 values(" + i + ",1,3.5,'T T ')");
		}
		HResult result = hConnection.doRandomSelect(table);
		if (result.isSuccess()) {
			if (result.getType().isRead()) {
				ArrayList<String> names = result.getColumnNames();
				for (int i = 0; i < names.size(); i++)
					System.out.print(names.get(i) + " ");
				System.out.println();
				while (result.hasNext()) {
					ArrayList<String> values = result.getColumnValues();
					for (int i = 0; i < values.size(); i++)
						System.out.print(values.get(i) + " ");
					System.out.println();
				}
			} else {
				System.out.println("write operation:updated count " + result.getUpdateCount());
			}
		} else {
			System.out.println(result.getMessage());
		}
	}
}
