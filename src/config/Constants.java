package config;

public class Constants {
	final public static long S = 1000000000;
	final public static long MIN = 60 * S;
	final public static long HOUR = 60 * MIN;
	final public static long DAY = 24 * HOUR;

	/*
	 * This variable is just for test,actually every tenants have different
	 * number of tables;
	 */
	final public static int NUMBER_OF_TABLES = 9;
	final public static int NUMBER_OF_TENANTS = 3000;
	final public static int NUMBER_OF_TYPE_OF_QUERY = 2;
	final public static int QTYPE_SELECT = 0;
	final public static int QTYPE_UPDATE = 1;

	/*
	 * All following is just for generating random tuples to select or update
	 */
	final public static int min_ware = 1;
	final public static int max_ware = 10;
	final public static int num_ware = 1;
	final public static int num_node = 0;
	final public static int arg_offset = 0;
	/********************* associated with datasize ***********************************/
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
	final public static int SELECTOR_THREADS = 3;
	final public static int QUEUE_SIZE_PER_SELECTOR = 4000;
	final public static int SERVER_THREADS = 4000;

	/*
	 * For ServerOffloader
	 */
	final public static long OFFLOADER_FIXED_INTERVAL_TIME = 5 * S;
	final public static int OFFLOADER_BATCH = 200;
	/*
	 * Number of Voltdb ID, just for test
	 */
	final public static int NUMBER_OF_VOLTDBID = 50;
	/*
	 * Flags for initial database mysql or voltdb
	 */
	final public static String INITDB_MYSQL = "mysql";
	final public static String INITDB_VOLTDB = "voltdb";

	final public static long SPLIT_TIME = 60 * S;
	final public static long NUMBEROF_AHEAD_SPLITS = 10;

	final public static String LOGGER_NAME = System.getProperty(
			"newhybrid.logger.name", "");
	final public static String LOGGER_DIR = System.getProperty(
			"newhybrid.logdir", "");
	final public static String LOGGER_NAME_SERVER = "SERVER_LOGGER";
	final public static String LOGGER_NAME_CLIENT = "CLIENT_LOGGER";
	final public static String WORKLOAD_FILE = System.getProperty(
			"newhybrid.workloadfile", "load550500_0.250.1_test1.txt");
	final public static String WORKLOAD_DIR = System.getProperty(
			"newhybrid.workloaddir", "");
	/*
	 * Configurable variable's default value in HConfig
	 */
	final public static long DEFAULT_SERVERCLIENT_CONNECT_TIMEOUT = 30 * S;
	final public static int DEFAULT_SERVER_PORT = 12345;
	final public static String DEFAULT_MYSQL_TMP_FOLDER = "/tmp";
	final public static Boolean DEFAULT_USE_MYSQL = true;
	final public static Boolean DEFAULT_USE_VOLTDB = true;
	final public static String DEFAULT_INITDB = INITDB_MYSQL;
	final public static Boolean DEFAULT_USE_MEMMONITOR = true;
	final public static Boolean DEFAULT_MODEL_DETERMINISTIC = false;
	final public static String DEFAULT_MYSQL_USERNAME = "remote";
	final public static String DEFAULT_MYSQL_PASSWORD = "remote";
	final public static int DEFAULT_MYSQL_POOL_INITSIZE = 40;
	final public static int DEFAULT_VOLTDB_POOL_INITSIZE = 0;
}
