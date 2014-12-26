package client;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import javax.naming.spi.DirectoryManager;
import javax.swing.plaf.SliderUI;

import org.voltdb.VoltTable;
import org.voltdb.client.Client;
import org.voltdb.client.ClientFactory;

import config.HConfig;
import dbInfo.CustomerTable;
import dbInfo.DistrictTable;
import dbInfo.HistoryTable;
import dbInfo.ItemTable;
import dbInfo.NewOrdersTable;
import dbInfo.OrderLineTable;
import dbInfo.OrdersTable;
import dbInfo.StockTable;
import dbInfo.Table;
import dbInfo.WarehouseTable;
import utillity.HTenantDynamicInfo;
import utillity.MySQLConnectionPool;
import utillity.PossionDistribution;
import utillity.VoltDBConnectionPool;

public class HTenant implements Runnable {
	private int id;
	private int SLO;
	private int dataSize;
	private int writeHeavy;

	private boolean usingVoltDB = false;
	private boolean isToUseVoltDB = false;
	private int isToUseVoltDBID;
	private int useVoltDBID;
	private boolean isToUseMySQLDB = false;
	private Connection mySQLConn = null;
	private Client voltDBConn = null;

	private Random random;
	private boolean isActive = false;
	private boolean isBurst = false;
	private long startTime;
	private int interval;
	private int split;

	private int remainQuerys;
	private int sentQuerys;
	private int doneQuerys;

	private Table[] tables;

	private Monitor monitor;
	private MySQLConnectionPool mySQLpool;
	private VoltDBConnectionPool voltDBPool;

	private ArrayList<SplitInfo> splitInfoList;
	private ArrayList<QueryInfo> queryInfoList;
	private int lastRemainQuerys;
	private int workload;

	private boolean[] activeInfo;
	private int[] workloadInfo;

	public int getEstimatedWorkLoadInBurst() {
		int res = 0;
		res += PossionDistribution.getRandomNumber(SLO);
		return res;
	}

	public int getEstimatedDataSizeInVoltDB() {
		return dataSize;
	}

	public int getID() {
		return id;
	}

	public int getSLOinfo() {
		return SLO;
	}

	public int getWriteHeavyinfo() {
		return writeHeavy;
	}

	public int getDataSizeinfo() {
		return dataSize;
	}

	private void getSLO() {
		// int percent = (int) ((double) id / HConfig.TOTTENANTS * 100.0);
		int percent = random.nextInt(100) + 1;
		int i;
		for (i = 0; i < HConfig.NUMBER_OF_DISTRIBUTION_OF_SLO; i++) {
			if (HConfig.DISTRIBUTION_OF_SLO[i][0] >= percent)
				break;
			percent -= HConfig.DISTRIBUTION_OF_SLO[i][0];
		}
		SLO = HConfig.DISTRIBUTION_OF_SLO[i][1];
	}

	private void getDataSize() {
		// int percent = (int) ((double) id / HConfig.TOTTENANTS * 100.0);
		// int i;
		// for (i = 0; i < HConfig.NUMBER_OF_DISTRIBUTION_OF_DATASIZE; i++) {
		// if (HConfig.DISTRIBUTION_OF_DATASIZE[i][0] >= percent)
		// break;
		// percent -= HConfig.DISTRIBUTION_OF_DATASIZE[i][0];
		// }
		// dataSize = HConfig.DISTRIBUTION_OF_DATASIZE[i][1];
		for (int i = 0; i < HConfig.NUMBER_OF_DISTRIBUTION_OF_DATASIZE; i++) {
			if (id >= HConfig.DISTRIBUTION_OF_DATASIZE[i][0]
					&& id <= HConfig.DISTRIBUTION_OF_DATASIZE[i][1]) {
				dataSize = HConfig.DISTRIBUTION_OF_DATASIZE[i][2];
				return;
			}
		}
	}

