package server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import thrift.ServerWorkerInfo;

public class ServerInfo {
	private final String SERVER_ADDRESS;
	private final int SERVER_PORT;

	private Map<Integer, ServerTenant> mTenants;

	private ArrayList<ServerWorkerInfo> mWorkers;

	public ServerInfo(String serverAddress, int serverPort) {
		SERVER_ADDRESS = serverAddress;
		SERVER_PORT = serverPort;
		mTenants = new HashMap<>();
		mWorkers = new ArrayList<>();
	}

	public void init() {
		// TODO load image here
	}
}
