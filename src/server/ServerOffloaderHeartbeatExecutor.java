package server;

import newhybrid.HeartbeatExecutor;

public class ServerOffloaderHeartbeatExecutor implements HeartbeatExecutor {
	final private HServer mServer;

	public ServerOffloaderHeartbeatExecutor(HServer server) {
		mServer = server;
	}

	@Override
	public void heartbeat() {
		System.out.println("ServerOffloader heartbeating!");
		mServer.offloadWorkloads();
	}

}
