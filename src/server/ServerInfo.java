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

import org.apache.log4j.Logger;

import com.mysql.fabric.Server;

import config.Constants;
import dbInfo.HConnection;
import dbInfo.HSQLException;
import dbInfo.Table;
import newhybrid.NoHConnectionException;
import thrift.DbmsInfo;
import thrift.NoTenantException;
import thrift.NoWorkerException;
import thrift.ServerWorkerInfo;
import thrift.TableInfo;
import thrift.TenantInfo;
import utillity.HConnectionPool;

public class ServerInfo {
	static private final Logger LOG = Logger.getLogger(Constants.LOGGER_NAME);
	private final String SERVER_ADDRESS;
	private final int SERVER_PORT;
	private final String IMAGE_PATH;

	private Map<Integer, ServerTenant> mTenants;

	private ArrayList<ServerWorkerInfo> mWorkers;

	public ServerInfo(String serverAddress, int serverPort, String imagePath) {
		SERVER_ADDRESS = serverAddress;
		SERVER_PORT = serverPort;
		IMAGE_PATH = imagePath;
		mTenants = new HashMap<>();
		mWorkers = new ArrayList<>();
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
				if (workerInfo.mDbmsInfo.mCompleteConnectionString.equals(dbmsInfo.mCompleteConnectionString))
					return true;
			}
			return false;
		}
	}

	private void checkTenantExist(int ID) throws NoTenantException {
		synchronized (mTenants) {
			if (!mTenants.containsKey(ID))
				throw new NoTenantException("tenant with ID=" + ID + " does not exist!");
		}
	}

	private void checkTenantWorker(ServerTenant tenant) throws NoWorkerException {
		synchronized (tenant) {
			DbmsInfo dbmsInfo = tenant.getDbmsInfo();
			if (dbmsInfo == null) {
				ServerWorkerInfo workerInfo = findWorkerForTenant(tenant.getID());
				if (workerInfo == null)
					throw new NoWorkerException(
							"no dbms and can not find a worker for tenant with ID=" + tenant.getID());
				else {
					dbmsInfo = workerInfo.mDbmsInfo;
					tenant.setDbms(dbmsInfo);
				}
			}
			if (haveWorkerForDbms(dbmsInfo))
				return;
			else
				throw new NoWorkerException("no worker is registered for tenant's dbms,tenant ID is " + tenant.getID());
		}
	}

	public boolean registerWorker(ServerWorkerInfo workerInfo) {
		synchronized (mWorkers) {
			for (int i = 0; i < mWorkers.size(); i++) {
				ServerWorkerInfo tmp = mWorkers.get(i);
				if (tmp.mAddress.equals(workerInfo.mAddress) && tmp.mPort == workerInfo.mPort)
					return false;
			}
			LOG.info("a new worker registered: worker@" + workerInfo.mAddress + ":" + workerInfo.mPort);
			mWorkers.add(workerInfo);
			return true;
		}
	}

	public int createTenant() {
		ServerTenant tenant;
		synchronized (mTenants) {
			int maxID = 0;
			for (ServerTenant tmp : mTenants.values()) {
				if (tmp.getID() > maxID)
					maxID = tmp.getID();
			}
			tenant = new ServerTenant(new TenantInfo(maxID + 1), new ArrayList<TableInfo>(), null);
			ServerWorkerInfo workerInfo = findWorkerForTenant(tenant.getID());
			if (workerInfo != null)
				tenant.setDbms(workerInfo.mDbmsInfo);
			mTenants.put(tenant.getID(), tenant);
			return tenant.getID();
		}
	}

	public boolean createTableForTenant(int ID, TableInfo tableInfo)
			throws NoTenantException, NoWorkerException, NoHConnectionException, HSQLException {
		ServerTenant tenant;
		synchronized (mTenants) {
			checkTenantExist(ID);
			tenant = mTenants.get(ID);
		}
		synchronized (tenant) {
			checkTenantWorker(tenant);
			DbmsInfo dbmsInfo = tenant.getDbmsInfo();
			try {
				HConnectionPool pool = HConnectionPool.getPool();
				HConnection hConnection = pool.getConnectionByDbmsInfo(dbmsInfo);
				boolean success = hConnection.createTable(new Table(ID, tableInfo));
				pool.putConnection(hConnection);
				if (success) {
					tenant.addTable(tableInfo);
					return true;
				} else
					return false;
			} catch (NoHConnectionException e) {
				throw e;
			} catch (HSQLException e) {
				throw e;
			}
		}
	}

	public boolean loginForTenant(int ID) throws NoTenantException, NoWorkerException {
		ServerTenant tenant;
		synchronized (mTenants) {
			checkTenantExist(ID);
			tenant = mTenants.get(ID);
		}
		synchronized (tenant) {
			return tenant.login();
		}
	}

	public boolean logoutForTenant(int ID) throws NoTenantException, NoWorkerException {
		ServerTenant tenant;
		synchronized (mTenants) {
			checkTenantExist(ID);
			tenant = mTenants.get(ID);
		}
		synchronized (tenant) {
			return tenant.logout();
		}
	}

	public void init() {
		try {
			synchronized (mTenants) {
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(IMAGE_PATH));
				int tenantsNumber = ois.readInt();
				for (int i = 0; i < tenantsNumber; i++) {
					TenantInfo tenantInfo = (TenantInfo) ois.readObject();
					ArrayList<TableInfo> tablesInfo = (ArrayList<TableInfo>) ois.readObject();
					DbmsInfo dbmsInfo = (DbmsInfo) ois.readObject();
					ServerTenant tenant = new ServerTenant(tenantInfo, tablesInfo, dbmsInfo);
					mTenants.put(tenantInfo.mId, tenant);
				}
				ois.close();
			}
		} catch (FileNotFoundException e) {
			LOG.warn("image file can not be opened for reading:" + e.getMessage());
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
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
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
			LOG.error("image file can not be opened for writing:" + e.getMessage());
		} catch (IOException e) {
			LOG.error(e.getMessage());
		}
	}

	public ArrayList<TableInfo> getTablesForTenant(int ID) throws NoTenantException {
		ServerTenant tenant;
		synchronized (mTenants) {
			checkTenantExist(ID);
			tenant = mTenants.get(ID);
		}
		synchronized (tenant) {
			return tenant.generateTablesInfo();
		}
	}

	public TableInfo getTableForTenant(int ID, String tableName) throws NoTenantException {
		ArrayList<TableInfo> tablesInfo = getTablesForTenant(ID);
		for (int i = 0; i < tablesInfo.size(); i++) {
			TableInfo current = tablesInfo.get(i);
			if (current.mName.equals(tableName))
				return current;
		}
		return null;
	}

	public void dropAllTablesForTenant(int ID)
			throws NoTenantException, NoWorkerException, NoHConnectionException, HSQLException {
		ServerTenant tenant;
		synchronized (mTenants) {
			checkTenantExist(ID);
			tenant = mTenants.get(ID);
		}
		synchronized (tenant) {
			checkTenantWorker(tenant);
			ArrayList<TableInfo> tablesInfo = tenant.generateTablesInfo();
			for (int i = 0; i < tablesInfo.size(); i++) {
				dropTableForTenant(ID, tablesInfo.get(i));
			}
		}
	}

	public void dropTableForTenant(int ID, TableInfo tableInfo)
			throws NoTenantException, NoWorkerException, NoHConnectionException, HSQLException {
		ServerTenant tenant;
		synchronized (mTenants) {
			checkTenantExist(ID);
			tenant = mTenants.get(ID);
		}
		synchronized (tenant) {
			checkTenantWorker(tenant);
			DbmsInfo dbmsInfo = tenant.getDbmsInfo();
			try {
				HConnectionPool pool = HConnectionPool.getPool();
				HConnection hConnection = pool.getConnectionByDbmsInfo(dbmsInfo);
				hConnection.dropTable(new Table(ID, tableInfo));
				pool.putConnection(hConnection);
				tenant.dropTable(tableInfo.mName);
			} catch (NoHConnectionException e) {
				throw e;
			} catch (HSQLException e) {
				throw e;
			}
		}
	}

}
