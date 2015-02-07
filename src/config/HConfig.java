package config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class HConfig {
	public static boolean debug = false;

	public static String urlOfMySQL = "jdbc:mysql://10.171.5.28/tpcc3000";
	final public static String userOfMySQL = "remote";
	final public static String passwordOfMySQL = "remote";

	final public static int TOTTENANTS = 3000;
	final public static long INTERVAL_TIME = 5 * 60 * 1000;
	final public static int INTERVAL_TIME_IN_MINUTES = 5;

	final public static int SPLIT_TIME = 1 * 60 * 1000;
	final public static int SPLIT_TIME_IN_MINUTES = 1;
	final public static int NUMBER_OF_SPLITS_IN_INTERVAL = 5;

	final public static int ACTIVE_RATIO = 20;
	final public static int ACTIVE_REMOVE_RATIO = 10;
	final public static int ACTIVE_ADD_RATIO = ACTIVE_RATIO
			* ACTIVE_REMOVE_RATIO / (100 - ACTIVE_RATIO);
	final public static int NUMBER_OF_INTERVAL = 7;
	final public static int NUMBER_OF_BURST_INTERVAL = 2;
	final public static boolean[] ISBURST = { false, false, false, true, true,
			false, false };

	public static String INFO_FILE = "tenants_info";
	public static String WL_FILE = "load_a40.txt";
	final public static int[][] DISTRIBUTION_OF_SLO = { { 30, 20 }, { 50, 60 },
			{ 20, 200 } };
	final public static int[][] DISTRIBUTION_OF_DATASIZE = { { 1, 1500, 7 },
			{ 1501, 2400, 16 }, { 2401, 3000, 35 } };
	final public static int[][] DISTRIBUTION_OF_WRITEHEAVY = { { 40, 60 },
			{ 60, 40 } };
	final public static int NUMBER_OF_DISTRIBUTION_OF_SLO = 3;
	final public static int NUMBER_OF_DISTRIBUTION_OF_DATASIZE = 3;
	final public static int NUMBER_OF_DISTRIBUTION_OF_WRITEHEAVY = 2;

	final public static int NUMBER_OF_TABLES = 9;
	final public static int NUMBER_OF_TYPE_OF_QUERY = 2;
	final public static int QTYPE_SELECT = 0;
	final public static int QTYPE_UPDATE = 1;

	final public static int SOCKET_PORT = 12345;
	public static String SOCKET_HOST = "10.171.5.28";

	public static String CSVPATH = "/host/tmp";
	final public static int BATCH = 100;

	final public static boolean USEVOLTDB = true;
	public static int VOLTDB_SIZE = 500;
	final public static int NUMBER_OF_ID_IN_VOLTDB = 50;
	public static String VOLTDB_SERVER = "10.171.5.28";

	final public static int CLIENT_TIME_GAP = 5;
	final public static int SERVER_TIME_GAP = 10;

	public static int MYSQLEXPORTER_CONCURRENCY = 300;
	public static int MOVER_CONCURRENCY = 300;
	public static int TABLE_CONCURRENCY = 2;
	// final public static int TABLE_CONCURRENCY = 9;

	public static int MYSQLPOOLINIT = 40;
	public static int VOLTDBPOOLINIT = 0;

	public static boolean MOVEDB = true;
	final public static int CONCURRENCY_PROCESS = 15;

	final public static String SETTING_FILE = "setting";

	public static boolean VOLTDB_TEST = false;
	public static int TOT_MEM = 4000;
	public static boolean ISDETERMINED = true;

	public static void load() {
		Scanner in = null;
		try {
			in = new Scanner(new File(SETTING_FILE));
			while (in.hasNextLine()) {
				String line = in.nextLine();
				String[] strs = line.split(" ");
				if (strs[0].equals("MOVEDB")) {
					MOVEDB = Boolean.valueOf(strs[1]);
				}
				if (strs[0].equals("WL_FILE")) {
					WL_FILE = strs[1];
				}
				if (strs[0].equals("VOLTDB_SIZE")) {
					VOLTDB_SIZE = Integer.valueOf(strs[1]);
				}
				if (strs[0].equals("SERVER_ADDRESS")) {
					urlOfMySQL = "jdbc:mysql://" + strs[1] + "/tpcc3000";
					SOCKET_HOST = strs[1];
					VOLTDB_SERVER = strs[1];
				}
				if (strs[0].equals("CSVPATH")) {
					CSVPATH = strs[1];
				}
				if (strs[0].equals("MOVER_CONCURRENCY")) {
					MOVER_CONCURRENCY = Integer.valueOf(strs[1]);
				}
				if (strs[0].equals("MYSQLPOOLINIT")) {
					MYSQLPOOLINIT = Integer.valueOf(strs[1]);
				}
				if (strs[0].equals("VOLTDBPOOLINIT")) {
					VOLTDBPOOLINIT = Integer.valueOf(strs[1]);
				}
				if (strs[0].equals("INFO_FILE")) {
					INFO_FILE = strs[1];
				}
				if (strs[0].equals("debug")) {
					debug = Boolean.valueOf(strs[1]);
				}
				if (strs[0].equals("TABLE_CONCURRENCY")) {
					TABLE_CONCURRENCY = Integer.valueOf(strs[1]);
				}
				if (strs[0].equals("MYSQLEXPORTER_CONCURRENCY")) {
					MYSQLEXPORTER_CONCURRENCY = Integer.valueOf(strs[1]);
				}
				if (strs[0].equals("VOLTDB_TEST")) {
					VOLTDB_TEST = Boolean.valueOf(strs[1]);
				}
				if (strs[0].equals("TOT_MEM")) {
					TOT_MEM = Integer.valueOf(strs[1]);
				}
				if (strs[0].equals("ISDETERMINED")) {
					ISDETERMINED = Boolean.valueOf(strs[1]);
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void print() {
		System.out.println("WL_FILE:" + WL_FILE);
		System.out.println("MOVEDB:" + MOVEDB);
		System.out.println("VOLTDB_SIZE:" + VOLTDB_SIZE);
		System.out.println("urlOfMySQL:" + urlOfMySQL);
		System.out.println("SOCKET_HOST:" + SOCKET_HOST);
		System.out.println("VOLTDB_SERVER:" + VOLTDB_SERVER);
		System.out.println("CSVPATH:" + CSVPATH);
		System.out.println("MOVER_CONCURRENCY:" + MOVER_CONCURRENCY);
		System.out.println("TABLE_CONCURRENCY:" + TABLE_CONCURRENCY);
		System.out.println("MYSQLEXPORTER_CONCURRENCY:"
				+ MYSQLEXPORTER_CONCURRENCY);
		System.out.println("MYSQLPOOLINIT:" + MYSQLPOOLINIT);
		System.out.println("VOLTDBPOOLINIT:" + VOLTDBPOOLINIT);
		System.out.println("INFO_FILE:" + INFO_FILE);
		System.out.println("debug:" + debug);
		System.out.println("VOLTDB_TEST:" + VOLTDB_TEST);
		System.out.println("TOT_MEM:" + TOT_MEM);
		System.out.println("ISDETERMINED:" + ISDETERMINED);
	}
}
