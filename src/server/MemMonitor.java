package server;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.voltdb.VoltTable;
import org.voltdb.VoltTableRow;
import org.voltdb.VoltType;
import org.voltdb.client.Client;
import org.voltdb.client.ProcCallException;

import config.Constants;
import utillity.VoltdbConnectionPool;

public class MemMonitor extends Thread {
	final static private Logger LOG = Logger.getLogger(Constants.LOGGER_NAME);
	private Client mVoltdbConn = null;
	private VoltTable[] mResults = null;
	private VoltTable[] mResults2 = null;
	private volatile boolean mIsFinished = false;
	private int mTotalMemory = 0;
	private int mDataMemory = 0;
	private int mJavaMemory = 0;

	public void run() {
		VoltdbConnectionPool pool;
		pool = VoltdbConnectionPool.getPool();
		mVoltdbConn = pool.getConnection();
		if (mVoltdbConn == null) {
			LOG.error("MemMonitor error: can't get a voltdb connection!");
			return;
		}
		FileWriter fstream = null;
		String outputFilePath = Constants.LOGGER_DIR + "/HMemory.log";
		try {
			fstream = new FileWriter(outputFilePath, true);
		} catch (IOException e) {
			LOG.error("IOException while opening file: " + e.getMessage());
			return;
		}
		BufferedWriter out = new BufferedWriter(fstream);
		int time = 0;
		try {
			out.write("***********MemMonitor START!!*************");
			out.newLine();
			while (!mIsFinished) {

				mResults = mVoltdbConn.callProcedure("@Statistics", "TABLE", 0)
						.getResults();
				mResults2 = mVoltdbConn.callProcedure("@Statistics", "MEMORY",
						0).getResults();
				mTotalMemory = 0;
				mDataMemory = 0;
				mJavaMemory = 0;
				int tupleMemory = 0;
				for (VoltTable table : mResults) {
					for (int i = 0; i < table.getRowCount(); i++) {
						VoltTableRow row = table.fetchRow(i);
						mDataMemory += (int) row.get("TUPLE_DATA_MEMORY",
								VoltType.INTEGER);
					}
				}
				for (VoltTable table : mResults2) {
					for (int i = 0; i < table.getRowCount(); i++) {
						VoltTableRow row = table.fetchRow(i);
						mTotalMemory += (int) row.get("RSS", VoltType.INTEGER);
						mJavaMemory += (int) row.get("JAVAUSED",
								VoltType.INTEGER);
						tupleMemory += (int) row.get("TUPLEDATA",
								VoltType.INTEGER);
					}
				}
				out.write("" + time + " " + mDataMemory + " " + mTotalMemory
						/ 1024.0 + " " + mJavaMemory / 1024.0 + " "
						+ tupleMemory);
				out.newLine();
				out.flush();
				time++;
				Thread.sleep(1000);

			}
			out.write("***********MemMonitor STOP!!*************");
		} catch (IOException | ProcCallException | InterruptedException e) {
			LOG.error(e.getMessage());
		}
		try {
			out.close();
			mVoltdbConn.close();
		} catch (InterruptedException | IOException e) {
			LOG.error(e.getMessage());
		}
	}

	public void shutdown() {
		mIsFinished = true;
	}
}
