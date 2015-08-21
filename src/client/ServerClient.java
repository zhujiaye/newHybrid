package client;

import newhybrid.ClientShutdownException;
import newhybrid.NoServerConnectionException;

import org.apache.log4j.Logger;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransportException;

import thrift.ServerService;
import thrift.ServerWorkerInfo;
import thrift.TenantResult;
import config.Constants;
import config.ServerConf;

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

	public int tenantGetIDInVoltdb(int tenant_id) throws ClientShutdownException {
		while (!mIsShutdown) {
			try {
				connect();
				return mClient.tenant_getIDInVoltdb(tenant_id);
			} catch (TException | ClientShutdownException | NoServerConnectionException e) {
				LOG.error(e.getMessage());
				mIsConnected = false;
			}
		}
		throw new ClientShutdownException("server client is already shut down");
	}

	public int tenantGetDataSize(int tenant_id) throws ClientShutdownException {
		while (!mIsShutdown) {
			try {
				connect();
				return mClient.tenant_getDataSize(tenant_id);
			} catch (TException | ClientShutdownException | NoServerConnectionException e) {
				LOG.error(e.getMessage());
				mIsConnected = false;
			}
		}
		throw new ClientShutdownException("server client is already shut down");
	}

	public int tenantGetDataSizeKind(int tenant_id) throws ClientShutdownException {
		while (!mIsShutdown) {
			try {
				connect();
				return mClient.tenant_getDataSizeKind(tenant_id);
			} catch (TException | ClientShutdownException | NoServerConnectionException e) {
				LOG.error(e.getMessage());
				mIsConnected = false;
			}
		}
		throw new ClientShutdownException("server client is already shut down");
	}

	public boolean tenantIsLoggedIn(int tenant_id) throws ClientShutdownException {
		while (!mIsShutdown) {
			try {
				connect();
				return mClient.tenant_isLoggedIn(tenant_id);
			} catch (TException | ClientShutdownException | NoServerConnectionException e) {
				LOG.error(e.getMessage());
				mIsConnected = false;
			}
		}
		throw new ClientShutdownException("server client is already shut down");
	}

	public boolean tenantIsStarted(int tenant_id) throws ClientShutdownException {
		while (!mIsShutdown) {
			try {
				connect();
				return mClient.tenant_isStarted(tenant_id);
			} catch (TException | ClientShutdownException | NoServerConnectionException e) {
				LOG.error(e.getMessage());
				mIsConnected = false;
			}
		}
		throw new ClientShutdownException("server client is already shut down");
	}

	public boolean tenantIsUseMysql(int tenant_id) throws ClientShutdownException {
		while (!mIsShutdown) {
			try {
				connect();
				return mClient.tenant_isUseMysql(tenant_id);
			} catch (TException | ClientShutdownException | NoServerConnectionException e) {
				LOG.error(e.getMessage());
				mIsConnected = false;
			}
		}
		throw new ClientShutdownException("server client is already shut down");
	}

	public boolean tenantLogin(int tenant_id) throws ClientShutdownException {
		while (!mIsShutdown) {
			try {
				connect();
				return mClient.tenant_login(tenant_id);
			} catch (TException | ClientShutdownException | NoServerConnectionException e) {
				LOG.error(e.getMessage());
				mIsConnected = false;
			}
		}
		throw new ClientShutdownException("server client is already shut down");
	}

	public boolean tenantLogout(int tenant_id) throws ClientShutdownException {
		while (!mIsShutdown) {
			try {
				connect();
				return mClient.tenant_logout(tenant_id);
			} catch (TException | ClientShutdownException | NoServerConnectionException e) {
				LOG.error(e.getMessage());
				mIsConnected = false;
			}
		}
		throw new ClientShutdownException("server client is already shut down");
	}

	public boolean tenantStart(int tenant_id) throws ClientShutdownException {
		while (!mIsShutdown) {
			try {
				connect();
				return mClient.tenant_start(tenant_id);
			} catch (TException | ClientShutdownException | NoServerConnectionException e) {
				LOG.error(e.getMessage());
				mIsConnected = false;
			}
		}
		throw new ClientShutdownException("server client is already shut down");
	}

	public boolean tenantStop(int tenant_id) throws ClientShutdownException {
		while (!mIsShutdown) {
			try {
				connect();
				return mClient.tenant_stop(tenant_id);
			} catch (TException | ClientShutdownException | NoServerConnectionException e) {
				LOG.error(e.getMessage());
				mIsConnected = false;
			}
		}
		throw new ClientShutdownException("server client is already shut down");
	}

	public boolean tenantCompleteOneQuery(int tenant_id) throws ClientShutdownException {
		while (!mIsShutdown) {
			try {
				connect();
				return mClient.tenant_completeOneQuery(tenant_id);
			} catch (TException | ClientShutdownException | NoServerConnectionException e) {
				LOG.error(e.getMessage());
				mIsConnected = false;
			}
		}
		throw new ClientShutdownException("server client is already shut down");
	}

	public boolean tenantAllLoggedIn() throws ClientShutdownException {
		while (!mIsShutdown) {
			try {
				connect();
				return mClient.tenant_isAllLoggedIn();
			} catch (TException | ClientShutdownException | NoServerConnectionException e) {
				LOG.error(e.getMessage());
				mIsConnected = false;
			}
		}
		throw new ClientShutdownException("server client is already shut down");
	}

	public void serverStop() throws ClientShutdownException {
		while (!mIsShutdown) {
			try {
				connect();
				mClient.server_stop();
				return;
			} catch (TException | ClientShutdownException | NoServerConnectionException e) {
				LOG.error(e.getMessage());
				mIsConnected = false;
			}
		}
		throw new ClientShutdownException("server client is already shut down");
	}

	public boolean serverReloadWorkloadFile(String fileName) throws ClientShutdownException {
		while (!mIsShutdown) {
			try {
				connect();
				return mClient.server_reloadWorkloadFile(fileName);
			} catch (TException | ClientShutdownException | NoServerConnectionException e) {
				LOG.error(e.getMessage());
				mIsConnected = false;
			}
		}
		throw new ClientShutdownException("server client is already shut down");
	}

	public void serverReportResult(TenantResult tenantResult, String outputFileName) throws ClientShutdownException {
		while (!mIsShutdown) {
			try {
				connect();
				mClient.server_reportResult(tenantResult, outputFileName);
				return;
			} catch (TException | ClientShutdownException | NoServerConnectionException e) {
				LOG.error(e.getMessage());
				mIsConnected = false;
			}
		}
		throw new ClientShutdownException("server client is already shut down");
	}

	public void serverReconfigure(boolean isMysqlOnly, int voltdbCapacity) throws ClientShutdownException {
		while (!mIsShutdown) {
			try {
				connect();
				mClient.server_reconfigure(isMysqlOnly, voltdbCapacity);
				return;
			} catch (TException | ClientShutdownException | NoServerConnectionException e) {
				LOG.error(e.getMessage());
				mIsConnected = false;
			}
		}
		throw new ClientShutdownException("server client is already shut down");
	}

	public void reportSplit(int splitID, int splitViolatedTenants, int splitViolatedQueries)
			throws ClientShutdownException {
		while (!mIsShutdown) {
			try {
				connect();
				mClient.test_reportSplit(splitID, splitViolatedTenants, splitViolatedQueries);
				return;
			} catch (TException | ClientShutdownException | NoServerConnectionException e) {
				LOG.error(e.getMessage());
				mIsConnected = false;
			}
		}
		throw new ClientShutdownException("server client is already shut down");
	}

	public boolean clientNeedToStop() throws ClientShutdownException {
		while (!mIsShutdown) {
			try {
				connect();
				return mClient.test_clientNeedToStop();
			} catch (TException | ClientShutdownException | NoServerConnectionException e) {
				LOG.error(e.getMessage());
				mIsConnected = false;
			}
		}
		throw new ClientShutdownException("server client is already shut down");
	}

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
}
