package client;

import org.apache.log4j.Logger;

import config.ClientConf;
import config.Constants;
import newhybrid.ClientShutdownException;
import server.ServerClient;
import thrift.NoTenantException;

/**
 * newhybrid client API
 * 
 * @author zhujiaye
 *
 */
public class Newhybrid {
	static private final Logger LOG = Logger.getLogger(Constants.LOGGER_NAME);
	private final String SERVER_ADDRESS;
	private final int SERVER_PORT;

	private ServerClient mClient;

	public Newhybrid(String server_address, int server_port) {
		SERVER_ADDRESS = server_address;
		SERVER_PORT = server_port;
		mClient = new ServerClient(SERVER_ADDRESS, SERVER_PORT);
	}

	/**
	 * create a new tenant
	 * 
	 * @return <b>ID</b> of the new created tenant
	 */
	public int createTenant() {
		try {
			return mClient.tenant_createTenant();
		} catch (ClientShutdownException e) {
			LOG.error(e.getMessage());
			mClient = new ServerClient(SERVER_ADDRESS, SERVER_PORT);
			return createTenant();
		}
	}

	/**
	 * check wherther server is started or not
	 * 
	 * @return <b>true</b> if server is started, <b>false</b> otherwise
	 */
	public boolean serverExist() {
		try {
			return mClient.serverExist();
		} catch (ClientShutdownException e) {
			LOG.error(e.getMessage());
			mClient = new ServerClient(SERVER_ADDRESS, SERVER_PORT);
			return serverExist();
		}
	}
}
