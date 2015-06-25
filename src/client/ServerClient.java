package client;

import newhybrid.HException;
import newhybrid.HeartbeatThread;

import org.apache.log4j.Logger;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransportException;

import thrift.ServerService;
import config.Constants;
import config.HConfig;

public class ServerClient {
	final private static Logger LOG = Logger
			.getLogger(Constants.LOGGER_NAME_CLIENT);
	final private HConfig mConf;
	private volatile boolean mIsConnected = false;
	private volatile boolean mIsShutdown = false;
	private volatile long mLastAccessTime;
	private TProtocol mProtocol = null;
	private HeartbeatThread mHeartbeatThread = null;
	private ServerService.Client mClient;

	public ServerClient() throws HException {
		mConf = HConfig.getConf();
	}

	public synchronized void cleanConnect() {
		if (mIsConnected) {
			LOG.debug("ServerClient Disconnecting from server....");
			mIsConnected = false;
		}
		if (mProtocol != null) {
			mProtocol.getTransport().close();
		}
		if (mHeartbeatThread != null) {
			mHeartbeatThread.shutdown();
		}
	}

	public synchronized void connect() throws HException {
		mLastAccessTime = System.nanoTime();
		if (mIsShutdown) {
			throw new HException("Client has shut down!");
		}
		if (mIsConnected)
			return;
		cleanConnect();
		int tries = 0;
		while (tries++ < Constants.MAX_CONNECT_TRY && !mIsShutdown) {
			mProtocol = new TBinaryProtocol(new TFramedTransport(new TSocket(
					mConf.getServerAddress(), mConf.getServerPort())));
			mClient = new ServerService.Client(mProtocol);
			mLastAccessTime = System.nanoTime();
			try {
				mProtocol.getTransport().open();

				mHeartbeatThread = new HeartbeatThread(
						"ServerClient_heartbeatThread",
						new ServerClientHeartbeatExecutor(this, mConf
								.getServerClientTimeout()),
						mConf.getServerClientTimeout() / 2);
				mHeartbeatThread.start();
			} catch (TTransportException e) {
				LOG.debug("Failed to connect (" + tries + ") to server:"
						+ e.getMessage());
				if (mHeartbeatThread != null) {
					mHeartbeatThread.shutdown();
				}
				try {
					Thread.sleep(Constants.S / 1000000);
				} catch (InterruptedException e1) {
					LOG.error("Interrupted while waiting to connect to server");
				}
				continue;
			}
			mIsConnected = true;
			return;
		}

		// Reaching here indicates that we did not successfully connect.
		throw new HException("Failed to connect to server "
				+ mConf.getServerAddress() + ":" + mConf.getServerPort()
				+ " after " + (tries - 1) + " attempts");
	}

	public synchronized void shutdown() throws HException {
		if (mIsShutdown)
			return;
		cleanConnect();
		mIsShutdown = true;
	}

	public long getLastAccessTime() {
		return mLastAccessTime;
	}

	public int tenantGetIDInVoltdb(int tenant_id) throws HException {
		while (!mIsShutdown) {
			connect();
			try {
				return mClient.tenant_getIDInVoltdb(tenant_id);
			} catch (TException e) {
				throw new HException(e.getMessage());
			}
		}
		throw new HException("Client has shutdown");
	}

	public int tenantGetDataSize(int tenant_id) throws HException {
		while (!mIsShutdown) {
			connect();
			try {
				return mClient.tenant_getDataSize(tenant_id);
			} catch (TException e) {
				throw new HException(e.getMessage());
			}
		}
		throw new HException("Client has shutdown");
	}

	public int tenantGetDataSizeKind(int tenant_id) throws HException {
		while (!mIsShutdown) {
			connect();
			try {
				return mClient.tenant_getDataSizeKind(tenant_id);
			} catch (TException e) {
				throw new HException(e.getMessage());
			}
		}
		throw new HException("Client has shutdown");
	}

	public boolean tenantIsLoggedIn(int tenant_id) throws HException {
		while (!mIsShutdown) {
			connect();
			try {
				return mClient.tenant_isLoggedIn(tenant_id);
			} catch (TException e) {
				throw new HException(e.getMessage());
			}
		}
		throw new HException("Client has shutdown");
	}

	public boolean tenantIsStarted(int tenant_id) throws HException {
		while (!mIsShutdown) {
			connect();
			try {
				return mClient.tenant_isStarted(tenant_id);
			} catch (TException e) {
				throw new HException(e.getMessage());
			}
		}
		throw new HException("Client has shutdown");
	}

	public boolean tenantIsUseMysql(int tenant_id) throws HException {
		while (!mIsShutdown) {
			connect();
			try {
				return mClient.tenant_isUseMysql(tenant_id);
			} catch (TException e) {
				throw new HException(e.getMessage());
			}
		}
		throw new HException("Client has shutdown");
	}

	public boolean tenantLogin(int tenant_id) throws HException {
		while (!mIsShutdown) {
			connect();
			try {
				return mClient.tenant_login(tenant_id);
			} catch (TException e) {
				e.printStackTrace();
				throw new HException(e.getMessage());
			}
		}
		throw new HException("Client has shutdown");
	}

	public boolean tenantLogout(int tenant_id) throws HException {
		while (!mIsShutdown) {
			connect();
			try {
				return mClient.tenant_logout(tenant_id);
			} catch (TException e) {
				throw new HException(e.getMessage());
			}
		}
		throw new HException("Client has shutdown");
	}

	public boolean tenantStart(int tenant_id) throws HException {
		while (!mIsShutdown) {
			connect();
			try {
				return mClient.tenant_start(tenant_id);
			} catch (TException e) {
				throw new HException(e.getMessage());
			}
		}
		throw new HException("Client has shutdown");
	}

	public boolean tenantStop(int tenant_id) throws HException {
		while (!mIsShutdown) {
			connect();
			try {
				return mClient.tenant_stop(tenant_id);
			} catch (TException e) {
				throw new HException(e.getMessage());
			}
		}
		throw new HException("Client has shutdown");
	}

	public boolean tenantCompleteOneQuery(int tenant_id) throws HException {
		while (!mIsShutdown) {
			connect();
			try {
				return mClient.tenant_completeOneQuery(tenant_id);
			} catch (TException e) {
				throw new HException(e.getMessage());
			}
		}
		throw new HException("Client has shutdown");
	}

	public boolean tenantAllLoggedIn() throws HException {
		while (!mIsShutdown) {
			connect();
			try {
				return mClient.tenant_isAllLoggedIn();
			} catch (TException e) {
				throw new HException(e.getMessage());
			}
		}
		throw new HException("Client has shutdown");
	}

	public void serverStop() throws HException {
		while (!mIsShutdown) {
			connect();
			try {
				mClient.server_stop();
			} catch (TException e) {
				throw new HException(e.getMessage());
			}
		}
		throw new HException("Client has shutdown");
	}

	public boolean serverReloadWorkloadFile(String fileName) throws HException {
		while (!mIsShutdown) {
			connect();
			try {
				return mClient.server_reloadWorkloadFile(fileName);
			} catch (TException e) {
				throw new HException(e.getMessage());
			}
		}
		throw new HException("Client has shutdown");
	}
}
