package dbInfo;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import newhybrid.NoHConnectionException;
import thrift.ColumnInfo;
import thrift.DType;
import thrift.DbmsInfo;
import thrift.DbmsType;
import thrift.TableInfo;
import thrift.TenantInfo;
import utillity.HConnectionPool;

/**
 * all DML(e.g select, update, insert, delete) operation results are instances
 * of HQueryResult
 * 
 * @author zhujiaye
 *
 */
public abstract class HResult {

	protected final QueryType TYPE;
	protected final boolean SUCCESS;
	protected final String MESSAGE;

	protected HResult(QueryType queryType, boolean success, String message) {
		TYPE = queryType;
		SUCCESS = success;
		MESSAGE = message;
	}

	public QueryType getType() {
		return TYPE;
	}

	public boolean isSuccess() {
		return SUCCESS;
	}

	public String getMessage() {
		return MESSAGE;
	}

	/**
	 * release result resources
	 * 
	 * @throws HSQLException
	 *             if a database access error occurs
	 */
	public abstract void release() throws HSQLException;

	/**
	 * get current result as a count number
	 * 
	 * @return the number of tuples updated if the current result comes from a
	 *         update, insert, delete operation, or <b>-1</b> if the current
	 *         result comes from a read operation
	 * @throws HSQLException
	 *             if called on a failed result object
	 */
	public abstract int getUpdateCount() throws HSQLException;

	/**
	 * test whether there are more rows, if so, move the current position one
	 * row forward. Initially, the position is before the first row.
	 * 
	 * @return <b>true</b> if there are more rows,<b>false</b> if there are no
	 *         more rows or if the current result doesn't come from a read
	 *         operation
	 * @throws HSQLException
	 *             if a database access error occurs or is called on a
	 *             failed|released|closed result object
	 */
	public abstract boolean hasNext() throws HSQLException;

	/**
	 * get all the column names for the table where the result comes from
	 * 
	 * @return a string list containing all the column names,<b>null</b> if the
	 *         result doesn't come from a read operation
	 * @throws HSQLException
	 *             if a database access error occurs or is called on a
	 *             failed|released|closed result object
	 */
	public abstract ArrayList<String> getColumnNames() throws HSQLException;

	/**
	 * get all the column values for the current row of the current result
	 * 
	 * @return a string list containing all the column values,<b>null</b> if the
	 *         result doesn't come from a read operation
	 * @throws HSQLException
	 *             if a database access error occurs or is called on a
	 *             failed|released|closed result object
	 */
	public abstract ArrayList<String> getColumnValues() throws HSQLException;

	/**
	 * just for test
	 * 
	 * @param args
	 * @throws InterruptedException
	 * @throws NoHConnectionException
	 * @throws HSQLException
	 */
	public static void main(String[] args) throws InterruptedException, NoHConnectionException, HSQLException {
		DbmsInfo info1, info2, info3, info4;
		info1 = new DbmsInfo(DbmsType.MYSQL, "jdbc:mysql://192.168.0.30/newhybrid", "remote", "remote", 0);
		info2 = new DbmsInfo(DbmsType.MYSQL, "jdbc:mysql://192.168.0.31/newhybrid", "remote", "remote", 0);
		info3 = new DbmsInfo(DbmsType.VOLTDB, "192.168.0.30", null, null, 2000);
		info4 = new DbmsInfo(DbmsType.VOLTDB, "192.168.0.31", null, null, 2000);
		MysqlConnection hConnection = (MysqlConnection) MysqlConnection.getConnection(info1);
		HResult result = null;
		result = hConnection.doSql("show tables");
		if (result != null) {
			if (result.isSuccess()) {
				if (result.getType().isRead()) {
					ArrayList<String> columnNames = result.getColumnNames();
					int columns = columnNames.size();
					for (int i = 0; i < columns; i++)
						System.out.print(" " + columnNames.get(i));
					System.out.println();
					ArrayList<String> columnValues = null;
					while (result.hasNext()) {
						columnValues = result.getColumnValues();
						for (int i = 0; i < columns; i++)
							System.out.print(" " + columnValues.get(i));
						System.out.println();
					}
				} else {
					System.out.println("updated count:" + result.getUpdateCount());
				}
			} else {
				System.out.println("failed message:" + result.getMessage());
			}
			result.release();
		}
	}
}
