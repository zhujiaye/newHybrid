package server;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import utillity.PossionDistribution;
import config.HConfig;

public class Tenant {
	private int id;
	private int SLO;
	private int dataSize;
	private int writeHeavy;

	private int interval;
	private boolean usingVoltDB;
	private int useVoltDBID;
	private boolean isActive;
	private boolean isBurst;

	private Sender sender;

	private int[] workloadInfo;
	private boolean[] activeInfo;

	public Tenant(int id, Sender sender) {
		this.id = id;
		this.sender = sender;
		getInfoFromFile();
		// setUsingVoltDB(false);
		// setIsActive(false);
	}

	public Tenant(int id) {
		this.id = id;
		workloadInfo = new int[HConfig.NUMBER_OF_INTERVAL
				* HConfig.NUMBER_OF_SPLITS_IN_INTERVAL];
		activeInfo = new boolean[HConfig.NUMBER_OF_INTERVAL];
		for (int i = 0; i < HConfig.NUMBER_OF_INTERVAL; i++)
			activeInfo[i] = false;
	}

	public Sender getSender() {
		return sender;
	}

	public void setActive(int interval, boolean boo) {
		activeInfo[interval - 1] = boo;
	}

	public boolean getActive(int interval) {
		return activeInfo[interval - 1];
	}

	public void setWorkload(int split, int x) {
		workloadInfo[split - 1] = x;
	}

	public int getWorkload(int split) {
		return workloadInfo[split - 1];
	}

	public int getID() {
		return id;
	}

	public int getSLO() {
		return SLO;
	}

	public void setSLO(int SLO) {
		this.SLO = SLO;
	}

	public int getEstimatedDataSizeInVoltDB() {
		return dataSize;
	}

	public int getDataSize() {
		return dataSize;
	}

	public void setDataSize(int dataSize) {
		this.dataSize = dataSize;
	}

	public int getWriteHeavy() {
		return writeHeavy;
	}

	public void setWriteHeavy(int writeHeavy) {
		this.writeHeavy = writeHeavy;
	}

	public synchronized void setInterval(int interval) {
		this.interval = interval;
	}

	public synchronized int getInterval() {
		return interval;
	}

	public synchronized void setUsingVoltDB(boolean boo) {
		usingVoltDB = boo;
	}

	public synchronized boolean isUsingVoltDB() {
		return usingVoltDB;
	}

	public synchronized void setUseVoltDBID(int voltDB_id) {
		useVoltDBID = voltDB_id;
	}

	public synchronized int getUseVoltDBID() {
		return useVoltDBID;
	}

	public synchronized void setIsActive(boolean boo) {
		isActive = boo;
	}

	public synchronized boolean isActive() {
		return isActive;
	}

	public synchronized void setIsBurst(boolean boo) {
		isBurst = boo;
	}

	public synchronized boolean isBurst() {
		return isBurst;
	}

	public boolean isActiveInBurst() {
		for (int i = 1; i <= HConfig.NUMBER_OF_INTERVAL; i++) {
			if (!HConfig.ISBURST[i - 1])
				continue;
			if (activeInfo[i - 1])
				return true;
		}
		return false;
	}

	public int getEstimatedWorkLoadInBurst() {
		int estimated = 0;
		estimated += PossionDistribution.getRandomNumber(SLO)
				* HConfig.NUMBER_OF_BURST_INTERVAL
				* HConfig.NUMBER_OF_SPLITS_IN_INTERVAL;
		// estimated += SLO * HConfig.NUMBER_OF_BURST_INTERVAL
		// * HConfig.NUMBER_OF_SPLITS_IN_INTERVAL;
		return estimated;
	}

	public int getActualWorkloadInBurst() {
		int actual = 0;
		for (int i = 1; i <= HConfig.NUMBER_OF_INTERVAL
				* HConfig.NUMBER_OF_SPLITS_IN_INTERVAL; i++) {
			int interval = (i - 1) / 5;
			if (!HConfig.ISBURST[interval])
				continue;
			actual += workloadInfo[i - 1];
		}
		return actual;
	}

	private void getInfoFromFile() {
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
						activeInfo[interval] = true;
						break;
					}
				}
				if (c == HConfig.NUMBER_OF_INTERVAL - 1)
					break;
			}
			activeInfo[0] = activeInfo[1];
			// workload information
			c = 5;
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
			c = 5;
			for (; c-- > 0;) {
				workloadInfo[c] = workloadInfo[c
						+ HConfig.NUMBER_OF_SPLITS_IN_INTERVAL];
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println(HConfig.WL_FILE
					+ " does not exist!Please call Procedure.main first!");
		} finally {
			if (reader != null)
				reader.close();
		}
	}
}
