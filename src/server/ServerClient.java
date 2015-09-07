package server;

import newhybrid.ClientShutdownException;
import newhybrid.NoServerConnectionException;

import org.apache.log4j.Logger;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransportException;

import thrift.NoTenantException;
import thrift.NoWorkerException;
import thrift.ServerService;
import thrift.ServerWorkerInfo;
import thrift.TableInfo;
import config.Constants;

/**
 * a server client to interact with server,it's not thread-safe now,so create
 * one for every thread
 * 
 * @author zhujiaye
 *
 */
public class ServerClient {
	private final static Logger LOG = Logger.getLogger(Constants.LOGGER_NAME);

	private String mServerAddress;
	private int mServerPort;
	private volatile boolean mIsConnected = false;
	private volatile boolean mIsShutdown = false;
	private volatile long mLastAccessTime;
	private TProtocol mProtocol = null;
	// private HeartbeatThread mHeartbeatThread = null;
	private ServerService.Client mClient;

	public ServerClient(String serverAddress, int serverPort) {
		mServerAddress = serverAddress;
		mServerPort = serverPort;
	}

	/**
	 * clean connection between client and server, including the socket
	 * connection
	 */
	private synchronized void cleanConnect() {
		if (mIsConnected) {
			LOG.debug("serverclient disconnecting from server@" + mServerAddress + ":" + mServerPort);
			mIsConnected = false;
		}
		if (mProtocol != null) {
			mProtocol.getTransport().close();
		}
		// if (mHeartbeatThread != null) {
		// mHeartbeatThread.shutdown();
		// }
	}

	private synchronized void connect() throws ClientShutdownException, NoServerConnectionException {
		mLastAccessTime = System.nanoTime();
		if (mIsShutdown) {
			throw new ClientShutdownException("server client is already shut down!");
		}
		if (mIsConnected)
			return;
		cleanConnect();
		int tries = 0;
		while (tries++ < Constants.MAX_CONNECT_TRY && !mIsShutdown) {
			mProtocol = new TBinaryProtocol(new TFramedTransport(new TSocket(mServerAddress, mServerPort)));
			mClient = new ServerService.Client(mProtocol);
			mLastAccessTime = System.nanoTime();
			try {
				mProtocol.getTransport().open();

				// mHeartbeatThread = new HeartbeatThread(
				// "ServerClient_heartbeatThread",
				// new ServerClientHeartbeatExecutor(this, mConf
				// .getServerClientTimeout()),
				// mConf.getServerClientTimeout() / 2);
				// mHeartbeatThread.start();
			} catch (TTransportException e) {
				LOG.debug("Failed to connect (" + tries + ") to server:" + e.getMessage());
				// if (mHeartbeatThread != null) {
				// mHeartbeatThread.shutdown();
				// }
				try {
					Thread.sleep(Constants.S / 1000000);
				} catch (InterruptedException e1) {
					LOG.error("Interrupted while waiting to connect to server:" + e1.getMessage());
				}
				continue;
			}
			mIsConnected = true;
			return;
		}

		// Reaching here indicates that we did not successfully connect.
		throw new NoServerConnectionException("Failed to connect to server " + mServerAddress + ":" + mServerPort
				+ " after " + (tries - 1) + " attempts");
	}

	public synchronized void shutdown() {
		if (mIsShutdown)
			return;
		cleanConnect();
		mIsShutdown = true;
	}

	public long getLastAccessTime() {
		return mLastAccessTime;
	}

	public boolean serverExist() throws ClientShutdownException {
		try {
			connect();
			cleanConnect();
		} catch (NoServerConnectionException e) {
			return false;
		}
		return true;
	}

	/**
	 * register a worker from server
	 * 
	 * @param workerInfo
	 * @return true if succeeded, false if the worker already exists
	 * @throws ClientShutdownException
	 */
	public boolean worker_register(ServerWorkerInfo workerInfo) throws ClientShutdownException {
		while (!mIsShutdown) {
			try {
				connect();
				return mClient.worker_register(workerInfo);
			} catch (TException | ClientShutdownException | NoServerConnectionException e) {
				LOG.error(e.getMessage());
				mIsConnected = false;
			}
		}
		throw new ClientShutdownException("server client is already shut down");
	}

	/**
	 * create a new tenant
	 * 
	 * @return the ID for the created new tenant
	 * @throws ClientShutdownException
	 */
	public int tenant_createTenant() throws ClientShutdownException {
		while (!mIsShutdown) {
			try {
				connect();
				return mClient.tenant_createTenant();
			} catch (TException | ClientShutdownException | NoServerConnectionException e) {
				LOG.error(e.getMessage());
				mIsConnected = false;
			}
		}
		throw new ClientShutdownException("server client is already shut down");
	}

	public boolean tenant_createTable(int ID, TableInfo tableInfo)
			throws ClientShutdownException, NoWorkerException, NoTenantException {
		while (!mIsShutdown) {
			try {
				connect();
				return mClient.tenant_createTable(ID, tableInfo);
			} catch (NoWorkerException | NoTenantException e) {
				throw e;
			} catch (TException | ClientShutdownException | NoServerConnectionException e) {
				LOG.error(e.getMessage());
				mIsConnected = false;
			}
		}
		throw new ClientShutdownException("server client is already shut down");
	}

	public boolean tenant_login(int ID) throws NoTenantException, ClientShutdownException {
		while (!mIsShutdown) {
			try {
				connect();
				return mClient.tenant_login(ID);
			} catch (NoTenantException e) {
				throw e;
			} catch (TException | ClientShutdownException | NoServerConnectionException e) {
				LOG.error(e.getMessage());
				mIsConnected = false;
			}
		}
		throw new ClientShutdownException("server client is already shut down");
	}
	public boolean tenant_logout(int ID) throws NoTenantException, ClientShutdownException {
		while (!mIsShutdown) {
			try {
				connect();
				return mClient.tenant_logout(ID);
			} catch (NoTenantException e) {
				throw e;
			} catch (TException | ClientShutdownException | NoServerConnectionException e) {
				LOG.error(e.getMessage());
				mIsConnected = false;
			}
		}
		throw new ClientShutdownException("server client is already shut down");
	}
}
