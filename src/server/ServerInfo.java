package server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.log4j.Logger;

import com.mysql.fabric.Server;

import config.Constants;
import dbInfo.HConnection;
import dbInfo.HConnectionPool;
import dbInfo.HSQLException;
import dbInfo.Table;
import newhybrid.NoHConnectionException;
import test.CustomerTable;
import test.DistrictTable;
import test.HistoryTable;
import test.ItemTable;
import test.NewOrdersTable;
import test.OrderLineTable;
import test.OrdersTable;
import test.StockTable;
import test.WarehouseTable;
import thrift.DbStatus;
import thrift.DbStatusInfo;
import thrift.DbmsInfo;
import thrift.DbmsType;
import thrift.NoTenantException;
import thrift.NoWorkerException;
import thrift.Operation;
import thrift.OperationPara;
import thrift.OperationType;
import thrift.ServerWorkerInfo;
import thrift.TableInfo;
import thrift.TableOperationPara;
import thrift.TenantInfo;

public class ServerInfo {
	static private final Logger LOG = Logger.getLogger(Constants.LOGGER_NAME);
	private final String SERVER_ADDRESS;
	private final int SERVER_PORT;
	private final String IMAGE_PATH;

	private Map<Integer, ServerTenant> mTenants;

	private ArrayList<ServerWorkerInfo> mWorkers;
	private ArrayList<DbMigrator> mDbMigrators;

	public ServerInfo(String serverAddress, int serverPort, String imagePath) {
		SERVER_ADDRESS = serverAddress;
		SERVER_PORT = serverPort;
		IMAGE_PATH = imagePath;
		mTenants = new HashMap<>();
		mWorkers = new ArrayList<>();
		mDbMigrators = new ArrayList<>();
	}

	private ServerWorkerInfo findWorkerForTenant(int ID) {
		synchronized (mWorkers) {
			if (mWorkers.isEmpty())
				return null;
			return mWorkers.get(0);
		}
	}

	private boolean haveWorkerForDbms(DbmsInfo dbmsInfo) {
		synchronized (mWorkers) {
			for (int i = 0; i < mWorkers.size(); i++) {
				ServerWorkerInfo workerInfo = mWorkers.get(i);
				if (workerInfo.mDbmsInfo.mCompleteConnectionString
						.equals(dbmsInfo.mCompleteConnectionString))
					return true;
			}
			return false;
		}
	}

	private void checkTenantExist(int ID) throws NoTenantException {
		synchronized (mTenants) {
			if (!mTenants.containsKey(ID))
				throw new NoTenantException("tenant with ID=" + ID
						+ " does not exist!");
		}
	}

	private void checkTenantWorker(ServerTenant tenant)
			throws NoWorkerException {
		DbmsInfo dbmsInfo = tenant.getDbmsInfo();
		if (dbmsInfo == null) {
			ServerWorkerInfo workerInfo = findWorkerForTenant(tenant.getID());
			if (workerInfo == null)
				throw new NoWorkerException(
						"no dbms and can not find a worker for tenant with ID="
								+ tenant.getID());
			else {
				dbmsInfo = workerInfo.mDbmsInfo;
				tenant.setDbms(dbmsInfo);
			}
		}
		if (haveWorkerForDbms(dbmsInfo))
			return;
		else
			throw new NoWorkerException(
					"no worker is registered for tenant's dbms,tenant ID is "
							+ tenant.getID());
	}

	public void offload() {
		LOG.info("offloading....");
		// ServerTenant tenant;
		// synchronized (mTenants) {
		// if (mTenants.isEmpty())
		// return;
		// Random random = new Random(System.nanoTime());
		// tenant = mTenants.get(random.nextInt(mTenants.size()) + 1);
		// }
		// synchronized (mWorkers) {
		// if (mWorkers.size() < 2)
		// return;
		// ServerWorkerInfo from = null, to = null;
		// for (int i = 0; i < mWorkers.size(); i++) {
		// ServerWorkerInfo currentWorker = mWorkers.get(i);
		// if (currentWorker.mDbmsInfo.mCompleteConnectionString
		// .equals(tenant.getDbmsInfo().mCompleteConnectionString)) {
		// from = currentWorker;
		// } else {
		// to = currentWorker;
		// }
		// }
		// if (from == null || to == null)
		// return;
		// synchronized (mDbMigrators) {
		// for (int i = 0; i < mDbMigrators.size(); i++) {
		// DbMigrator currentMigrator = mDbMigrators.get(i);
		// synchronized (currentMigrator) {
		// if (currentMigrator.getTenantID() == tenant.getID()) {
		// if (currentMigrator.isMigrating()) {
		// // currentMigrator.migrateToNewWorker(to);
		// return;
		// } else {
		// mDbMigrators.remove(i);
		// break;
		// }
		// }
		// }
		// }
		// DbMigrator migrator = new DbMigrator(this, tenant.getID(),
		// from, to);
		// mDbMigrators.add(migrator);
		// migrator.start();
		// }
		// }
	}

