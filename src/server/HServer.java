package server;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.voltdb.VoltTable;
import org.voltdb.client.Client;
import org.voltdb.client.ClientResponse;
import org.voltdb.client.NoConnectionsException;
import org.voltdb.client.ProcCallException;

import utillity.VoltdbConnectionPool;
import newhybrid.HException;
import config.Constants;
import config.HConfig;

/*
 * HServer maintains all the server-side information and is used to interact with clients
 */
public class HServer {
	final private HConfig mConf;

	private boolean isStarted;
	private String mAddress;
	private int mPort;
	private int mVoltdbSpaceTotal;
	private int mVoltdbSpaceAvailable;

	private Map<Integer, HTenant> mTenants;
	private Map<Integer, Integer> mTenantsVoltdbID;
	private Map<Integer, List<Integer>> mVoltdbIDList;

	public HServer() throws HException {
		mConf = HConfig.getConf();
		mAddress = mConf.getServerAddress();
		mPort = mConf.getServerPort();
		mVoltdbSpaceTotal = mConf.getVoltdbCapacity();
		mTenants = new HashMap<>();
		mTenantsVoltdbID = new HashMap<>();
		mVoltdbIDList = new HashMap<>();
		isStarted = false;
	}

	private void clearVoltdb() throws HException {
		VoltdbConnectionPool pool = new VoltdbConnectionPool();
		Client voltdbConnection = pool.getConnection();
		ClientResponse response;
		VoltTable[] tables;
		VoltTable table;
		String tableName;
		if (voltdbConnection != null) {
			try {
				response = voltdbConnection.callProcedure("@SystemCatalog",
						"TABLES");
				if (response != null
						&& response.getStatus() == ClientResponse.SUCCESS) {
					tables = response.getResults();
					for (int i = 0; i < tables.length; i++) {
						table = tables[i];
						while (table.advanceRow()) {
							tableName = table.getString("TABLE_NAME");
							response = voltdbConnection.callProcedure("@AdHoc",
									"delete from " + tableName + ";");
							if (response.getStatus() != ClientResponse.SUCCESS) {
								throw new HException("Can not delete table "
										+ tableName);
							} else {
								System.out.println("voltdb table " + tableName
										+ " cleared");
							}
						}
					}
				} else {
					throw new HException("Failed response");
				}
			} catch (IOException | ProcCallException e) {
				throw new HException(e.getMessage());
			}
		} else {
			throw new HException("Can't connect to voltdb server");
		}
		pool.clear();
	}

	public void start() throws HException {
		if (isStarted)
			return;
		isStarted = true;
		if (mConf.isUseVoltdb()) {
			clearVoltdb();
		}
		mVoltdbSpaceAvailable = mVoltdbSpaceTotal;
		mTenants.clear();
		mTenantsVoltdbID.clear();
		mVoltdbIDList.clear();
		for (int i = 1; i <= Constants.NUMBER_OF_TENANTS; i++) {
			HTenant tenant = new HTenant(this, i);
			mTenants.put(i, tenant);
		}
		// TODO be ready to interact with clients
		System.out.format("Server@%s:%d started!%n", mAddress, mPort);
	}

	public void stop() {
		if (!isStarted)
			return;
		isStarted = false;
		/*
		 * TODO move voltdb contents back to mysql and save all information
		 * needed
		 */
		System.out.format("Server@%s:%d stopped!%n", mAddress, mPort);
	}

	public String getAddress() {
		return mAddress;
	}

	public int getPort() {
		return mPort;
	}

	public boolean tenantLogin(int id) {
		HTenant tenant;
		tenant = mTenants.get(id);
		if (tenant != null) {
			tenant.login();
			return true;
		}
		return false;
	}

	public boolean tenantLogout(int id) {
		HTenant tenant;
		tenant = mTenants.get(id);
		if (tenant != null) {
			tenant.logout();
			return true;
		}
		return false;
	}

	public boolean tenantStart(int id) {
		HTenant tenant;
		tenant = mTenants.get(id);
		if (tenant != null) {
			tenant.start();
			return true;
		}
		return false;
	}

	public boolean tenantStop(int id) {
		HTenant tenant;
		tenant = mTenants.get(id);
		if (tenant != null) {
			tenant.stop();
			return true;
		}
		return false;
	}

	public int tenantGetVoltdbID(int id) {
		Integer result = mTenantsVoltdbID.get(id);
		if (result == null)
			return -1;
		return result;
	}

	public static void main(String[] args) throws HException {
		HServer server = new HServer();
		server.start();
		server.stop();
	}
}
