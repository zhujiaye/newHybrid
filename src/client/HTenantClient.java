package client;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Random;

import newhybrid.HException;
import newhybrid.HQueryResult;
import newhybrid.HSQLTimeOutException;
import org.voltdb.client.Client;

import server.ServerClient;
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
	private int mID;
	private MysqlConnectionPool mMysqlPool = null;
	private VoltdbConnectionPool mVoltdbPool = null;
	private Connection mMysqlConnection = null;
	private Client mVoltdbConnection = null;
	private Table[] mTables = null;

	private ServerClient mServerClient = null;

	public HTenantClient(int tenantId) throws HException {
		this(tenantId, null, null);
	}

	public HTenantClient(int tenantId, MysqlConnectionPool mysqlPool,
			VoltdbConnectionPool voltdbPool) throws HException {
		mID = tenantId;
		mMysqlPool = mysqlPool;
		mVoltdbPool = voltdbPool;
		if (mMysqlPool == null) {
			mMysqlPool = new MysqlConnectionPool();
		}
		if (mVoltdbPool == null) {
			mVoltdbPool = new VoltdbConnectionPool();
		}
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
		if (mMysqlPool != null)
			mMysqlPool.clear();
		if (mVoltdbPool != null)
			mVoltdbPool.clear();
	}

	public synchronized boolean isLoggedIn() throws HException {
		return mServerClient.isLoggedIn(mID);
	}

	public synchronized boolean isStarted() throws HException {
		return mServerClient.isStarted(mID);
	}

	/**
	 * If the tenant is not registered yet this method will return false
	 * 
	 * @return whether this tenant logged in successfully
	 * @throws HException
	 */
	public synchronized boolean login() throws HException {
		return mServerClient.login(mID);
	}

	public synchronized boolean logout() throws HException {
		return mServerClient.logout(mID);
	}

	public synchronized boolean start() throws HException {
		return mServerClient.start(mID);
	}

	public synchronized boolean stop() throws HException {
		return mServerClient.stop(mID);
	}

	public synchronized int getIDInVoltdb() throws HException {
		return mServerClient.getIDInVoltdb(mID);
	}

	public synchronized int getDataSize() throws HException {
		return mServerClient.getDataSize(mID);
	}

	public synchronized int getDataSizeKind() throws HException {
		return mServerClient.getDataSizeKind(mID);
	}

	public synchronized boolean isUseMysql() throws HException {
		return mServerClient.isUseMysql(mID);
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
			System.out.println("Tenant " + mID + " not logged in!");
			return null;
		}
		if (!isStarted()) {
			System.out.println("Tenant " + mID + " not started!");
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
			System.out.println("Tenant " + mID + " not logged in!");
			return null;
		}
		if (!isStarted()) {
			System.out.println("Tenant " + mID + " not started!");
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
