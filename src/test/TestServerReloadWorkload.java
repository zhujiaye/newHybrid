package test;

import org.apache.log4j.Logger;

import config.Constants;
import newhybrid.HException;
import client.ServerClient;

public class TestServerReloadWorkload {
	final static private Logger LOG = Logger.getLogger(Constants.LOGGER_NAME);

	public static void main(String[] args) {
		if (args.length < 1) {
			LOG.error("invalid arguments");
			return;
		}
		try {
			if (new ServerClient().serverReloadWorkloadFile(args[0]))
				LOG.info("server successfully reload workload from " + args[0]);
			else
				LOG.error("server failed to reload workload from " + args[0]);
		} catch (HException e) {
			LOG.error(e.getMessage());
			return;
		}
	}

}
