package test;

import newhybrid.HException;

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
		try {
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
			if (db.equals("mysql"))
				serverClient.serverReconfigure(true, capacity);
			else if (db.equals("hybrid"))
				serverClient.serverReconfigure(false, capacity);
			else {
				LOG.error("neither mysql or hybrid!");
				LOG.info(usage);
				serverClient.shutdown();
				return;
			}
			LOG.info("server reconfigure " + db + " " + capacity);
		} catch (HException e) {
			LOG.error(e.getMessage());
			if (serverClient != null)
				try {
					serverClient.shutdown();
				} catch (HException e1) {
					LOG.error(e.getMessage());
				}
			return;
		}
	}

}