	public boolean registerWorker(ServerWorkerInfo workerInfo) {
		synchronized (mWorkers) {
			for (int i = 0; i < mWorkers.size(); i++) {
				ServerWorkerInfo tmp = mWorkers.get(i);
				if (tmp.mAddress.equals(workerInfo.mAddress)
						&& tmp.mPort == workerInfo.mPort)
					return false;
			}
			LOG.info("a new worker registered: worker@" + workerInfo.mAddress
					+ ":" + workerInfo.mPort);
			mWorkers.add(workerInfo);
			return true;
		}
	}

	public int createTenant() {
		ServerTenant tenant;
		synchronized (mTenants) {
			int maxID = -1;
			for (ServerTenant tmp : mTenants.values()) {
				if (tmp.getID() > maxID)
					maxID = tmp.getID();
			}
			tenant = new ServerTenant(new TenantInfo(maxID + 1),
					new ArrayList<TableInfo>(), null);
			ServerWorkerInfo workerInfo = findWorkerForTenant(tenant.getID());
			if (workerInfo != null)
				tenant.setDbms(workerInfo.mDbmsInfo);
			mTenants.put(tenant.getID(), tenant);
			return tenant.getID();
		}
	}

	public boolean createTableForTenant(int tenantID, TableInfo tableInfo)
			throws NoTenantException, NoWorkerException,
			NoHConnectionException, HSQLException {
		ServerTenant tenant;
		synchronized (mTenants) {
			checkTenantExist(tenantID);
			tenant = mTenants.get(tenantID);
		}
		checkTenantWorker(tenant);
		DbStatusInfo dbStatusInfo = tenant.generateDbStatusInfo();
		DbmsInfo dbmsInfo = dbStatusInfo.mDbmsInfo;
		HConnectionPool pool = HConnectionPool.getPool();
		HConnection hConnection = null;
		try {
			hConnection = pool.getConnectionByDbmsInfo(dbmsInfo);
			boolean success = hConnection.createTable(tenantID, tableInfo);
			if (success) {
				tenant.addTable(tableInfo);
				if (dbStatusInfo.mDbStatus == DbStatus.MIGRATING) {
					TableOperationPara tableParas = new TableOperationPara(
							tenantID, tableInfo);
					OperationPara paras = new OperationPara();
					paras.setMTableOpPara(tableParas);
					addOperationToMigrator(tenantID, new Operation(
							OperationType.TABLE_CREATE, paras));
				}
				return true;
			} else
				return false;
		} catch (NoHConnectionException e) {
			throw e;
		} catch (HSQLException e) {
			throw e;
		} finally {
			pool.putConnection(hConnection);
		}
	}

	public boolean loginForTenant(int ID) throws NoTenantException,
			NoWorkerException {
		ServerTenant tenant;
		synchronized (mTenants) {
			checkTenantExist(ID);
			tenant = mTenants.get(ID);
		}
		return tenant.login();
	}

	public boolean logoutForTenant(int ID) throws NoTenantException,
			NoWorkerException {
		ServerTenant tenant;
		synchronized (mTenants) {
			checkTenantExist(ID);
			tenant = mTenants.get(ID);
		}
		return tenant.logout();
	}

