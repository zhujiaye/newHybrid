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

	private boolean checkWorkerForTenant(int ID) throws NoWorkerException {
		synchronized (mTenants) {
			ServerTenant tenant = mTenants.get(ID);
			DbmsInfo dbmsInfo = tenant.getDbmsInfo();
			if (dbmsInfo == null) {
				ServerWorkerInfo workerInfo = findWorkerForTenant(ID);
				if (workerInfo == null)
					throw new NoWorkerException("no dbms and can not find a worker for tenant with ID=" + ID);
				else {
					dbmsInfo = workerInfo.mDbmsInfo;
					tenant.setDbms(dbmsInfo);
				}
			}
			return haveWorkerForDbms(dbmsInfo);
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

	public boolean createTableForTenant(int ID, TableInfo tableInfo) throws NoTenantException, NoWorkerException {
		synchronized (mTenants) {
			if (!mTenants.containsKey(ID))
				throw new NoTenantException("tenant with ID=" + ID + " does not exists");
			if (!checkWorkerForTenant(ID))
				throw new NoWorkerException("no worker is registered for tenant's dbms,tenant ID is " + ID);
			ServerTenant tenant = mTenants.get(ID);
			DbmsInfo dbmsInfo = tenant.getDbmsInfo();
			try {
				HConnectionPool pool = HConnectionPool.getPool();
				HConnection hConnection = pool.getConnectionByDbmsInfo(dbmsInfo);
				if (hConnection.createTable(new Table(ID, tableInfo))) {
					tenant.addTable(tableInfo);
					return true;
				} else
					return false;
			} catch (NoHConnectionException e) {
				LOG.error(e.getMessage());
				return false;
			} catch (HSQLException e) {
				LOG.error(e.getMessage());
				return false;
			}
		}
	}

	public void init() {
		try {
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
			oos.writeInt(mTenants.size());
			for (ServerTenant tenant : mTenants.values()) {
				oos.writeObject(tenant.generateTenantInfo());
				oos.writeObject(tenant.generateTablesInfo());
				oos.writeObject(tenant.getDbmsInfo());
			}
			oos.flush();
			oos.close();
		} catch (FileNotFoundException e) {
			LOG.error("image file can not be opened for writing:" + e.getMessage());
		} catch (IOException e) {
			LOG.error(e.getMessage());
		}
	}

}
