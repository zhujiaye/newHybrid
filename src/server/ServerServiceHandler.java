package server;

import org.apache.log4j.Logger;
import org.apache.thrift.TException;

import config.Constants;
import thrift.NoTenantException;
import thrift.NoWorkerException;
import thrift.ServerService;
import thrift.ServerWorkerInfo;
import thrift.TableInfo;
import thrift.TenantResult;

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
			throws NoWorkerException, NoTenantException, TException {
		boolean success = mServerInfo.createTableForTenant(ID, tableInfo);
		if (success)
			mServerInfo.writeToImage();
		return success;
	}

	@Override
	public boolean tenant_login(int ID) throws NoTenantException, TException {
		return mServerInfo.loginForTenant(ID);
	}

	@Override
	public boolean tenant_logout(int ID) throws NoTenantException, TException {
		return mServerInfo.logoutForTenant(ID);
	}

}
