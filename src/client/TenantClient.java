package client;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;

import newhybrid.ClientShutdownException;
import newhybrid.NoServerConnectionException;
import server.ServerClient;
import thrift.NoTenantException;
import thrift.NoWorkerException;
import thrift.TableInfo;

import org.apache.log4j.Logger;
import org.voltdb.client.Client;

import config.Constants;

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
	 */
	public boolean createTable(TableInfo tableInfo) throws NoWorkerException, NoTenantException {
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
	 */
	public boolean login() throws NoTenantException {
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
	 */
	public boolean logout() throws NoTenantException {
		try {
			return mClient.tenant_logout(ID);
		} catch (ClientShutdownException e) {
			LOG.error(e.getMessage());
			mClient = new ServerClient(SERVER_ADDRESS, SERVER_PORT);
			return logout();
		}
	}

	public List<TableInfo> getTables() throws NoTenantException {
		try {
			return mClient.tenant_getTables(ID);
		} catch (ClientShutdownException e) {
			LOG.error(e.getMessage());
			mClient = new ServerClient(SERVER_ADDRESS, SERVER_PORT);
			return getTables();
		}
	}

	public List<TableInfo> getTable(String tableName) throws NoTenantException {
		try {
			return mClient.tenant_getTable(ID, tableName);
		} catch (ClientShutdownException e) {
			LOG.error(e.getMessage());
			mClient = new ServerClient(SERVER_ADDRESS, SERVER_PORT);
			return getTable(tableName);
		}
	}

	public void dropAllTables() throws NoTenantException, NoWorkerException {
		try {
			mClient.tenant_dropAllTables(ID);
		} catch (ClientShutdownException e) {
			LOG.error(e.getMessage());
			mClient = new ServerClient(SERVER_ADDRESS, SERVER_PORT);
			dropAllTables();
		}
	}

	public void dropTable(String tableName) throws NoTenantException, NoWorkerException {
		try {
			mClient.tenant_dropTable(ID, tableName);
		} catch (ClientShutdownException e) {
			LOG.error(e.getMessage());
			mClient = new ServerClient(SERVER_ADDRESS, SERVER_PORT);
			dropTable(tableName);
		}
	}
}