	public void init() {
		try {
			synchronized (mTenants) {
				ObjectInputStream ois = new ObjectInputStream(
						new FileInputStream(IMAGE_PATH));
				int tenantsNumber = ois.readInt();
				for (int i = 0; i < tenantsNumber; i++) {
					TenantInfo tenantInfo = (TenantInfo) ois.readObject();
					ArrayList<TableInfo> tablesInfo = (ArrayList<TableInfo>) ois
							.readObject();
					DbmsInfo dbmsInfo = (DbmsInfo) ois.readObject();
					ServerTenant tenant = new ServerTenant(tenantInfo,
							tablesInfo, dbmsInfo);
					mTenants.put(tenantInfo.mId, tenant);
				}
				ois.close();
			}
		} catch (FileNotFoundException e) {
			LOG.warn("image file can not be opened for reading:"
					+ e.getMessage());
		} catch (IOException e) {
			LOG.error(e.getMessage());
		} catch (ClassNotFoundException e) {
			LOG.error(e.getMessage());
		}
	}

	public void writeToImage() {
		try {
			File file = new File(IMAGE_PATH);
			File parentFile = file.getParentFile();
			parentFile.mkdirs();
			ObjectOutputStream oos = new ObjectOutputStream(
					new FileOutputStream(file));
			synchronized (mTenants) {
				oos.writeInt(mTenants.size());
				for (ServerTenant tenant : mTenants.values()) {
					oos.writeObject(tenant.generateTenantInfo());
					oos.writeObject(tenant.generateTablesInfo());
					oos.writeObject(tenant.getDbmsInfo());
				}
				oos.flush();
				oos.close();
			}
		} catch (FileNotFoundException e) {
			LOG.error("image file can not be opened for writing:"
					+ e.getMessage());
		} catch (IOException e) {
			LOG.error(e.getMessage());
		}
	}

	public ArrayList<TableInfo> getTablesForTenant(int ID)
			throws NoTenantException {
		ServerTenant tenant;
		synchronized (mTenants) {
			checkTenantExist(ID);
			tenant = mTenants.get(ID);
		}
		return tenant.generateTablesInfo();
	}

	public TableInfo getTableForTenant(int ID, String tableName)
			throws NoTenantException {
		ArrayList<TableInfo> tablesInfo = getTablesForTenant(ID);
		for (int i = 0; i < tablesInfo.size(); i++) {
			TableInfo current = tablesInfo.get(i);
			if (current.mName.equals(tableName))
				return current;
		}
		return null;
	}

	public void dropAllTablesForTenant(int ID) throws NoTenantException,
			NoWorkerException, NoHConnectionException, HSQLException {
		ServerTenant tenant;
		synchronized (mTenants) {
			checkTenantExist(ID);
			tenant = mTenants.get(ID);
		}
		checkTenantWorker(tenant);
		ArrayList<TableInfo> tablesInfo = tenant.generateTablesInfo();
		for (int i = 0; i < tablesInfo.size(); i++) {
			dropTableForTenant(ID, tablesInfo.get(i));
		}
	}

	public void dropTableForTenant(int ID, TableInfo tableInfo)
			throws NoTenantException, NoWorkerException,
			NoHConnectionException, HSQLException {
		ServerTenant tenant;
		synchronized (mTenants) {
			checkTenantExist(ID);
			tenant = mTenants.get(ID);
		}
		checkTenantWorker(tenant);
		HConnectionPool pool = HConnectionPool.getPool();
		HConnection hConnection = null;
		DbStatusInfo dbStatusInfo = tenant.generateDbStatusInfo();
		DbmsInfo dbmsInfo = dbStatusInfo.mDbmsInfo;
		try {
			hConnection = pool.getConnectionByDbmsInfo(dbmsInfo);
			hConnection.dropTable(ID, tableInfo);
			tenant.dropTable(tableInfo.mName);
			if (dbStatusInfo.mDbStatus == DbStatus.MIGRATING) {
				TableOperationPara tableParas = new TableOperationPara(ID,
						tableInfo);
				OperationPara paras = new OperationPara();
				paras.setMTableOpPara(tableParas);
				addOperationToMigrator(ID, new Operation(
						OperationType.TABLE_DROP, paras));
			}
		} catch (NoHConnectionException e) {
			throw e;
		} catch (HSQLException e) {
			throw e;
		} finally {
			pool.putConnection(hConnection);
		}
	}

