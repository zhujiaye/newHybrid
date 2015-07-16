package server;

import org.apache.log4j.Logger;
import org.apache.thrift.TException;

import config.Constants;
import thrift.ServerService;
import thrift.TenantResult;

public class ServerServiceHandler implements ServerService.Iface {
	final static private Logger LOG = Logger.getLogger(Constants.LOGGER_NAME);
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

	@Override
	public boolean tenant_completeOneQuery(int id) throws TException {
		return mServer.tenantCompleteOneQuery(id);
	}

	@Override
	public boolean tenant_isAllLoggedIn() throws TException {
		return mServer.tenantAllLoggedIn();
	}

	@Override
	public boolean server_reloadWorkloadFile(String workloadFileName)
			throws TException {
		return mServer.reloadWorkloadFile(workloadFileName);
	}

	@Override
	public void server_stop() throws TException {
		mServer.stop();
	}

	@Override
	public void server_reportResult(TenantResult tenantResult,
			String outputFileName) throws TException {
		mServer.reportResult(tenantResult, outputFileName);
	}

	@Override
	public void server_reconfigure(boolean isMysqlOnly, int voltdbCapacity)
			throws TException {
		mServer.reconfigure(isMysqlOnly, voltdbCapacity);
	}

	@Override
	public void test_reportSplit(int splitID, int splitViolatedTenants,
			int splitViolatedQueries) throws TException {
		mServer.reportSplit(splitID, splitViolatedTenants, splitViolatedQueries);
	}

	@Override
	public boolean test_clientNeedToStop() throws TException {
		return mServer.clientNeedToStop();
	}

}
