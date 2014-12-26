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

	public Tenant(int id, Sender sender) {
		this.id = id;
		this.sender = sender;
		getInfoFromFile();
		// setUsingVoltDB(false);
		// setIsActive(false);
	}

	public Sender getSender() {
		return sender;
	}

	public int getID() {
		return id;
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

	public int getSLO() {
		return SLO;
	}

	public int getEstimatedWorkLoadInBurst() {
		int res = 0;
		res += PossionDistribution.getRandomNumber(SLO);
		return res;
	}

	public int getEstimatedDataSizeInVoltDB() {
		return dataSize;
	}

	public int getDataSize() {
		return dataSize;
	}

	public int getWriteHeavy() {
		return writeHeavy;
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
	}
}
