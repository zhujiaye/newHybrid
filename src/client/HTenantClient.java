package client;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Random;

import newhybrid.HException;
import newhybrid.HQueryResult;
import newhybrid.HSQLTimeOutException;

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
	private MysqlConnectionPool mMysqlPool = null;
	private VoltdbConnectionPool mVoltdbPool = null;
	private Connection mMysqlConnection = null;
	private Client mVoltdbConnection = null;
	private Table[] mTables = null;

	private ServerClient mServerClient = null;

	public HTenantClient(int tenantId) throws HException {
		mID = tenantId;
		mMysqlPool = MysqlConnectionPool.getPool();
		mVoltdbPool = VoltdbConnectionPool.getPool();
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

	public synchronized void connect() throws HException {
		mServerClient.connect();
	}

	public synchronized void shutdown() throws HException {
		mServerClient.shutdown();
		releaseConnection();
		// if (mMysqlPool != null)
		// mMysqlPool.clear();
		// if (mVoltdbPool != null)
		// mVoltdbPool.clear();
	}

	public synchronized boolean isLoggedIn() throws HException {
		return mServerClient.tenantIsLoggedIn(mID);
	}

	public synchronized boolean isStarted() throws HException {
		return mServerClient.tenantIsStarted(mID);
	}

	/**
	 * If the tenant is not registered yet this method will return false
	 * 
	 * @return whether this tenant logged in successfully
	 * @throws HException
	 */
	public synchronized boolean login() throws HException {
		return mServerClient.tenantLogin(mID);
	}

	public synchronized boolean logout() throws HException {
		return mServerClient.tenantLogout(mID);
	}

	public synchronized boolean start() throws HException {
		return mServerClient.tenantStart(mID);
	}

	public synchronized boolean stop() throws HException {
		return mServerClient.tenantStop(mID);
	}

	public synchronized int getIDInVoltdb() throws HException {
		return mServerClient.tenantGetIDInVoltdb(mID);
	}

	public synchronized int getDataSize() throws HException {
		return mServerClient.tenantGetDataSize(mID);
	}

	public synchronized int getDataSizeKind() throws HException {
		return mServerClient.tenantGetDataSizeKind(mID);
	}

	public synchronized boolean isUseMysql() throws HException {
		return mServerClient.tenantIsUseMysql(mID);
	}

	public synchronized boolean completeOneQuery() throws HException {
		return mServerClient.tenantCompleteOneQuery(mID);
	}

	public synchronized Connection getMysqlConnection() throws HException {
		openMysqlConnection();
		return mMysqlConnection;
	}

	public synchronized Client getVoltdbConnection() throws HException {
		openVoltDBConnection();
		return mVoltdbConnection;
	}

	public synchronized HQueryResult sqlRandomSelect() throws HException,
			HSQLTimeOutException {
		if (!isLoggedIn()) {
			LOG.error("Tenant " + mID + " not logged in!");
			return null;
		}
		if (!isStarted()) {
			LOG.error("Tenant " + mID + " not started!");
			return null;
		}
		Random random = new Random(System.currentTimeMillis());
		int tableIndex;
		tableIndex = random.nextInt(mTables.length);
		HQueryResult result;
		result = mTables[tableIndex].sqlRandomSelect();
		return result;
	}

	public synchronized HQueryResult sqlRandomUpdate() throws HException,
			HSQLTimeOutException {
		if (!isLoggedIn()) {
			LOG.error("Tenant " + mID + " not logged in!");
			return null;
		}
		if (!isStarted()) {
			LOG.error("Tenant " + mID + " not started!");
			return null;
		}
		Random random = new Random(System.currentTimeMillis());
		int tableIndex;
		tableIndex = random.nextInt(mTables.length);
		HQueryResult result;
		result = mTables[tableIndex].sqlRandomUpdate();
		return result;
	}

	private void releaseConnection() throws HException {
		closeMysqlConnection();
		closeVoltDBConnection();
	}

	private void openMysqlConnection() throws HException {
		try {
			if (mMysqlConnection != null && !mMysqlConnection.isClosed()) {
				return;
			}
		} catch (SQLException e) {
			throw new HException("Database access error!");
		}
		if (mMysqlPool != null) {
			mMysqlConnection = mMysqlPool.getConnection();
		}
		if (mMysqlConnection == null) {
			throw new HException("Can't connect to mysql!");
		}
	}

	private void closeMysqlConnection() throws HException {
		try {
			if (mMysqlConnection == null || mMysqlConnection.isClosed())
				return;
		} catch (SQLException e) {
			throw new HException("Database access error!");
		}
		if (mMysqlPool != null) {
			mMysqlPool.putConnection(mMysqlConnection);
		}

		mMysqlConnection = null;
	}

	private void openVoltDBConnection() throws HException {
		if (mVoltdbConnection != null
				&& !mVoltdbConnection.getConnectedHostList().isEmpty())
			return;
		if (mVoltdbPool != null) {
			mVoltdbConnection = mVoltdbPool.getConnection();
		}
	}

	private void closeVoltDBConnection() {
		if (mVoltdbConnection == null)
			return;
		if (mVoltdbPool != null) {
			mVoltdbPool.putConnection(mVoltdbConnection);
		}
		mVoltdbConnection = null;
	}
}
