package test;

import org.apache.log4j.Logger;

import config.Constants;
import newhybrid.HException;
import client.ServerClient;

public class TestServerStop {
	final static private Logger LOG = Logger.getLogger(Constants.LOGGER_NAME);

	public static void main(String[] args) {
		try {
			new ServerClient().serverStop();
			LOG.info("server successfully stopped");
		} catch (HException e) {
			LOG.error(e.getMessage());
			return;
		}
	}
}
