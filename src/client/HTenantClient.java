package client;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Random;

import newhybrid.ClientShutdownException;
import newhybrid.HQueryResult;
import newhybrid.HSQLTimeOutException;
import newhybrid.NoServerConnectionException;
import newhybrid.QueryType;

import org.apache.log4j.Logger;
import org.voltdb.client.Client;

import utillity.MysqlConnectionPool;
import utillity.VoltdbConnectionPool;
import config.Constants;
import dbInfo.CustomerTable;
import dbInfo.DistrictTable;
import dbInfo.HistoryTable;
import dbInfo.ItemTable;
import dbInfo.NewOrdersTable;
import dbInfo.OrderLineTable;
import dbInfo.OrdersTable;
import dbInfo.StockTable;
import dbInfo.Table;
import dbInfo.WarehouseTable;

/*
 * HTenantClient is the user API for interacting with the server.
 * Every operation that a client need can be found here.
 */
public class HTenantClient {
	final static private Logger LOG = Logger
			.getLogger(Constants.LOGGER_NAME_CLIENT);
	private int mID;
	private Table[] mTables = null;

	private ServerClient mServerClient = null;

	public HTenantClient(int tenantId) {
		mID = tenantId;
		mTables = new Table[Constants.NUMBER_OF_TABLES];
		mTables[0] = new CustomerTable(this);
		mTables[1] = new DistrictTable(this);
		mTables[2] = new HistoryTable(this);
		mTables[3] = new ItemTable(this);
		mTables[4] = new NewOrdersTable(this);
		mTables[5] = new OrderLineTable(this);
		mTables[6] = new OrdersTable(this);
		mTables[7] = new StockTable(this);
		mTables[8] = new WarehouseTable(this);
		mServerClient = new ServerClient();
	}

	public int getID() {
		return mID;
	}

	public synchronized void connect() {
		while (true) {
			try {
				mServerClient.connect();
				break;
			} catch (ClientShutdownException | NoServerConnectionException e) {
				LOG.warn("server client is not useful any more, try to get a new one");
				mServerClient.shutdown();
				mServerClient = new ServerClient();
			}
		}
	}

	public synchronized void cleanConnect() {
		mServerClient.cleanConnect();
	}

	public synchronized void shutdown() {
		mServerClient.shutdown();
	}

	public synchronized boolean isLoggedIn() {
		while (true) {
			try {
				return mServerClient.tenantIsLoggedIn(mID);
			} catch (ClientShutdownException e) {
				LOG.warn("server client is not useful any more, try to get a new one");
				mServerClient.shutdown();
				mServerClient = new ServerClient();
			}
		}
	}

	public synchronized boolean isStarted() {
		while (true) {
			try {
				return mServerClient.tenantIsStarted(mID);
			} catch (ClientShutdownException e) {
				LOG.warn("server client is not useful any more, try to get a new one");
				mServerClient.shutdown();
				mServerClient = new ServerClient();
			}
		}
	}

	/**
	 * If the tenant is not registered yet this method will return false
	 * 
	 * @return whether this tenant logged in successfully
	 * @throws ClientShutdownException
	 */
	public synchronized boolean login() {
		while (true) {
			try {
				return mServerClient.tenantLogin(mID);
			} catch (ClientShutdownException e) {
				LOG.warn("server client is not useful any more, try to get a new one");
				mServerClient.shutdown();
				mServerClient = new ServerClient();
			}
		}
	}

	public synchronized boolean logout() {
		while (true) {
			try {
				return mServerClient.tenantLogout(mID);
			} catch (ClientShutdownException e) {
				LOG.warn("server client is not useful any more, try to get a new one");
				mServerClient.shutdown();
				mServerClient = new ServerClient();
			}
		}
	}

	public synchronized boolean start() {
		while (true) {
			try {
				return mServerClient.tenantStart(mID);
			} catch (ClientShutdownException e) {
				LOG.warn("server client is not useful any more, try to get a new one");
				mServerClient.shutdown();
				mServerClient = new ServerClient();
			}
		}
	}

	public synchronized boolean stop() {
		while (true) {
			try {
				return mServerClient.tenantStop(mID);
			} catch (ClientShutdownException e) {
				LOG.warn("server client is not useful any more, try to get a new one");
				mServerClient.shutdown();
				mServerClient = new ServerClient();
			}
		}
	}

	public synchronized int getIDInVoltdb() {
		while (true) {
			try {
				return mServerClient.tenantGetIDInVoltdb(mID);
			} catch (ClientShutdownException e) {
				LOG.warn("server client is not useful any more, try to get a new one");
				mServerClient.shutdown();
				mServerClient = new ServerClient();
			}
		}
	}

	public synchronized int getDataSize() {
		while (true) {
			try {
				return mServerClient.tenantGetDataSize(mID);
			} catch (ClientShutdownException e) {
				LOG.warn("server client is not useful any more, try to get a new one");
				mServerClient.shutdown();
				mServerClient = new ServerClient();
			}
		}
	}

	public synchronized int getDataSizeKind() {
		while (true) {
			try {
				return mServerClient.tenantGetDataSizeKind(mID);
			} catch (ClientShutdownException e) {
				LOG.warn("server client is not useful any more, try to get a new one");
				mServerClient.shutdown();
				mServerClient = new ServerClient();
			}
		}
	}

	public synchronized boolean isUseMysql() {
		while (true) {
			try {
				return mServerClient.tenantIsUseMysql(mID);
			} catch (ClientShutdownException e) {
				LOG.warn("server client is not useful any more, try to get a new one");
				mServerClient.shutdown();
				mServerClient = new ServerClient();
			}
		}
	}

	public synchronized boolean completeOneQuery() {
		while (true) {
			try {
				return mServerClient.tenantCompleteOneQuery(mID);
			} catch (ClientShutdownException e) {
				LOG.warn("server client is not useful any more, try to get a new one");
				mServerClient.shutdown();
				mServerClient = new ServerClient();
			}
		}
	}

	public synchronized HQueryResult sqlRandomSelect() {
		if (!isLoggedIn()) {
			LOG.error("Tenant " + mID + " not logged in!");
			return new HQueryResult(QueryType.SELECT, true, false,
					"tenant not logged in", 0, 0, null, null, 0);
		}
		if (!isStarted()) {
			LOG.error("Tenant " + mID + " not started!");
			return new HQueryResult(QueryType.SELECT, true, false,
					"tenant not started", 0, 0, null, null, 0);
		}
		Random random = new Random(System.currentTimeMillis());
		int tableIndex;
		tableIndex = random.nextInt(mTables.length);
		HQueryResult result;
		result = mTables[tableIndex].sqlRandomSelect();
		return result;
	}

	public synchronized HQueryResult sqlRandomUpdate() {
		if (!isLoggedIn()) {
			LOG.error("Tenant " + mID + " not logged in!");
			return new HQueryResult(QueryType.UPDATE, true, false,
					"tenant not logged in", 0, 0, null, null, 0);
		}
		if (!isStarted()) {
			LOG.error("Tenant " + mID + " not started!");
			return new HQueryResult(QueryType.UPDATE, true, false,
					"tenant not started", 0, 0, null, null, 0);
		}
		Random random = new Random(System.currentTimeMillis());
		int tableIndex;
		tableIndex = random.nextInt(mTables.length);
		HQueryResult result;
		result = mTables[tableIndex].sqlRandomUpdate();
		return result;
	}
}
