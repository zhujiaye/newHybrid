package server;

import org.apache.log4j.Logger;
import org.apache.thrift.TException;

import config.Constants;
import thrift.ServerService;
import thrift.ServerWorkerInfo;
import thrift.TenantResult;

public class ServerServiceHandler implements ServerService.Iface {
	final static private Logger LOG = Logger.getLogger(Constants.LOGGER_NAME);
	private ServerInfo mServerInfo;

	public ServerServiceHandler(ServerInfo serverInfo) {
		mServerInfo = serverInfo;
	}

	@Override
	public boolean worker_register(ServerWorkerInfo workerInfo) throws TException {
		// TODO Auto-generated method stub
		return false;
	}

}
