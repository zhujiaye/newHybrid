package config;

public class Constants {
	final public static long S = 1000000000;
	final public static long MIN = 60 * S;
	final public static long HOUR = 60 * MIN;
	final public static long DAY = 24 * HOUR;

	final public static String MYSQL_FLAG = "mysql";
	final public static String VOLTDB_FLAG = "voltdb";
	/*
	 * All following is just for generating random tuples to select or update
	 */
	final public static int min_ware = 1;
	final public static int max_ware = 10;
	final public static int num_ware = 1;
	final public static int num_node = 0;
	final public static int arg_offset = 0;
	/*********************
	 * associated with datasize
	 ***********************************/
	final public static int[] MAXITEMS = { 1000, 3000, 5000 };
	final public static int[] CUST_PER_DIST = { 30, 50, 50 };
	final public static int[] DIST_PER_WARE = { 3, 5, 5 };
	final public static int[] ORD_PER_DIST = { 30, 50, 50 };
	final public static int MAX_NUM_ITEM = 15;
	final public static int MAX_ITEM_LEN = 24;

	/*
	 * For thrift
	 */

	final public static int MAX_CONNECT_TRY = 5;

	/*
	 * For ServerOffloader
	 */
	final public static long OFFLOADER_FIXED_INTERVAL_TIME = 5 * S;
	final public static int OFFLOADER_BATCH = 200;

	final public static long WORKER_HEARTBEAT_INTERVAL_TIME = 5 * S;

	final public static long SPLIT_TIME = 60 * S;
	final public static long NUMBEROF_AHEAD_SPLITS = 10;

	final public static String LOGGER_NAME = System.getProperty("newhybrid.logname", "");
	final public static String LOGGER_DIR = System.getProperty("newhybrid.logdir", "");
	final public static String WORKLOAD_DIR = System.getProperty("newhybrid.workloaddir", "");
	/*
	 * Configurable system variable's default value
	 */
	final public static int DEFAULT_SERVER_CLIENT_CONNECT_TIMEOUT_S = 30;
	final public static int DEFAULT_WORKER_CLIENT_CONNECT_TIMEOUT_S = 30;
	final public static int DEFAULT_SERVER_PORT = 12345;
	final public static int DEFAULT_WORKER_PORT = 54321;
	final public static String DEFAULT_TEMP_FOLDER = "/tmp";
	final public static Boolean DEFAULT_USE_MEMMONITOR = false;
	final public static Boolean DEFAULT_MODEL_DETERMINISTIC = false;
	final public static String DEFAULT_MYSQL_USERNAME = "remote";
	final public static String DEFAULT_MYSQL_PASSWORD = "remote";
	final public static int DEFAULT_VOLTDB_CAPACITY_MB = 2000;

	/*
	 * Configurable system variable's default value for thrift
	 */
	final public static int DEFAULT_THRIFT_SELECTOR_THREADS = 3;
	final public static int DEFAULT_THRIFT_QUEUE_SIZE_PER_SELECTOR = 4000;
	final public static int DEFAULT_THRIFT_SERVER_THREADS = 4000;
}