	private void getWriteHeavy() {
		// int percent = (int) ((double) id / HConfig.TOTTENANTS * 100.0);
		int percent = random.nextInt(100) + 1;
		int i;
		for (i = 0; i < HConfig.NUMBER_OF_DISTRIBUTION_OF_WRITEHEAVY; i++) {
			if (HConfig.DISTRIBUTION_OF_WRITEHEAVY[i][0] >= percent)
				break;
			percent -= HConfig.DISTRIBUTION_OF_WRITEHEAVY[i][0];
		}
		writeHeavy = HConfig.DISTRIBUTION_OF_WRITEHEAVY[i][1];
	}

	private void getInfo() {
		random = new Random(System.nanoTime());
		getSLO();
		getDataSize();
		getWriteHeavy();
	}

	private void getInfoFromFile() {
		// random = new Random(System.currentTimeMillis());
		// if (random.nextInt(100) <= 12) {
		// isToUseVoltDB = true;
		// isToUseVoltDBID = 0;
		// }
		Scanner reader = null;
		try {
			reader = new Scanner(new File(HConfig.INFO_FILE));
			while (reader.hasNextLine()) {
				String str = reader.nextLine();
				String[] strs = str.split(" ");
				if (Integer.valueOf(strs[0]) == id) {
					this.SLO = Integer.valueOf(strs[1]);
					this.dataSize = Integer.valueOf(strs[2]);
					this.writeHeavy = Integer.valueOf(strs[3]);
					break;
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println(HConfig.INFO_FILE
					+ " does not exist!Please call Procedure.main first!");
		} finally {
			if (reader != null)
				reader.close();
		}
		workloadInfo = new int[HConfig.NUMBER_OF_INTERVAL
				* HConfig.NUMBER_OF_SPLITS_IN_INTERVAL];
		activeInfo = new boolean[HConfig.NUMBER_OF_INTERVAL];
		for (int i = 0; i < HConfig.NUMBER_OF_INTERVAL; i++)
			activeInfo[i] = false;
		try {
			reader = new Scanner(new File(HConfig.WL_FILE));
			int c = 0;
			// active information
			while (reader.hasNextLine()) {
				String str = reader.nextLine();
				String[] strs = str.split(" ");
				c++;
				int interval = Integer.valueOf(strs[0]);
				if (interval != c) {
					System.out.println("wrong workload information WTF!!!!!!!");
					System.exit(1);
				}
				for (int i = 1; i < strs.length; i++) {
					int tmpId = Integer.valueOf(strs[i]);
					if (tmpId + 1 == id) {
						activeInfo[interval - 1] = true;
						break;
					}
				}
				if (c == HConfig.NUMBER_OF_INTERVAL)
					break;
			}
			// workload information
			c = 0;
			while (reader.hasNextLine()) {
				String str = reader.nextLine();
				String[] strs = str.split(" ");
				workloadInfo[c++] = Integer.valueOf(strs[id]);
			}
			if (c != HConfig.NUMBER_OF_INTERVAL
					* HConfig.NUMBER_OF_SPLITS_IN_INTERVAL) {
				System.out.println("wrong workload information WTF!!!!!!!");
				System.exit(1);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println(HConfig.WL_FILE
					+ " does not exist!Please call Procedure.main first!");
		} finally {
			if (reader != null)
				reader.close();
		}
		tables = new Table[HConfig.NUMBER_OF_TABLES];
		tables[0] = new CustomerTable(id);
		tables[1] = new DistrictTable(id);
		tables[2] = new HistoryTable(id);
		tables[3] = new ItemTable(id);
		tables[4] = new NewOrdersTable(id);
		tables[5] = new OrderLineTable(id);
		tables[6] = new OrdersTable(id);
		tables[7] = new StockTable(id);
		tables[8] = new WarehouseTable(id);
	}

	public HTenant(int id, Monitor monitor, MySQLConnectionPool mySQLpool,
			VoltDBConnectionPool voltDBpool) {
		this.id = id;
		this.monitor = monitor;
		this.mySQLpool = mySQLpool;
		this.voltDBPool = voltDBpool;
		lastRemainQuerys = workload = remainQuerys = 0;
		sentQuerys = 0;
		doneQuerys = 0;
		interval = 0;
		split = 0;
		random = new Random(System.nanoTime());
		splitInfoList = new ArrayList<>();
		queryInfoList = new ArrayList<>();
		getInfoFromFile();
		if (HConfig.debug) {
			System.out.format("id(%d) SLO(%d) dataSize(%d) writeHeavy(%d)%n",
					id, SLO, dataSize, writeHeavy);
			for (int i = 0; i < HConfig.NUMBER_OF_INTERVAL; i++) {
				System.out.print(activeInfo[i] + " ");
			}
			System.out.println("");
			for (int i = 0; i < HConfig.NUMBER_OF_INTERVAL
					* HConfig.NUMBER_OF_SPLITS_IN_INTERVAL; i++) {
				System.out.print(workloadInfo[i] + " ");
			}
			System.out.println("");
		}
	}

	public HTenant(int id) {
		this.id = id;
		getInfo();
		System.out.format("id(%d) SLO(%d) dataSize(%d) writeHeavy(%d)%n", id,
				SLO, dataSize, writeHeavy);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		startTime = System.nanoTime();
		// System.out.println("Tenant" + id + " started!");
		while (getInterval() <= HConfig.NUMBER_OF_INTERVAL) {
			// long tmp = System.currentTimeMillis();
			// if (tmp - startTime >= interval * HConfig.INTERVAL_TIME) {
			// updateSplitInfo(tmp - startTime);
			// updateIntervalInfo(tmp - startTime);
			// // interval++;
			// if (interval > HConfig.NUMBER_OF_INTERVAL)
			// break;
			// updateActive(interval);
			// updateBurst(interval);
			// updateQuerys(interval);
			// updateUsingDB();
			// monitor.sendInfoMessageForTenant(id, new HTenantDynamicInfo(
			// interval, usingVoltDB, useVoltDBID, isActive, isBurst));
			// // if (isActive)
			// // System.out.println("----------------------Tennat" + id
			// // + ": enter the " + interval + " interval"
			// // + " active:" + isActive + " burst:" + isBurst
			// // + "-------------------------");
			// } else if (tmp - startTime >= split * HConfig.SPLIT_TIME) {
			// updateSplitInfo(tmp - startTime);
			// updateQuerys(interval);
			// }
			if (getRemainQuerys() > 0) {
				if (random.nextInt(100) <= writeHeavy)
					doSQL(true);
				else
					doSQL(false);
			} else {
				closeMySQLConnection();
				closeVoltDBConnection();
				synchronized (monitor) {
					try {
						// System.out.println("Tenant " + id +
						// " start waiting");
						monitor.addWatingTenants();
						// if (isUsingVoltDB())
						// monitor.addWaitingTenantsForV();
						monitor.wait(HConfig.INTERVAL_TIME);
						// this.wait();
						monitor.removeWatingTenants();
						// System.out.println("Tenant " + id + " continue");
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				// if (isActive == false) {
				// closeMySQLConnection();
				// closeVoltDBConnection();
				// }
				// Thread.yield();
				// closeMySQLConnection();
			}
		}
		closeMySQLConnection();
		closeVoltDBConnection();
	}

	private void openMySQLConnection() {
		try {
			if (mySQLConn != null && !mySQLConn.isClosed()) {
				return;
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage() + "&" + e.getErrorCode() + "&"
					+ e.getSQLState());
		}
		try {
			while (mySQLConn == null || mySQLConn.isClosed()) {
				mySQLConn = mySQLpool.getConnection();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			System.out.println(e.getMessage() + "&" + e.getErrorCode() + "&"
					+ e.getSQLState());
			System.exit(1);
		}
		for (int i = 0; i < HConfig.NUMBER_OF_TABLES; i++)
			tables[i].update(mySQLConn);
		// System.out.println("Tenant " + id + " opened a MySQL connection "
		// + interval + " " + split);
	}

	private void closeMySQLConnection() {
		try {
			if (mySQLConn == null || mySQLConn.isClosed())
				return;
		} catch (SQLException e) {
			System.out.println(e.getMessage() + "&" + e.getErrorCode() + "&"
					+ e.getSQLState());
		}
		mySQLpool.putConnection(mySQLConn);
		mySQLConn = null;
	}

	private void openVoltDBConnection() {
		// voltDBConn = ClientFactory.createClient();
		// try {
		// voltDBConn.createConnection(HConfig.VOLTDB_SERVER);
		// for (int i = 0; i < HConfig.NUMBER_OF_TABLES; i++)
		// tables[i].update(voltDBConn);
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// System.exit(1);
		// }
		if (voltDBConn != null && voltDBConn.getConnectedHostList().size() != 0)
			return;
		voltDBConn = voltDBPool.getConnection();
		for (int i = 0; i < HConfig.NUMBER_OF_TABLES; i++)
			tables[i].update(voltDBConn);
	}

	private void closeVoltDBConnection() {
		if (voltDBConn == null)
			return;
		voltDBPool.putConnection(voltDBConn);
		voltDBConn = null;
		// if (voltDBConn != null)
		// try {
		// voltDBConn.close();
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// System.exit(1);
		// }
	}

	private void doSQL(boolean isWrite) {
		if (isWrite)
			doWriteSQL();
		else
			doReadSQL();
		// doReadSQL();
	}

	private void doWriteSQL() {
		if (!isUsingVoltDB())
			doWriteSQLInMySQL();
		else
			doWriteSQLInVoltDB();
	}

	private void doWriteSQLInMySQL() {
		int updateRes = 0;
		int tableID = random.nextInt(HConfig.NUMBER_OF_TABLES);
		int queryType = HConfig.QTYPE_UPDATE;
		long t1, t2;
		t1 = System.nanoTime() - startTime;
		openMySQLConnection();
		try {
			mySQLConn.setAutoCommit(false);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			System.out.println("********************SQLException:"
					+ e.getMessage() + "&" + e.getErrorCode() + "&"
					+ e.getSQLState()
					+ ".Try to setAutoCommit again**************");
			openMySQLConnection();
			try {
				mySQLConn.setAutoCommit(false);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				System.out.println("@@@@@@@@@@@@@@@SQLException:"
						+ e1.getMessage() + "&" + e1.getErrorCode() + "&"
						+ e1.getSQLState()
						+ "setAutoCommit failed,WHAT THE FUCK!@@@@@@@@@@@@@");
				System.exit(1);
			}
		}
		if (queryType == HConfig.QTYPE_UPDATE) {
			// System.out
			// .format("Tenant %d: proposed a update query in table %d the %dth query%n",
			// id, tableID, sentQuerys + 1);
			// remainQuerys--;
			decRemainQuerys(1);
			sentQuerys++;
			updateRes = tables[tableID].doUpdateInMySQL();
			t2 = System.nanoTime() - startTime;
			if (updateRes != -1) {
				doneQuerys++;
				queryInfoList.add(new QueryInfo(doneQuerys, "MySQL", "Write",
						t1, t2));
				// if (queryType == HConfig.QTYPE_UPDATE)
				// System.out.format("update %d rows%n", updateRes);
			} else {
				System.out.println("Tenant " + id
						+ " update query in MySQL failed!");
			}
		}

		try {
			mySQLConn.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("@@@@@@@@@@@@@@@@@@@SQLException:"
					+ e.getMessage() + "&" + e.getErrorCode() + "&"
					+ e.getSQLState() + ".Commit failed.WTF! @@@@@@@@@@@@@@@@");
			System.exit(1);
		}
	}

	private void doWriteSQLInVoltDB() {
		long t1, t2;
		int updateRes = 0;
		int tableID = random.nextInt(HConfig.NUMBER_OF_TABLES);
		// remainQuerys--;
		decRemainQuerys(1);
		sentQuerys++;
		t1 = System.nanoTime() - startTime;
		openVoltDBConnection();
		updateRes = tables[tableID].doUpdateInVoltDB();
		t2 = System.nanoTime() - startTime;
		if (updateRes != -1) {
			doneQuerys++;
			queryInfoList.add(new QueryInfo(doneQuerys, "VoltDB", "Write", t1,
					t2));
			// System.out.format(
			// "Tenant %d: complete a update query in VoltDB(%d)%n", id,
			// useVoltDBID);
		} else {
			System.out.println("Tenant " + id
					+ " update query in VoltDB failed!");
		}
		closeVoltDBConnection();
	}

	private void doReadSQL() {
		if (!isUsingVoltDB())
			doReadSQLInMySQL();
		else
			doReadSQLInVoltDB();
	}

	private void doReadSQLInMySQL() {
		ResultSet rs = null;
		int tableID = random.nextInt(HConfig.NUMBER_OF_TABLES);
		int queryType = HConfig.QTYPE_SELECT;
		long t1;
		long t2;
		t1 = System.nanoTime() - startTime;
		openMySQLConnection();
		try {
			mySQLConn.setAutoCommit(false);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			System.out.println("********************SQLException:"
					+ e.getMessage() + "&" + e.getErrorCode() + "&"
					+ e.getSQLState()
					+ ".Try to setAutoCommit again!**************");
			openMySQLConnection();
			try {
				mySQLConn.setAutoCommit(false);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				System.out
						.println("@@@@@@@@@@@@@@@@@@@@SQLException:"
								+ e.getMessage() + "&" + e.getErrorCode() + "&"
								+ e.getSQLState()
								+ ".WHAT THE FUCK!@@@@@@@@@@@@@@@@@@");
			}
		}
		if (queryType == HConfig.QTYPE_SELECT) {
			// System.out
			// .format("Tenant %d: proposed a select query in table %d the %dth query%n",
			// id, tableID, sentQuerys + 1);
			// remainQuerys--;
			decRemainQuerys(1);
			sentQuerys++;
			rs = tables[tableID].doSelectInMySQL();
			t2 = System.nanoTime() - startTime;
			if (rs != null) {
				doneQuerys++;
				queryInfoList.add(new QueryInfo(doneQuerys, "MySQL", "Read",
						t1, t2));
			} else {
				System.out.println("Tenant " + id
						+ " select query in MySQL failed!");
			}
		}
		try {
			mySQLConn.commit();
		} catch (SQLException e) {
			System.out.println("@@@@@@@@@@@@@@@@@@@SQLException:"
					+ e.getMessage() + "&" + e.getErrorCode() + "&"
					+ e.getSQLState() + ".Commit failed.WTF! @@@@@@@@@@@@@@@@");
			System.exit(1);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				rs = null;
			}
		}
		// System.out.println("Tenant " + id + ":done the " + (doneQuerys + 1)
		// + "th query in MySQL for " + (((double) t2 - t1) / 1000) + "s");
	}

	private void doReadSQLInVoltDB() {
		VoltTable[] rs = null;
		int tableID = random.nextInt(HConfig.NUMBER_OF_TABLES);
		long t1, t2;
		decRemainQuerys(1);
		sentQuerys++;
		t1 = System.nanoTime() - startTime;
		openVoltDBConnection();
		rs = tables[tableID].doSelectInVoltDB();
		t2 = System.nanoTime() - startTime;
		if (rs != null) {
			doneQuerys++;
			queryInfoList.add(new QueryInfo(doneQuerys, "VoltDB", "Read", t1,
					t2));
			// System.out.format(
			// "Tenant %d: complete a select query in VoltDB(%d)%n",
			// id, useVoltDBID);
		} else {
			System.out.println("Tenant " + id
					+ " select query in VoltDB failed!");
		}
		closeVoltDBConnection();
	}

	// private void updateSplitInfo(long time) {
	// if (time >= split * HConfig.SPLIT_TIME
	// && time < (split + 1) * HConfig.SPLIT_TIME) {
	// SplitInfo tmp = new SplitInfo(split, lastRemainQuerys, workload,
	// remainQuerys);
	// splitInfoList.add(tmp);
	// split++;
	// } else if (time >= (split + 1) * HConfig.SPLIT_TIME) {
	// System.out
	// .println("***********Error:cross more than one split!**********");
	// System.exit(1);
	// } else {
	// System.out
	// .println("*******************Error:strange split!**************");
	// System.exit(1);
	// }
	// }
	//
	//
	// private void updateIntervalInfo(long time) {
	// if (time >= interval * HConfig.INTERVAL_TIME
	// && time < (interval + 1) * HConfig.INTERVAL_TIME) {
	// interval++;
	// } else if (time >= (interval + 1) * HConfig.INTERVAL_TIME) {
	// System.out
	// .println("***********Error:cross more than one interval!**********");
	// System.exit(1);
	// } else {
	// System.out
	// .println("*******************Error:strange intervel!**************");
	// System.exit(1);
	// }
	// }

	private synchronized int getRemainQuerys() {
		return remainQuerys;
	}

	private synchronized void addRemainQuerys(int workload) {
		remainQuerys += workload;
	}

	private synchronized void decRemainQuerys(int x) {
		remainQuerys -= x;
	}

	private synchronized void nextInterval() {
		interval++;
	}

	private synchronized int getInterval() {
		return interval;
	}

	public boolean isActiveInInterval(int interval) {
		return activeInfo[interval - 1];
	}

	public void updateSplitInfo() {
		SplitInfo tmp = new SplitInfo(split, lastRemainQuerys, workload,
				remainQuerys);
		splitInfoList.add(tmp);
		split++;
		if (split > HConfig.NUMBER_OF_SPLITS_IN_INTERVAL
				* HConfig.NUMBER_OF_INTERVAL)
			return;
		updateQuerys();
	}

	public void updateIntervalInfo() {
		nextInterval();
		if (interval > HConfig.NUMBER_OF_INTERVAL)
			return;
		updateActive();
		updateBurst();
		updateUsingDB();
		if (monitor != null) {
			monitor.sendInfoMessageForTenant(id, new HTenantDynamicInfo(
					interval, usingVoltDB, useVoltDBID, isActive, isBurst));
		}
	}

	private void updateActive() {
		/*
		 * if (interval == 1) { if (random.nextInt(100) <= HConfig.ACTIVE_RATIO)
		 * isActive = true; else isActive = false; } else { if (isActive) { if
		 * (random.nextInt(100) <= HConfig.ACTIVE_REMOVE_RATIO) { isActive =
		 * !isActive; } } else { if (random.nextInt(100) <=
		 * HConfig.ACTIVE_ADD_RATIO) { isActive = !isActive; } } }
		 */
		isActive = activeInfo[interval - 1];
	}

	private void updateBurst() {
		isBurst = HConfig.ISBURST[interval - 1];
	}

	private void updateQuerys() {
		// if (remainQuerys > 0)
		// System.out.println("Tenant " + id + ":remain " + remainQuerys
		// + "queries to do at interval " + interval + " and split "
		// + (split - 1));
		lastRemainQuerys = getRemainQuerys();
		if (!isActive)
			workload = 0;
		else {
			workload = getWorkLoad();
			if (workload > SLO)
				workload = SLO;
			// workload = 0;
		}
		addRemainQuerys(workload);
		// /remainQuerys += workload;
		// if (workload > 0)
		// System.out.println("Tenant " + id + ":" + workload);
	}

	public synchronized void updateUsingDB() {
		if (isToUseMySQLDB())
			setUsingVoltDB(false);
		if (isToUseVoltDB()) {
			setUsingVoltDB(true);
			setUsingVoltDBID(getIsToUseVoltDBID());
		}
		setIsToUseMySQLDB(false);
		setIsToUseVoltDB(false);
	}

	private int getWorkLoad() {
		// if (!isActive)
		// return 0;
		// if (isBurst) {
		// return PossionDistribution.getRandomNumber(SLO
		// * HConfig.SPLIT_TIME_IN_MINUTES);
		// } else {
		// return PossionDistribution.getRandomNumber(SLO / 3
		// * HConfig.SPLIT_TIME_IN_MINUTES);
		// }
		return workloadInfo[split - 1];
	}

	private boolean isUsingVoltDB() {
		return usingVoltDB;
	}

	private void setUsingVoltDB(boolean boo) {
		usingVoltDB = boo;
	}

	private void setUsingVoltDBID(int voltDB_id) {
		useVoltDBID = voltDB_id;
		for (int i = 0; i < HConfig.NUMBER_OF_TABLES; i++)
			tables[i].update(useVoltDBID);
	}

	private synchronized boolean isToUseMySQLDB() {
		return isToUseMySQLDB;
	}

	public synchronized void setIsToUseMySQLDB(boolean boo) {
		isToUseMySQLDB = boo;
	}

	private synchronized boolean isToUseVoltDB() {
		return isToUseVoltDB;
	}

	public synchronized void setIsToUseVoltDB(boolean boo) {
		isToUseVoltDB = boo;
	}

	private synchronized int getIsToUseVoltDBID() {
		return isToUseVoltDBID;
	}

	public synchronized void setIsToUseVoltDBID(int voltDB_id) {
		isToUseVoltDBID = voltDB_id;
	}

	public void print() {
		PrintWriter writer = null;
		// File file = new File("/home/zhujiaye/results", "result" + id);
		// if (!file.exists())
		// try {
		// file.createNewFile();
		// } catch (IOException e1) {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// }
		File dir = new File("results");
		dir.mkdir();
		try {
			writer = new PrintWriter(new File("results", "result" + id));
			// Tenant information
			writer.format(
					"***********************************************************Tenant %d information start***********************************************************%n",
					id);
			writer.format("%20s%20s%20s%20s%n", "id", "SLO", "dataSize",
					"writeHeavy");
			writer.format("%20d%20d%20d%20d%n", id, SLO, dataSize, writeHeavy);
			writer.format(
					"***********************************************************Tenant %d information   end***********************************************************%n",
					id);
			// Split information
			writer.format("***********************************************************Split information start***********************************************************%n");
			writer.format("%20s%20s%20s%20s%20s%n", "id",
					"remainQueriesBefore", "workload", "remainQueriesAfter",
					"sentQueries");
			for (int i = 0; i < splitInfoList.size(); i++) {
				SplitInfo tmp = splitInfoList.get(i);
				writer.format("%20d%20d%20d%20d%20d%n", tmp.getID(),
						tmp.getRemainQueriesBefore(), tmp.getWorkload(),
						tmp.getRemainQueriesAfter(), tmp.getSentQueries());
			}
			writer.format("***********************************************************Split information   end***********************************************************%n");
			// Query information
			writer.format("***********************************************************Query information start***********************************************************%n");
			writer.format("%20s%20s%20s%20s%20s%20s%n", "id", "whatDB",
					"whatAccess", "startTime", "endTime", "latency");
			for (int i = 0; i < queryInfoList.size(); i++) {
				QueryInfo tmp = queryInfoList.get(i);
				long t1, t2, t3;
				t1 = ((long) tmp.getStartTime());
				t2 = ((long) tmp.getEndTime());
				t3 = ((long) tmp.getLatency());
				writer.format("%20d%20s%20s%20d%20d%20d%n", tmp.getID(),
						tmp.getWhatDB(), tmp.getWhatAccess(), t1 / 1000000,
						t2 / 1000000, t3 / 1000000);
			}
			writer.format("***********************************************************Query information   end***********************************************************%n");
			// Total query information
			writer.format(
					"Tenant %d stopped! %d queries done,%d queries sent,%d queries remained%n",
					id, doneQuerys, sentQuerys, remainQuerys);
			writer.flush();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (writer != null)
				writer.close();
		}
		// System.out.format("Tenant %d: SLO(%d),dataSize(%d),writeHeavy(%d)%n",
		// id, SLO, dataSize, writeHeavy);
		// System.out.format(
		// "Tenant %d: SLO(%d),normal workload(%d),burst workload(%d)%n",
		// id,
		// SLO,
		// PossionDistribution.getRandomNumber(SLO / 2
		// * HConfig.INTERVAL_TIME_IN_MINUTES),
		// PossionDistribution.getRandomNumber(SLO
		// * HConfig.INTERVAL_TIME_IN_MINUTES));
	}

	/*
	 * public static void main(String[] args) { PrintWriter writer = null; File
	 * dir = new File("results"); dir.mkdir(); try { writer = new
	 * PrintWriter(new File("results", "result_test")); // Tenant information
	 * writer.format(
	 * "***********************************************************Tenant %d information start***********************************************************%n"
	 * , 0); writer.format("%20s%20s%20s%20s%n", "id", "SLO", "dataSize",
	 * "writeHeavy"); writer.format("%20d%20d%20d%20d%n", 0, 30, 30, 20);
	 * writer.format(
	 * "***********************************************************Tenant %d information   end***********************************************************%n"
	 * , 0); // Split information writer.format(
	 * "***********************************************************Split information start***********************************************************%n"
	 * ); writer.format("%20s%20s%20s%20s%20s%n", "id", "remainQueriesBefore",
	 * "workload", "remainQueriesAfter", "sentQueries"); for (int i = 0; i < 5;
	 * i++) { writer.format("%20d%20d%20d%20d%20d%n", i, i + 1, i + 2, i + 3, i
	 * + 4); } writer.format(
	 * "***********************************************************Split information   end***********************************************************%n"
	 * ); // Query information writer.format(
	 * "***********************************************************Query information start***********************************************************%n"
	 * ); writer.format("%20s%20s%20s%20s%20s%20s%n", "id", "whatDB",
	 * "whatAccess", "startTime", "endTime", "latency"); for (int i = 0; i < 3;
	 * i++) { ; double t1, t2, t3; t1 = ((double) i * 1000) / (60 * 1000); t2 =
	 * ((double) (i + 1) * 1000) / (60 * 1000); t3 = ((double) 1000) / (60 *
	 * 1000); writer.format("%20d%20s%20s%20.3f%20.3f%20.3f%n", i, "MySQL",
	 * "Read", t1, t2, t3); } writer.format(
	 * "***********************************************************Query information   end***********************************************************%n"
	 * ); writer.flush(); } catch (FileNotFoundException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); } finally { if (writer !=
	 * null) writer.close(); } }
	 */
}

class SplitInfo {
	final private int id;
	final private int remainQueriesBefore;
	final private int workload;
	final private int remainQueriesAfter;
	final private int sentQueries;

	public SplitInfo(int id, int remainQueriesBefore, int workload,
			int remainQueriesAfter) {
		this.id = id;
		this.remainQueriesBefore = remainQueriesBefore;
		this.workload = workload;
		this.remainQueriesAfter = remainQueriesAfter;
		this.sentQueries = remainQueriesBefore + workload - remainQueriesAfter;
		if (sentQueries < 0) {
			System.out
					.println("***********Error:negtive sent queries!***********");
		}
	}

	public int getID() {
		return id;
	}

	public int getRemainQueriesBefore() {
		return remainQueriesBefore;
	}

	public int getWorkload() {
		return workload;
	}

	public int getRemainQueriesAfter() {
		return remainQueriesAfter;
	}

	public int getSentQueries() {
		return sentQueries;
	}
}

class QueryInfo {
	private int id;
	private String whatDB;
	private String whatAccess;
	private long startTime;
	private long endTime;
	private long latency;

	public QueryInfo(int id, String whatDB, String whatAccess, long startTime,
			long endTime) {
		this.id = id;
		this.whatDB = whatDB;
		this.whatAccess = whatAccess;
		this.startTime = startTime;
		this.endTime = endTime;
		this.latency = endTime - startTime;
		if (latency < 0) {
			System.out
					.println("************Error:negtive query latency!**************");
		}
	}

	public int getID() {
		return id;
	}

	public String getWhatDB() {
		return whatDB;
	}

	public String getWhatAccess() {
		return whatAccess;
	}

	public long getStartTime() {
		return startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public long getLatency() {
		return latency;
	}
}
