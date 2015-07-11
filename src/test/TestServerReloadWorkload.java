package test;

import org.apache.log4j.Logger;

import config.Constants;
import newhybrid.ClientShutdownException;
import client.ServerClient;

public class TestServerReloadWorkload {
	final static private Logger LOG = Logger.getLogger(Constants.LOGGER_NAME);

	public static void main(String[] args) {
		if (args.length < 1) {
			LOG.error("invalid arguments");
			return;
		}
		ServerClient serverClient = null;
		serverClient = new ServerClient();
		while (true) {
			try {
				if (serverClient.serverReloadWorkloadFile(args[0]))
					LOG.info("server successfully reload workload from "
							+ args[0]);
				else
					LOG.error("server failed to reload workload from "
							+ args[0]);
				serverClient.shutdown();
			} catch (ClientShutdownException e) {
				LOG.warn("server client is already shut down, try to get a new one");
				serverClient.shutdown();
				serverClient = new ServerClient();
			}
		}
	}

}
