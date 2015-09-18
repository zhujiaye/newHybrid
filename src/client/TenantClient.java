package client;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import newhybrid.ClientShutdownException;
import newhybrid.NoHConnectionException;
import newhybrid.NoServerConnectionException;
import newhybrid.Pair;
import server.ServerClient;
import thrift.DbStatus;
import thrift.DbStatusInfo;
import thrift.DbmsException;
import thrift.LockException;
import thrift.NoTenantException;
import thrift.NoWorkerException;
import thrift.Operation;
import thrift.OperationPara;
import thrift.OperationType;
import thrift.SqlOperationPara;
import thrift.TableInfo;

import org.apache.log4j.Logger;
import org.voltdb.client.Client;

import com.google_voltpatches.common.base.CaseFormat;

import config.Constants;
import dbInfo.HConnection;
import dbInfo.HConnectionPool;
import dbInfo.HResult;
import dbInfo.HSQLException;
import dbInfo.QueryType;
import dbInfo.Table;

/**
 * this class stands for a tenant,all operations that a tenant will issue can be
 * accessed here
 * 
 * @author zhujiaye
 *
 */
public class TenantClient {
	static private final Logger LOG = Logger.getLogger(Constants.LOGGER_NAME);
	private final int ID;
	private final String SERVER_ADDRESS;
	private final int SERVER_PORT;
	private ServerClient mClient;

	public TenantClient(int tenant_id, String server_address, int server_port) {
		ID = tenant_id;
		SERVER_ADDRESS = server_address;
		SERVER_PORT = server_port;
		mClient = new ServerClient(SERVER_ADDRESS, SERVER_PORT);
	}

	public int getID() {
		return ID;
	}

	/**
	 * create a table for this tenant
	 * 
	 * @param tableInfo
	 *            the table information of the table which will be created
	 * @return <b>true</b> if succeeded, <b>false</b> if the table already
	 *         exists
	 * @throws NoWorkerException
	 *             if there is no worker where the table can be created
	 * @throws NoTenantException
	 *             if this tenant does not exist
	 * @throws NoServerConnectionException
	 */
	public boolean createTable(TableInfo tableInfo)
			throws NoWorkerException, NoTenantException, NoServerConnectionException {
		try {
			return mClient.tenant_createTable(ID, tableInfo);
		} catch (ClientShutdownException e) {
			LOG.error(e.getMessage());
			mClient = new ServerClient(SERVER_ADDRESS, SERVER_PORT);
			return createTable(tableInfo);
		}
	}

	/**
	 * log in this tenant
	 * 
	 * @return <b>true</b> if succeeded, <b>false</b> if the tenant is already
	 *         logged in
	 * @throws NoTenantException
	 *             if this tenant does not exist
	 * @throws NoServerConnectionException
	 */
	public boolean login() throws NoTenantException, NoServerConnectionException {
		try {
			return mClient.tenant_login(ID);
		} catch (ClientShutdownException e) {
			LOG.error(e.getMessage());
			mClient = new ServerClient(SERVER_ADDRESS, SERVER_PORT);
			return login();
		}
	}

	/**
	 * log out this tenant
	 * 
	 * @return <b>true</b> if succeeded, <b>false</b> if the tenant is already
	 *         logged out
	 * @throws NoTenantException
	 *             if this tenant does not exist
	 * @throws NoServerConnectionException
	 */
	public boolean logout() throws NoTenantException, NoServerConnectionException {
		try {
			return mClient.tenant_logout(ID);
		} catch (ClientShutdownException e) {
			LOG.error(e.getMessage());
			mClient = new ServerClient(SERVER_ADDRESS, SERVER_PORT);
			return logout();
		}
	}

	public List<TableInfo> getTables() throws NoTenantException, NoServerConnectionException {
		try {
			return mClient.tenant_getTables(ID);
		} catch (ClientShutdownException e) {
			LOG.error(e.getMessage());
			mClient = new ServerClient(SERVER_ADDRESS, SERVER_PORT);
			return getTables();
		}
	}

	public List<TableInfo> getTable(String tableName) throws NoTenantException, NoServerConnectionException {
		try {
			return mClient.tenant_getTable(ID, tableName);
		} catch (ClientShutdownException e) {
			LOG.error(e.getMessage());
			mClient = new ServerClient(SERVER_ADDRESS, SERVER_PORT);
			return getTable(tableName);
		}
	}

	public void dropAllTables() throws NoTenantException, NoWorkerException, NoServerConnectionException {
		try {
			mClient.tenant_dropAllTables(ID);
		} catch (ClientShutdownException e) {
			LOG.error(e.getMessage());
			mClient = new ServerClient(SERVER_ADDRESS, SERVER_PORT);
			dropAllTables();
		}
	}

	public void dropTable(String tableName) throws NoTenantException, NoWorkerException, NoServerConnectionException {
		try {
			mClient.tenant_dropTable(ID, tableName);
		} catch (ClientShutdownException e) {
			LOG.error(e.getMessage());
			mClient = new ServerClient(SERVER_ADDRESS, SERVER_PORT);
			dropTable(tableName);
		}
	}

	private void addOperationToMigrator(int ID, Operation operation) throws NoServerConnectionException {
		try {
			mClient.addOperationToMigrator(ID, operation);
		} catch (ClientShutdownException e) {
			LOG.error(e.getMessage());
			mClient = new ServerClient(SERVER_ADDRESS, SERVER_PORT);
			addOperationToMigrator(ID, operation);
		}
	}

