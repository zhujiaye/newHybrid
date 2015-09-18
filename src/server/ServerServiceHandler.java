package server;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.thrift.TException;

import config.Constants;
import dbInfo.HSQLException;
import newhybrid.NoHConnectionException;
import thrift.DbStatusInfo;
import thrift.DbmsException;
import thrift.LockException;
import thrift.NoTenantException;
import thrift.NoWorkerException;
import thrift.Operation;
import thrift.ServerService;
import thrift.ServerWorkerInfo;
import thrift.TableInfo;

public class ServerServiceHandler implements ServerService.Iface {
	final static private Logger LOG = Logger.getLogger(Constants.LOGGER_NAME);
	private ServerInfo mServerInfo;

	public ServerServiceHandler(ServerInfo serverInfo) {
		mServerInfo = serverInfo;
	}

	@Override
	public boolean worker_register(ServerWorkerInfo workerInfo) throws TException {
		return mServerInfo.registerWorker(workerInfo);
	}

	@Override
	public int tenant_createTenant() throws TException {
		int ID = mServerInfo.createTenant();
		mServerInfo.writeToImage();
		return ID;
	}

	@Override
	public boolean tenant_createTable(int ID, TableInfo tableInfo)
			throws NoWorkerException, NoTenantException, DbmsException, TException {
		try {
			boolean success = mServerInfo.createTableForTenant(ID, tableInfo);
			if (success)
				mServerInfo.writeToImage();
			return success;
		} catch (HSQLException | NoHConnectionException e) {
			throw new DbmsException(e.getMessage());
		}
	}

	@Override
	public boolean tenant_login(int ID) throws NoTenantException, TException {
		return mServerInfo.loginForTenant(ID);
	}

	@Override
	public boolean tenant_logout(int ID) throws NoTenantException, TException {
		return mServerInfo.logoutForTenant(ID);
	}

	@Override
	public List<TableInfo> tenant_getTables(int ID) throws NoTenantException, TException {
		return mServerInfo.getTablesForTenant(ID);
	}

	@Override
	public List<TableInfo> tenant_getTable(int ID, String tableName) throws NoTenantException, TException {
		List<TableInfo> result = new ArrayList<>();
		TableInfo tableInfo = mServerInfo.getTableForTenant(ID, tableName);
		if (tableInfo != null)
			result.add(tableInfo);
		return result;
	}

	@Override
	public void tenant_dropAllTables(int ID) throws NoTenantException, NoWorkerException, TException {
		try {
			mServerInfo.dropAllTablesForTenant(ID);
			mServerInfo.writeToImage();
		} catch (NoHConnectionException | HSQLException e) {
			throw new DbmsException(e.getMessage());
		}
	}

	@Override
	public void tenant_dropTable(int ID, String tableName) throws NoTenantException, NoWorkerException, TException {
		TableInfo tableInfo = mServerInfo.getTableForTenant(ID, tableName);
		if (tableInfo == null)
			return;
		try {
			mServerInfo.dropTableForTenant(ID, tableInfo);
			mServerInfo.writeToImage();
		} catch (NoHConnectionException | HSQLException e) {
			throw new DbmsException(e.getMessage());
		}

	}

	@Override
	public DbStatusInfo tenant_getDbStatusInfo(int ID) throws NoTenantException, NoWorkerException, TException {
		return mServerInfo.getDbStatusInfoForTenant(ID);
	}

	@Override
	public void tenant_lock_lock(int ID) throws LockException, NoTenantException, TException {
		try {
			mServerInfo.lockLockForTenant(ID);
		} catch (InterruptedException e) {
			throw new LockException("require lock for tenant with ID=" + ID + " while being interrupted");
		}
	}

	@Override
	public void tenant_lock_release(int ID) throws TException {
		mServerInfo.releaseLockForTenant(ID);
	}

	@Override
	public void addOperationToMigrator(int ID, Operation operation) throws TException {
		mServerInfo.addOperationToMigrator(ID, operation);
	}

}
