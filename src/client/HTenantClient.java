package client;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Random;

import newhybrid.HException;
import newhybrid.HQueryResult;
import newhybrid.HSQLTimeOutException;

import org.voltdb.client.Client;

import utillity.MysqlConnectionPool;
import utillity.VoltdbConnectionPool;
import config.Constants;
import config.HConfig;
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
	private int mId;
	private boolean mIsConnected = false;
	private boolean mIsShutdown = false;
	private MysqlConnectionPool mMysqlPool = null;
	private VoltdbConnectionPool mVoltdbPool = null;
	private Connection mMysqlConnection = null;
	private Client mVoltdbConnection = null;
	private Table[] mTables = null;

	public HTenantClient(int tenantId) throws HException {
		this(tenantId, null, null);
	}

	public HTenantClient(int tenantId, MysqlConnectionPool mysqlPool,
			VoltdbConnectionPool voltdbPool) throws HException {
		mId = tenantId;
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
	}

	/*
	 * TODO if a client not connect to server for a while, we should clean the
	 * connection for saving resources
	 */
	public void cleanConnect() {
	}

	// TODO complete this method
	public void connect() {
		if (mIsShutdown)
			return;
		if (mIsConnected)
			return;
		mIsConnected = true;
	}

	// TODO complete this method
	public void shutdown() {
		if (mIsShutdown)
			return;
		mIsShutdown = true;
	}

	public int getID() {
		return mId;
	}

	// TODO complete this method
	public int getIDInVoltdb() {
		connect();
		return 1;
	}

	// TODO complete this method
	public boolean isUseMysql() {
		connect();
		return false;
	}

	public Connection getMysqlConnection() throws HException {
		openMysqlConnection();
		return mMysqlConnection;
	}

	public Client getVoltdbConnection() throws HException {
		openVoltDBConnection();
		return mVoltdbConnection;
	}

	public HQueryResult sqlRandomSelect() throws HException,
			HSQLTimeOutException {
		Random random = new Random(System.currentTimeMillis());
		int tableIndex;
		tableIndex = random.nextInt(mTables.length);
		HQueryResult result;
		result = mTables[tableIndex].sqlRandomSelect();
		return result;
	}

	public HQueryResult sqlRandomUpdate() throws HException,
			HSQLTimeOutException {
		Random random = new Random(System.currentTimeMillis());
		int tableIndex;
		tableIndex = random.nextInt(mTables.length);
		HQueryResult result;
		result = mTables[tableIndex].sqlRandomUpdate();
		return result;
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
