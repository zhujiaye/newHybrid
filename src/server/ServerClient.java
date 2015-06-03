package server;

import newhybrid.HException;
import newhybrid.HeartbeatThread;

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
	final private HConfig mConf;
	private boolean mIsConnected = false;
	private boolean mIsShutdown = false;
	private long mLastAccessTime;
	private TProtocol mProtocol = null;
	private HeartbeatThread mHeartbeatThread = null;
	private ServerService.Client mClient;

	public ServerClient() throws HException {
		mConf = HConfig.getConf();
	}

	public synchronized void cleanConnect() {
		if (mIsConnected) {
			System.out.println("serverClient Disconnecting from server....");
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
						"HTenantClient_heartbeatThread",
						new ServerClientHeartbeatExecutor(this,
								Constants.CONNECT_TIMEOUT),
						Constants.CONNECT_TIMEOUT / 2);
				mHeartbeatThread.start();
			} catch (TTransportException e) {
				System.out.println("Failed to connect (" + tries
						+ ") to server:" + e.getMessage());
				if (mHeartbeatThread != null) {
					mHeartbeatThread.shutdown();
				}
				try {
					Thread.sleep(Constants.S / 1000000);
				} catch (InterruptedException e1) {
					System.out
							.println("Interrupted while waiting to connect to server");
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

	public synchronized long getLastAccessTime() {
		return mLastAccessTime;
	}

	public synchronized int getIDInVoltdb(int tenant_id) throws HException {
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

	public synchronized int getDataSize(int tenant_id) throws HException {
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

	public synchronized int getDataSizeKind(int tenant_id) throws HException {
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

	public synchronized boolean isLoggedIn(int tenant_id) throws HException {
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

	public synchronized boolean isStarted(int tenant_id) throws HException {
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

	public synchronized boolean isUseMysql(int tenant_id) throws HException {
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

	public synchronized boolean login(int tenant_id) throws HException {
		while (!mIsShutdown) {
			connect();
			try {
				return mClient.tenant_login(tenant_id);
			} catch (TException e) {
				throw new HException(e.getMessage());
			}
		}
		throw new HException("Client has shutdown");
	}

	public synchronized boolean logout(int tenant_id) throws HException {
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

	public synchronized boolean start(int tenant_id) throws HException {
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

	public synchronized boolean stop(int tenant_id) throws HException {
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

}
