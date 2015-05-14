package config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class HConfig {
	final private static String ENV_FILEPATH = "newhybrid-env";

	private static HConfig conf = null;
	private static boolean mUsemysql = true;
	private static boolean mUsevoltdb = true;
	private static String mInitdb = "mysql";
	private static String mServerAddress;
	private static int mServerPort = 12345;
	private static boolean mSeverUseMemmonitor = true;
	private static boolean mServerModelDeterministic = false;
	private static String mMysqlServerAddress;
	private static String mMysqlDbname;
	private static String mMysqlUsername = "remote";
	private static String mMysqlPassword = "remote";
	private static String mVoltdbServerAddress;
	private static int mVoltdbCapacity;
	private static String mMysqlTempFolder = "/tmp";
	private static int mMysqlExportConcurrency = 300;
	private static int mMysqlTableConcurrency = 9;
	// For test only
	private static int mMysqlPoolInitsize = 40;
	private static int mVoltdbPoolInitsize = 0;
	private static String mWorkloadfilepath;
	private static int mNumberOfVoltdbTables = 50;
	private static int mNumberOfIntervals = 7;
	private static int mNumberOfSplits = 5;
	private static long mSplitTime = 60 * Constants.S;

	public synchronized static HConfig getConf() throws IOException {
		if (conf == null) {
			conf = new HConfig();
		}
		return conf;
	}

	private HConfig() throws IOException {
		Scanner in = null;
		try {
			in = new Scanner(new File(ENV_FILEPATH));
			while (in.hasNextLine()) {
				String line = in.nextLine();
				String[] strs = line.trim().split(" ");
				if (strs.length < 1 || strs[0].startsWith("#")
						|| strs[0].startsWith("//"))
					continue;
				if (strs[0].equals("newhybrid.usemysql")) {
					mUsemysql = Boolean.valueOf(strs[1]);
				}
				if (strs[0].equals("newhybrid.usevoltdb")) {
					mUsevoltdb = Boolean.valueOf(strs[1]);
				}
				if (strs[0].equals("newhybrid.initdb")) {
					mInitdb = strs[1];
				}
				if (strs[0].equals("newhybrid.server.address")) {
					mServerAddress = strs[1];
				}
				if (strs[0].equals("newhybrid.server.port")) {
					mServerPort = Integer.valueOf(strs[1]);
				}
				if (strs[0].equals("newhybrid.mysql.server.address")) {
					mMysqlServerAddress = "jdbc:mysql://" + strs[1];
				}
				if (strs[0].equals("newhybrid.mysql.server.dbname")) {
					mMysqlDbname = strs[1];
				}
				if (strs[0].equals("newhybrid.mysql.server.username")) {
					mMysqlUsername = strs[1];
				}
				if (strs[0].equals("newhybrid.mysql.server.password")) {
					mMysqlPassword = strs[1];
				}
				if (strs[0].equals("newhybrid.voltdb.server.address")) {
					mVoltdbServerAddress = strs[1];
				}
				if (strs[0].equals("newhybrid.voltdb.server.capacity")) {
					mVoltdbCapacity = Integer.valueOf(strs[1]);
				}

				if (strs[0].equals("newhybrid.mysql.tempfolder")) {
					mMysqlTempFolder = strs[1];
				}
				if (strs[0].equals("newhybrid.mysql.export.concurrency")) {
					mMysqlExportConcurrency = Integer.valueOf(strs[1]);
				}

				if (strs[0].equals("newhybrid.mysql.table.concurrency")) {
					mMysqlTableConcurrency = Integer.valueOf(strs[1]);
				}
				if (strs[0].equals("newhybrid.mysql.pool.initsize")) {
					mMysqlPoolInitsize = Integer.valueOf(strs[1]);
				}
				if (strs[0].equals("newhybrid.voltdb.pool.initsize")) {
					mVoltdbPoolInitsize = Integer.valueOf(strs[1]);
				}
				if (strs[0].equals("newhybrid.test.workloadfile.path")) {
					mWorkloadfilepath = strs[1];
				}
				if (strs[0].equals("newhybrid.server.model.isdeterministic")) {
					mServerModelDeterministic = Boolean.valueOf(strs[1]);
				}
				if (strs[0].equals("newhybrid.sever.usememmonitor")) {
					mSeverUseMemmonitor = Boolean.valueOf(strs[1]);
				}
				if (strs[0].equals("newhybrid.test.voltdbtable.number")) {
					mNumberOfVoltdbTables = Integer.valueOf(strs[1]);
				}
				if (strs[0].equals("newhybrid.test.interval.number")) {
					mNumberOfIntervals = Integer.valueOf(strs[1]);
				}
				if (strs[0].equals("newhybrid.test.split.number")) {
					mNumberOfSplits = Integer.valueOf(strs[1]);
				}
				if (strs[0].equals("newhybrid.test.split.time")) {
					mSplitTime = Long.valueOf(strs[1]);
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			throw new IOException("Can not find environment file!");
		} finally {
			if (in != null)
				in.close();
		}
	}

	public boolean isUseMysql() {
		return mUsemysql;
	}

	public boolean isUseVoltdb() {
		return mUsevoltdb;
	}

	public String getInitdb() {
		return mInitdb;
	}

	public String getServerAddress() {
		return mServerAddress;
	}

	public int getServerPort() {
		return mServerPort;
	}

	public boolean isServerModelDeterministic() {
		return mServerModelDeterministic;
	}

	public boolean isUseServerMemmonitor() {
		return mSeverUseMemmonitor;
	}

	public String getMysqlServerAddress() {
		return mMysqlServerAddress;
	}

	public String getMysqlDbname() {
		return mMysqlDbname;
	}

	public String getMysqlUsername() {
		return mMysqlUsername;
	}

	public String getMysqlPassword() {
		return mMysqlPassword;
	}

	public String getMysqlTempFolder() {
		return mMysqlTempFolder;
	}

	public int getMysqlExportConcurrency() {
		return mMysqlExportConcurrency;
	}

	public int getMysqlTableConcurrency() {
		return mMysqlTableConcurrency;
	}

	public int getMysqlPoolInitsize() {
		return mMysqlPoolInitsize;
	}

	public String getVoltdbServerAddress() {
		return mVoltdbServerAddress;
	}

	public int getVoltdbCapacity() {
		return mVoltdbCapacity;
	}

	public int getVoltdbPoolInitsize() {
		return mVoltdbPoolInitsize;
	}

	public synchronized static void print() {
		System.out.println("[NewHybrid]");
		System.out.println("\tnewhybrid.usemysql " + mUsemysql);
		System.out.println("\tnewhybrid.usevoltdb " + mUsevoltdb);
		System.out.println("\tnewhybrid.initdb " + mInitdb);
		System.out.println("[Server]");
		System.out.println("\tnewhybrid.server.address " + mServerAddress);
		System.out.println("\tnewhybrid.server.port " + mServerPort);
		System.out.println("\tnewhybrid.server.model.isdeterministic "
				+ mServerModelDeterministic);
		System.out.println("\tnewhybrid.sever.usememmonitor "
				+ mSeverUseMemmonitor);
		System.out.println("[MySQL server]");
		System.out.println("\tnewhybrid.mysql.server.address "
				+ mMysqlServerAddress);
		System.out.println("\tnewhybrid.mysql.server.dbname " + mMysqlDbname);
		System.out.println("\tnewhybrid.mysql.server.username "
				+ mMysqlUsername);
		System.out.println("\tnewhybrid.mysql.server.password "
				+ mMysqlPassword);
		System.out.println("\tnewhybrid.mysql.tempfolder " + mMysqlTempFolder);
		System.out.println("\tnewhybrid.mysql.export.concurrency "
				+ mMysqlExportConcurrency);
		System.out.println("\tnewhybrid.mysql.table.concurrency "
				+ mMysqlTableConcurrency);
		System.out.println("\tnewhybrid.mysql.pool.initsize "
				+ mMysqlPoolInitsize);
		System.out.println("[VoltDB server]");
		System.out.println("\tnewhybrid.voltdb.server.address "
				+ mVoltdbServerAddress);
		System.out.println("\tnewhybrid.voltdb.server.capacity "
				+ mVoltdbCapacity);
		System.out.println("\tnewhybrid.voltdb.pool.initsize "
				+ mVoltdbPoolInitsize);
		System.out.println("[For test]");
		System.out.println("\tnewhybrid.test.workloadfile.path "
				+ mWorkloadfilepath);
		System.out.println("\tnewhybrsid.test.voltdbtable.number "
				+ mNumberOfVoltdbTables);
		System.out.println("\tnewhybrid.test.interval.number "
				+ mNumberOfIntervals);
		System.out.println("\tnewhybrid.test.split.number " + mNumberOfSplits);
		System.out.println("\tnewhybrid.test.split.time " + mSplitTime);

	}
}
