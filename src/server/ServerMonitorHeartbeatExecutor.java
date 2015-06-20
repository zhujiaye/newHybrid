package server;

import newhybrid.HException;
import newhybrid.HeartbeatExecutor;

public class ServerMonitorHeartbeatExecutor implements HeartbeatExecutor {
	final private HServer mServer;

	public ServerMonitorHeartbeatExecutor(HServer server) {
		mServer = server;
	}

	@Override
	public void heartbeat() throws HException {
		mServer.updateWorkloadLimitInMysql();
	}

}
