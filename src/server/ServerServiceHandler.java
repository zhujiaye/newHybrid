package server;

import org.apache.thrift.TException;

import thrift.ServerService;

public class ServerServiceHandler implements ServerService.Iface {

	private HServer mServer;

	public ServerServiceHandler(HServer server) {
		mServer = server;
	}

	@Override
	public int tenant_getIDInVoltdb(int id) throws TException {
		return mServer.tenantGetIDInVoltdb(id);
	}

	@Override
	public boolean tenant_isUseMysql(int id) throws TException {
		return mServer.tenantIsUseMysql(id);
	}

	@Override
	public boolean tenant_login(int id) throws TException {
		return mServer.tenantLogin(id);
	}

	@Override
	public boolean tenant_logout(int id) throws TException {
		return mServer.tenantLogout(id);
	}

	@Override
	public boolean tenant_start(int id) throws TException {
		return mServer.tenantStart(id);
	}

	@Override
	public boolean tenant_stop(int id) throws TException {
		return mServer.tenantStart(id);
	}

	@Override
	public boolean tenant_isLoggedIn(int id) throws TException {
		return mServer.tenantIsLoggedIn(id);
	}

	@Override
	public boolean tenant_isStarted(int id) throws TException {
		return mServer.tenantIsStarted(id);
	}

	@Override
	public int tenant_getDataSize(int id) throws TException {
		return mServer.tenantGetDataSize(id);
	}

	@Override
	public int tenant_getDataSizeKind(int id) throws TException {
		return mServer.tenantGetDataSizeKind(id);
	}

}