	public DbStatusInfo getDbStatusInfoForTenant(int ID)
			throws NoTenantException, NoWorkerException {
		ServerTenant tenant;
		synchronized (mTenants) {
			checkTenantExist(ID);
			tenant = mTenants.get(ID);
		}
		checkTenantWorker(tenant);
		return tenant.generateDbStatusInfo();
	}

	public void lockLockForTenant(int ID) throws InterruptedException,
			NoTenantException {
		ServerTenant tenant;
		synchronized (mTenants) {
			checkTenantExist(ID);
			tenant = mTenants.get(ID);
		}
		tenant.getLock().lock();
	}

	public void releaseLockForTenant(int ID) {
		ServerTenant tenant;
		synchronized (mTenants) {
			try {
				checkTenantExist(ID);
			} catch (NoTenantException e) {
				return;
			}
			tenant = mTenants.get(ID);
		}
		tenant.getLock().release();
	}

	public void setDbStatusForTenant(int ID, DbStatus newDbStatus)
			throws NoTenantException {
		ServerTenant tenant;
		synchronized (mTenants) {
			checkTenantExist(ID);
			tenant = mTenants.get(ID);
		}
		tenant.setDbStatus(newDbStatus);
	}

	public void setDbmsForTenant(int ID, DbmsInfo newDbmsInfo)
			throws NoTenantException {
		ServerTenant tenant;
		synchronized (mTenants) {
			checkTenantExist(ID);
			tenant = mTenants.get(ID);
		}
		tenant.setDbms(newDbmsInfo);
	}

	public void addOperationToMigrator(int tenantID, Operation operation) {
		DbMigrator migrator = null;
		synchronized (mDbMigrators) {
			for (int i = 0; i < mDbMigrators.size(); i++) {
				DbMigrator current = mDbMigrators.get(i);
				if (current.getTenantID() == tenantID) {
					migrator = current;
					break;
				}
			}
		}
		if (migrator == null)
			return;
		migrator.addOperation(operation);
	}

	// just for test
	public void importTpccTable() {
		for (int i = 0; i < 3000; i++)
			createTenant();
		for (int i = 0; i < 3000; i++) {
			ServerTenant tenant = mTenants.get(i);
			tenant.addTable(CustomerTable.getTableInfo());
			tenant.addTable(HistoryTable.getTableInfo());
			tenant.addTable(ItemTable.getTableInfo());
			tenant.addTable(WarehouseTable.getTableInfo());
			tenant.addTable(OrdersTable.getTableInfo());
			tenant.addTable(NewOrdersTable.getTableInfo());
			tenant.addTable(OrderLineTable.getTableInfo());
			tenant.addTable(StockTable.getTableInfo());
			tenant.addTable(DistrictTable.getTableInfo());
		}
		this.writeToImage();
	}

	// just for test
	public boolean migrateTenant(int ID) throws NoTenantException {
		ServerTenant tenant;
		synchronized (mTenants) {
			checkTenantExist(ID);
			tenant = mTenants.get(ID);
		}
		synchronized (mWorkers) {
			if (mWorkers.size() < 2)
				return false;
			ServerWorkerInfo from = null, to = null;
			for (int i = 0; i < mWorkers.size(); i++) {
				ServerWorkerInfo currentWorker = mWorkers.get(i);
				if (currentWorker.mDbmsInfo.mCompleteConnectionString
						.equals(tenant.getDbmsInfo().mCompleteConnectionString)) {
					from = currentWorker;
				} else {
					to = currentWorker;
				}
			}
			if (from == null || to == null)
				return false;
			synchronized (mDbMigrators) {
				for (int i = 0; i < mDbMigrators.size(); i++) {
					DbMigrator currentMigrator = mDbMigrators.get(i);
					synchronized (currentMigrator) {
						if (currentMigrator.getTenantID() == tenant.getID()) {
							if (currentMigrator.isMigrating()) {
								// currentMigrator.migrateToNewWorker(to);
								return false;
							} else {
								mDbMigrators.remove(i);
								break;
							}
						}
					}
				}
				DbMigrator migrator = new DbMigrator(this, tenant.getID(),
						from, to);
				mDbMigrators.add(migrator);
				migrator.start();
			}
			return true;
		}
	}
}
