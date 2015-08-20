package config;

import org.apache.log4j.Logger;

public class ClientConf {
	private final static Logger LOG = Logger.getLogger(Constants.LOGGER_NAME);
	private static ClientConf conf = null;
	public final String SERVER_ADDRESS;
	public final int SERVER_PORT;
	public final long SERVER_CLIENT_CONNECT_TIMEOUT_S;

	public synchronized static ClientConf getConf() {
		if (conf == null) {
			conf = new ClientConf();
		}
		return conf;
	}

	private ClientConf() {
		SERVER_ADDRESS = System.getProperty("newhybrid.server.address");
		if (SERVER_ADDRESS == null) {
			LOG.error("server address must be defined");
			System.exit(1);
		}
		SERVER_PORT = Integer.valueOf(System.getProperty("newhybrid.server.port", Constants.DEFAULT_SERVER_PORT + ""));
		SERVER_CLIENT_CONNECT_TIMEOUT_S = Long.valueOf(System.getProperty(
				"newhybrid.server.client.connect.timeout.s", Constants.DEFAULT_SERVER_CLIENT_CONNECT_TIMEOUT_S + ""));
	}

	/**
	 * just for test
	 */
	public void print() {
		System.out.println("SERVER_ADDRESS:" + SERVER_ADDRESS);
		System.out.println("SERVER_PORT:" + SERVER_PORT);
		System.out.println("SERVER_CLIENT_CONNECT_TIMEOUT_S:" + SERVER_CLIENT_CONNECT_TIMEOUT_S);
	}
}
