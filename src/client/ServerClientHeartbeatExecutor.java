package client;

import newhybrid.HeartbeatExecutor;
import server.ServerClient;

public class ServerClientHeartbeatExecutor implements HeartbeatExecutor {
	final private ServerClient mServerClient;
	final private long mTimeout;

	public ServerClientHeartbeatExecutor(ServerClient serverClient, long timeout) {
		mServerClient = serverClient;
		mTimeout = timeout;
	}

	@Override
	public void heartbeat() {
		long nowTime = System.nanoTime();
		long lastTime = mServerClient.getLastAccessTime();
		long deltaTime = nowTime - lastTime;
		if (deltaTime > mTimeout) {
			mServerClient.cleanConnect();
		}
	}
}
