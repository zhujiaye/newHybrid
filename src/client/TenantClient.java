package client;

import java.sql.Connection;
import java.sql.SQLException;
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

	public boolean createTable(TableInfo tableInfo) throws NoWorkerException, NoTenantException {
		try {
			return mClient.tenant_createTable(ID, tableInfo);
		} catch (ClientShutdownException e) {
			LOG.error(e.getMessage());
			mClient = new ServerClient(SERVER_ADDRESS, SERVER_PORT);
			return createTable(tableInfo);
		}
	}
}