	public DbStatusInfo getDbStatusInfo() throws NoWorkerException, NoTenantException, NoServerConnectionException {
		try {
			return mClient.tenant_getDbInfo(ID);
		} catch (ClientShutdownException e) {
			LOG.error(e.getMessage());
			mClient = new ServerClient(SERVER_ADDRESS, SERVER_PORT);
			return getDbStatusInfo();
		}
	}

	/**
	 * execute a sql command for this tenant,sql command only supports
	 * select/insert/update/delete now
	 * 
	 * @param sqlString
	 * @return
	 * @throws NoWorkerException
	 * @throws NoTenantException
	 * @throws DbmsException
	 * @throws NoServerConnectionException
	 */
	public HResult executeSql(String sqlString)
			throws NoWorkerException, NoTenantException, DbmsException, NoServerConnectionException {
		DbStatusInfo dbInfo = getDbStatusInfo();
		HResult result = null;
		if (dbInfo.mDbStatus == DbStatus.NORMAL || dbInfo.mDbStatus == DbStatus.MIGRATING) {
			HConnectionPool pool = HConnectionPool.getPool();
			HConnection hConnection = null;
			try {
				hConnection = pool.getConnectionByDbmsInfo(dbInfo.mDbmsInfo);
				result = hConnection.executeSql(ID, sqlString);
				if (result.isSuccess() && dbInfo.mDbStatus == DbStatus.MIGRATING) {
					if (sqlString.indexOf("select") == -1) {
						SqlOperationPara sqlParas = new SqlOperationPara(ID, sqlString);
						OperationPara paras = new OperationPara();
						paras.setMSqlOpPara(sqlParas);
						addOperationToMigrator(ID, new Operation(OperationType.EXECUTE_SQL, paras));
					}
				}
			} catch (NoHConnectionException e) {
				throw new DbmsException(e.getMessage());
			} finally {
				pool.putConnection(hConnection);
			}
		} else {
			result = null;
		}
		return result;
	}

	public HResult selectRandomly()
			throws NoTenantException, NoServerConnectionException, NoWorkerException, DbmsException {
		List<TableInfo> tablesInfo = getTables();
		Random random = new Random(System.nanoTime());
		TableInfo randomTableInfo = tablesInfo.get(random.nextInt(tablesInfo.size()));
		Table randomTable = new Table(randomTableInfo);
		return executeSql(randomTable.generateRandomSelectString());
	}

	public HResult updateRandomly()
			throws NoTenantException, NoServerConnectionException, NoWorkerException, DbmsException {
		List<TableInfo> tablesInfo = getTables();
		Random random = new Random(System.nanoTime());
		TableInfo randomTableInfo = tablesInfo.get(random.nextInt(tablesInfo.size()));
		Table randomTable = new Table(randomTableInfo);
		return executeSql(randomTable.generateRandomUpdateString());
	}

	public HResult insertRandomly()
			throws NoTenantException, NoServerConnectionException, NoWorkerException, DbmsException {
		List<TableInfo> tablesInfo = getTables();
		Random random = new Random(System.nanoTime());
		TableInfo randomTableInfo = tablesInfo.get(random.nextInt(tablesInfo.size()));
		Table randomTable = new Table(randomTableInfo);
		return executeSql(randomTable.generateRandomInsertString());
	}

	public HResult deleteRandomly()
			throws NoTenantException, NoServerConnectionException, NoWorkerException, DbmsException {
		List<TableInfo> tablesInfo = getTables();
		Random random = new Random(System.nanoTime());
		TableInfo randomTableInfo = tablesInfo.get(random.nextInt(tablesInfo.size()));
		Table randomTable = new Table(randomTableInfo);
		return executeSql(randomTable.generateRandomDeleteString());
	}

	public void lock_lock() throws NoTenantException, LockException, NoServerConnectionException {
		try {
			mClient.tenant_lock_lock(ID);
		} catch (ClientShutdownException e) {
			LOG.error(e.getMessage());
			mClient = new ServerClient(SERVER_ADDRESS, SERVER_PORT);
			lock_lock();
		}
	}

	public void lock_release() throws NoServerConnectionException {
		try {
			mClient.tenant_lock_release(ID);
		} catch (ClientShutdownException e) {
			LOG.error(e.getMessage());
			mClient = new ServerClient(SERVER_ADDRESS, SERVER_PORT);
			lock_release();
		}
	}

	static public void main(String[] args) throws NoWorkerException, NoTenantException, DbmsException, HSQLException,
			IOException, InterruptedException {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					TenantClient client = new TenantClient(3, "192.168.0.35", 12345);
					client.lock_lock();
					HResult result = client.executeSql("select * from stock");
					if (result.isSuccess()) {
						result.print(System.out);
					} else
						System.out.println(result.getMessage());
					Thread.sleep(5000);
					client.lock_release();
				} catch (NoWorkerException | NoTenantException | DbmsException | HSQLException | InterruptedException
						| LockException | NoServerConnectionException e) {
					LOG.error(e.getMessage());
				}
			}
		}).start();
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					TenantClient client = new TenantClient(3, "192.168.0.35", 12345);
					client.lock_lock();
					HResult result = client.executeSql("select * from stock");
					client.lock_release();
					if (result.isSuccess()) {
						result.print(System.out);
					} else
						System.out.println(result.getMessage());
					Thread.sleep(5000);
					client.lock_release();
				} catch (NoWorkerException | NoTenantException | DbmsException | HSQLException | InterruptedException
						| LockException | NoServerConnectionException e) {
					LOG.error(e.getMessage());
				}
			}
		}).start();
	}
}
