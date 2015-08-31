package server;

import newhybrid.HeartbeatExecutor;

public class ServerMonitorHeartbeatExecutor implements HeartbeatExecutor {
	private final ServerInfo mServerInfo;

	public ServerMonitorHeartbeatExecutor(ServerInfo serverInfo) {
		mServerInfo = serverInfo;
	}

	@Override
	public void heartbeat() {
	}

}
