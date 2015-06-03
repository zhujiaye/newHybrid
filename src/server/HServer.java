package server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadedSelectorServer;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TTransportException;
import org.voltdb.VoltTable;
import org.voltdb.client.Client;
import org.voltdb.client.ClientResponse;
import org.voltdb.client.ProcCallException;

import thrift.ServerService;
import utillity.VoltdbConnectionPool;
import newhybrid.HException;
import config.Constants;
import config.HConfig;

/*
 * HServer maintains all the server-side information and is used to interact with clients
 */
public class HServer {
	final private HConfig mConf;
	final private String mAddress;
	final private int mPort;
	final private int mVoltdbSpaceTotal;

	private boolean isStarted;
	private int mVoltdbSpaceAvailable;
	private Map<Integer, HTenant> mTenants;
	private Map<Integer, Integer> mTenantsVoltdbID;
	private Map<Integer, List<Integer>> mVoltdbIDList;

	private ServerServiceHandler mServerServiceHandler;
	private TNonblockingServerSocket mServerTNonblockingServerSocket;
	private TServer mServerServiceServer;

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

	public void start() throws HException, TTransportException {
		if (isStarted)
			return;
		if (mConf.isUseVoltdb()) {
			System.out.println("clearing voltdb contents....");
			clearVoltdb();
			System.out.println("voltdb contents cleared......20%%");
		}
		mVoltdbSpaceAvailable = mVoltdbSpaceTotal;
		System.out.println("clearing tenants information.......");
		mTenants.clear();
		mTenantsVoltdbID.clear();
		mVoltdbIDList.clear();
		System.out.println("tenants information cleared......40%%");
		System.out.println("getting all registered tenants....");
		/*
		 * TODO Initialize all the tenants registered
		 */
		for (int i = 1; i <= Constants.NUMBER_OF_TENANTS; i++) {
			HTenant tenant = new HTenant(this, i);
			mTenants.put(i, tenant);
		}
		System.out.println("all registered tenants got.......60%%");
		System.out.println("setting up server service......");
		mServerServiceHandler = new ServerServiceHandler(this);
		ServerService.Processor<ServerServiceHandler> serverServiceProcessor = new ServerService.Processor<ServerServiceHandler>(
				mServerServiceHandler);

		mServerTNonblockingServerSocket = new TNonblockingServerSocket(
				new InetSocketAddress(mAddress, mPort));
		mServerServiceServer = new TThreadedSelectorServer(
				new TThreadedSelectorServer.Args(
						mServerTNonblockingServerSocket)
						.processor(serverServiceProcessor)
						.selectorThreads(Constants.SELECTOR_THREADS)
						.acceptQueueSizePerThread(
								Constants.QUEUE_SIZE_PER_SELECTOR)
						.workerThreads(Constants.SERVER_THREADS));
		isStarted = true;
		System.out.println("server service start to work......80%");
		System.out
				.format("Server@%s:%d started!......%%100%n", mAddress, mPort);
		mServerServiceServer.serve();
	}

	public void stop() {
		if (!isStarted)
			return;
		/*
		 * TODO move voltdb contents back to mysql and save all information
		 * needed
		 */
		mServerServiceServer.stop();
		mServerTNonblockingServerSocket.close();
		isStarted = false;
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
		synchronized (mTenants) {
			tenant = mTenants.get(id);
			if (tenant != null) {
				tenant.login();
				return true;
			}
			return false;
		}
	}

	public boolean tenantLogout(int id) {
		HTenant tenant;
		synchronized (mTenants) {
			tenant = mTenants.get(id);
			if (tenant != null) {
				tenant.logout();
				return true;
			}
			return false;
		}
	}

	public boolean tenantStart(int id) {
		HTenant tenant;
		synchronized (mTenants) {
			tenant = mTenants.get(id);
			if (tenant != null) {
				tenant.start();
				return true;
			}
			return false;
		}
	}

	public boolean tenantStop(int id) {
		HTenant tenant;
		synchronized (mTenants) {
			tenant = mTenants.get(id);
			if (tenant != null) {
				tenant.stop();
				return true;
			}
			return false;
		}
	}

	public boolean tenantIsLoggedIn(int id) {
		HTenant tenant;
		synchronized (mTenants) {
			tenant = mTenants.get(id);
			if (tenant != null) {
				return tenant.isLoggedIn();
			}
			return false;
		}
	}

	public boolean tenantIsStarted(int id) {
		HTenant tenant;
		synchronized (mTenants) {
			tenant = mTenants.get(id);
			if (tenant != null) {
				return tenant.isStarted();
			}
			return false;
		}
	}

	public int tenantGetIDInVoltdb(int id) {
		synchronized (mTenantsVoltdbID) {
			Integer result = mTenantsVoltdbID.get(id);
			if (result == null)
				return -1;
			return result;
		}
	}

	public int tenantGetDataSize(int id) {
		HTenant tenant;
		synchronized (mTenants) {
			tenant = mTenants.get(id);
			if (tenant != null) {
				return tenant.getDataSize();
			}
			return -1;
		}
	}

	public int tenantGetDataSizeKind(int id){
		HTenant tenant;
		synchronized (mTenants) {
			tenant = mTenants.get(id);
			if (tenant != null) {
				return tenant.getDataSizeKind();
			}
			return -1;
		}
	}
	public boolean tenantIsUseMysql(int id) {
		HTenant tenant;
		synchronized (mTenants) {
			tenant = mTenants.get(id);
			if (tenant != null) {
				return tenant.isUseMysql();
			}
			return false;
		}
	}

	public static void main(String[] args) throws HException,
			TTransportException {
		HServer server = new HServer();
		server.start();
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
}
