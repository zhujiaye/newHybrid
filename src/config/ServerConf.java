package config;

import org.apache.log4j.Logger;

public class ServerConf {
	private final static Logger LOG = Logger.getLogger(Constants.LOGGER_NAME);
	private static ServerConf conf = null;
	public final String SERVER_ADDRESS;
	public final int SERVER_PORT;
	public final boolean SERVER_USE_MEMMONITOR;
	public final boolean MODEL_DETERMINISTIC;
	public final int SERVER_SELECTOR_THREADS;
	public final int SERVER_QUEUE_SIZE_PER_SELECTOR;
	public final int SERVER_SERVER_THREADS;
	public final long SERVER_WORKER_CLIENT_CONNECT_TIMEOUT_S;
	public final String SERVER_IMAGE_PATH;

	public synchronized static ServerConf getConf() {
		if (conf == null) {
			conf = new ServerConf();
		}
		return conf;
	}

	private ServerConf() {
		SERVER_ADDRESS = System.getProperty("newhybrid.server.address");
		if (SERVER_ADDRESS == null) {
			LOG.error("server address must be defined");
			System.exit(1);
		}
		SERVER_PORT = Integer.valueOf(System.getProperty("newhybrid.server.port", Constants.DEFAULT_SERVER_PORT + ""));
		SERVER_USE_MEMMONITOR = Boolean
				.valueOf(System.getProperty("newhybrid.server.use.memmonitor", Constants.DEFAULT_USE_MEMMONITOR + ""));
		MODEL_DETERMINISTIC = Boolean.valueOf(
				System.getProperty("newhybrid.model.deterministic", Constants.DEFAULT_MODEL_DETERMINISTIC + ""));
		SERVER_SELECTOR_THREADS = Integer.valueOf(System.getProperty("newhybrid.thrift.selector.threads",
				Constants.DEFAULT_THRIFT_SELECTOR_THREADS + ""));
		SERVER_QUEUE_SIZE_PER_SELECTOR = Integer.valueOf(System.getProperty("newhybrid.thrift.queue.size.per.selector",
				Constants.DEFAULT_THRIFT_QUEUE_SIZE_PER_SELECTOR + ""));
		SERVER_SERVER_THREADS = Integer.valueOf(
				System.getProperty("newhybrid.thrift.server.threads", Constants.DEFAULT_THRIFT_SERVER_THREADS + ""));
		SERVER_WORKER_CLIENT_CONNECT_TIMEOUT_S = Long.valueOf(System.getProperty(
				"newhybrid.worker.client.connect.timeout.s", Constants.DEFAULT_WORKER_CLIENT_CONNECT_TIMEOUT_S + ""));
		SERVER_IMAGE_PATH = System.getProperty("newhybrid.server.image.path", "image");
	}

	/**
	 * just for test
	 */
	public void print() {
		System.out.println("SERVER_ADDRESS:" + SERVER_ADDRESS);
		System.out.println("SERVER_PORT:" + SERVER_PORT);
		System.out.println("SERVER_USE_MEMMONITOR:" + SERVER_USE_MEMMONITOR);
		System.out.println("MODEL_DETERMINISTIC:" + MODEL_DETERMINISTIC);
		System.out.println("SERVER_SELECTOR_THREADS:" + SERVER_SELECTOR_THREADS);
		System.out.println("SERVER_QUEUE_SIZE_PER_SELECTOR:" + SERVER_QUEUE_SIZE_PER_SELECTOR);
		System.out.println("SERVER_SERVER_THREADS:" + SERVER_SERVER_THREADS);
		System.out.println("SERVER_WORKER_CLIENT_CONNECT_TIMEOUT_S:" + SERVER_WORKER_CLIENT_CONNECT_TIMEOUT_S);
		System.out.println("SERVER_IMAGE_PATH:" + SERVER_IMAGE_PATH);
	}
}
