package config;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import newhybrid.HException;

public class HConfig {
	final private String ENV_FILEPATH = System.getProperty("newhybrid.envpath",
			"newhybrid-env");

	private static HConfig conf = null;
	private boolean mUsemysql = Constants.DEFAULT_USE_MYSQL;
	private boolean mUsevoltdb = Constants.DEFAULT_USE_VOLTDB;
	private String mInitdb = Constants.DEFAULT_INITDB;
	private String mServerAddress;
	private int mServerPort = Constants.DEFAULT_SERVER_PORT;
	private boolean mSeverUseMemmonitor = Constants.DEFAULT_USE_MEMMONITOR;
	private boolean mServerModelDeterministic = Constants.DEFAULT_MODEL_DETERMINISTIC;
	private String mMysqlServerAddress;
	private String mMysqlDbname;
	private String mMysqlUsername = Constants.DEFAULT_MYSQL_USERNAME;
	private String mMysqlPassword = Constants.DEFAULT_MYSQL_PASSWORD;
	private String mVoltdbServerAddress;
	private int mVoltdbCapacity;
	private String mMysqlTempFolder = Constants.DEFAULT_MYSQL_TMP_FOLDER;
	private long mServerClientTimeout = Constants.DEFAULT_SERVERCLIENT_CONNECT_TIMEOUT;
	// For test only
	private int mMysqlPoolInitsize = Constants.DEFAULT_MYSQL_POOL_INITSIZE;
	private int mVoltdbPoolInitsize = Constants.DEFAULT_VOLTDB_POOL_INITSIZE;
	private String mWorkloadfilepath;

	public synchronized static HConfig getConf() throws HException {
		if (conf == null) {
			conf = new HConfig();
		}
		return conf;
	}

	private HConfig() throws HException {
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
				if (strs[0].equals("newhybrid.server.timeout")) {
					mServerClientTimeout = Long.valueOf(strs[1]);
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
			}
		} catch (FileNotFoundException e) {
			throw new HException("Can not find environment file!");
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

	public String getMysqlCompleteAddress() {
		return mMysqlServerAddress + "/" + mMysqlDbname;
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

	public long getServerClientTimeout() {
		return mServerClientTimeout;
	}

	public synchronized void print() {
		System.out.println("[NewHybrid]");
		System.out.println("\tnewhybrid.usemysql " + mUsemysql);
		System.out.println("\tnewhybrid.usevoltdb " + mUsevoltdb);
		System.out.println("\tnewhybrid.initdb " + mInitdb);
		System.out.println("[Server]");
		System.out
				.println("\tnewhybrid.server.timeout " + mServerClientTimeout);
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

	}
}
