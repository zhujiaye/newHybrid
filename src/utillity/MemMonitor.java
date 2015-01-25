package utillity;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import org.voltdb.VoltTable;
import org.voltdb.VoltTableRow;
import org.voltdb.VoltType;
import org.voltdb.client.Client;
import org.voltdb.client.ProcCallException;

public class MemMonitor extends Thread {
	public String voltdbServer;
	Client voltdbConn = null;
	VoltTable[] results = null;
	VoltTable[] results2 = null;
	VoltTable[] results3 = null;
	int totalMemory = 0;
	int dataMemory = 0;
	int javaMemory = 0;

	public MemMonitor(String vs) {
		this.voltdbServer = vs;
	}

	public void run() {
		VoltDBConnectionPool pool;
		pool = new VoltDBConnectionPool();
		// voltdbConn = DBManager.connectVoltdb(voltdbServer);
		voltdbConn = pool.getConnection();
		if (voltdbConn == null) {
			System.out
					.println("error connecting to voltdb while retriving data...");
		}
		FileWriter fstream = null;
		try {
			fstream = new FileWriter("HMemory.txt", true);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		BufferedWriter out = new BufferedWriter(fstream);
		int time = 0;
		System.out.println("***********MemMonitor START!!*************");
		while (true) {
			if (time<31*60) {
				try {
					results = voltdbConn.callProcedure("@Statistics", "TABLE",
							0).getResults();
					results2 = voltdbConn.callProcedure("@Statistics",
							"MEMORY", 0).getResults();
					totalMemory = 0;
					dataMemory = 0;
					javaMemory = 0;
					int tupleMemory = 0;
					for (VoltTable table : results) {
						for (int i = 0; i < table.getRowCount(); i++) {
							VoltTableRow row = table.fetchRow(i);
							dataMemory += (int) row.get("TUPLE_DATA_MEMORY",
									VoltType.INTEGER);
						}
					}
					for (VoltTable table : results2) {
						for (int i = 0; i < table.getRowCount(); i++) {
							VoltTableRow row = table.fetchRow(i);
							totalMemory += (int) row.get("RSS",
									VoltType.INTEGER);
							javaMemory += (int) row.get("JAVAUSED",
									VoltType.INTEGER);
							tupleMemory += (int) row.get("TUPLEDATA",
									VoltType.INTEGER);
						}
					}
					out.write("" + time + " " + dataMemory + " " + totalMemory
							/ 1024.0 + " " + javaMemory / 1024.0 + " "
							+ tupleMemory);
					out.newLine();
					out.flush();
					time++;
					Thread.sleep(1000);
				} catch (IOException | ProcCallException | InterruptedException e) {
					e.printStackTrace();
				}
			}
			else
				break;
		}
		try {
			out.close();
			voltdbConn.close();
			pool.clear();
		} catch (InterruptedException | IOException e) {
			e.printStackTrace();
		}
		System.out.println("***********MemMonitor STOP!!*************");
	}

}
