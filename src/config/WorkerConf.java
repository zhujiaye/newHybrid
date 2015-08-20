package config;

import org.apache.log4j.Logger;

import utillity.NetWorkUtils;

public class WorkerConf {
	private static Logger LOG = Logger.getLogger(Constants.LOGGER_NAME);
	private static WorkerConf conf = null;

	public final String SERVER_ADDRESS;
	public final int SERVER_PORT;
	public final long SERVER_CLIENT_CONNECT_TIMEOUT_S;

	public final String WORKER_ADDRESS;
	public final int WORKER_PORT;
	public final String WORKER_DBMS_TYPE;
	public final String WORKER_TEMP_FOLDER;
	/*
	 * mysql-type variables
	 */
	public final String MYSQL_DB_NAME;
	public final String MYSQL_COMPLETE_CONNECTION_STRING;
	public final String MYSQL_USERNAME;
	public final String MYSQL_PASSWORD;

	/*
	 * voltdb-type variables
	 */
	public final String VOLTDB_COMPLETE_CONNECTION_STRING;
	public final int VOLTDB_CAPACITY_MB;

	public synchronized static WorkerConf getConf() {
		if (conf == null) {
			conf = new WorkerConf();
		}
		return conf;
	}

	private WorkerConf() {
		SERVER_ADDRESS = System.getProperty("newhybrid.server.address");
		if (SERVER_ADDRESS == null) {
			LOG.error("server address must be defined");
			System.exit(1);
		}
		SERVER_PORT = Integer.valueOf(System.getProperty("newhybrid.server.port", Constants.DEFAULT_SERVER_PORT + ""));
		SERVER_CLIENT_CONNECT_TIMEOUT_S = Long.valueOf(System.getProperty("newhybrid.server.client.connect.timeout.s",
				Constants.DEFAULT_SERVER_CLIENT_CONNECT_TIMEOUT_S + ""));
		WORKER_ADDRESS = NetWorkUtils.getLocalIpAddress();
		WORKER_PORT = Integer.valueOf(System.getProperty("newhybrid.worker.port", Constants.DEFAULT_WORKER_PORT + ""));
		WORKER_DBMS_TYPE = System.getProperty("newhybrid.worker.dbms.type");
		if (WORKER_DBMS_TYPE == null) {
			LOG.error("worker dbms type must be defined");
			System.exit(1);
		}
		WORKER_TEMP_FOLDER = System.getProperty("newhybrid.temp.folder", Constants.DEFAULT_TEMP_FOLDER);
		MYSQL_DB_NAME = System.getProperty("newhybrid.mysql.db.name", "");
		MYSQL_COMPLETE_CONNECTION_STRING = "jdbc:mysql://" + WORKER_ADDRESS + "/" + MYSQL_DB_NAME;
		MYSQL_USERNAME = System.getProperty("newhybrid.mysql.username", Constants.DEFAULT_MYSQL_USERNAME);
		MYSQL_PASSWORD = System.getProperty("newhybrid.mysql.password", Constants.DEFAULT_MYSQL_PASSWORD);
		VOLTDB_CAPACITY_MB = Integer
				.valueOf(System.getProperty("newhybrid.voltdb.capacity.mb", Constants.DEFAULT_VOLTDB_CAPACITY_MB + ""));
		VOLTDB_COMPLETE_CONNECTION_STRING = WORKER_ADDRESS;
	}

	/**
	 * just for test
	 */
	public void print() {
		System.out.println("SERVER_ADDRESS:"+SERVER_ADDRESS);
		System.out.println("SERVER_PORT:"+SERVER_PORT);
		System.out.println("SERVER_CLIENT_CONNECT_TIMEOUT_S:"+SERVER_CLIENT_CONNECT_TIMEOUT_S);
		System.out.println("WORKER_ADDRESS:"+WORKER_ADDRESS);
		System.out.println("WORKER_PORT:"+WORKER_PORT);
		System.out.println("WORKER_DBMS_TYPE:"+WORKER_DBMS_TYPE);
		System.out.println("WORKER_TEMP_FOLDER:"+WORKER_TEMP_FOLDER);
		System.out.println("MYSQL_DB_NAME:"+MYSQL_DB_NAME);
		System.out.println("MYSQL_COMPLETE_CONNECTION_STRING:"+MYSQL_COMPLETE_CONNECTION_STRING);
		System.out.println("MYSQL_USERNAME:"+MYSQL_USERNAME);
		System.out.println("MYSQL_PASSWORD:"+MYSQL_PASSWORD);
		System.out.println("VOLTDB_CAPACITY_MB:"+VOLTDB_CAPACITY_MB);
		System.out.println("VOLTDB_COMPLETE_CONNECTION_STRING:"+VOLTDB_COMPLETE_CONNECTION_STRING);
	}
}
