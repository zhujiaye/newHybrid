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
	final public static int MAXITEMS = 8000;
	final public static int CUST_PER_DIST = 100;
	final public static int DIST_PER_WARE = 10;
	final public static int ORD_PER_DIST = 100;

	final public static int MAX_NUM_ITEM = 15;
	final public static int MAX_ITEM_LEN = 24;

	/*
	 * For thrift
	 */
	final public static int SERVER_PORT = 12345;
	final public static long CONNECT_TIMEOUT = 5 * S;
	final public static int MAX_CONNECT_TRY = 5;
	final public static int SELECTOR_THREADS = 3;
	final public static int QUEUE_SIZE_PER_SELECTOR = 3000;
	final public static int SERVER_THREADS = 128;
}
