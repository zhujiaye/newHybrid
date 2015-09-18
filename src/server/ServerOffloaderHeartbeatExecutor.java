package server;

import newhybrid.HeartbeatExecutor;

public class ServerOffloaderHeartbeatExecutor implements HeartbeatExecutor {
	private final ServerInfo mServerInfo;

	public ServerOffloaderHeartbeatExecutor(ServerInfo serverInfo) {
		mServerInfo = serverInfo;
	}

	@Override
	public void heartbeat() {
		mServerInfo.offload();
	}

}
