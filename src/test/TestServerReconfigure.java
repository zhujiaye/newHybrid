package test;

import newhybrid.ClientShutdownException;

import org.apache.log4j.Logger;

import client.ServerClient;
import config.Constants;

public class TestServerReconfigure {
	final static private Logger LOG = Logger.getLogger(Constants.LOGGER_NAME);

	public static void main(String[] args) {
		String usage = "Usage: TestServerReconfigure [mysql/hybrid] voltdbcapacity(in MB)";
		if (args.length < 2) {
			LOG.error("invalid arguments");
			LOG.info(usage);
			return;
		}
		ServerClient serverClient = null;
		serverClient = new ServerClient();
		String db = args[0];
		int capacity = 0;
		try {
			capacity = Integer.valueOf(args[1]);
		} catch (NumberFormatException e) {
			LOG.error("wrong number format!");
			LOG.info(usage);
			serverClient.shutdown();
			return;
		}
		while (true) {
			try {
				if (db.equals("mysql")) {
					serverClient.serverReconfigure(true, capacity);
					break;
				} else if (db.equals("hybrid")) {
					serverClient.serverReconfigure(false, capacity);
					break;
				} else {
					LOG.error("neither mysql or hybrid!");
					LOG.info(usage);
					serverClient.shutdown();
					return;
				}
			} catch (ClientShutdownException e) {
				LOG.warn("server client is already shut down, try to get a new one");
				serverClient.shutdown();
				serverClient = new ServerClient();
			}
		}
		LOG.info("server reconfigure " + db + " " + capacity);
	}
}
