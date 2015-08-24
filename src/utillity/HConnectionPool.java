package utillity;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.voltdb.client.Client;
import org.voltdb.client.ClientConfig;
import org.voltdb.client.ClientFactory;

import config.Constants;
import dbInfo.HConnection;
import dbInfo.MysqlConnection;
import dbInfo.VoltdbConnection;
import newhybrid.NoHConnectionException;
import thrift.DbmsInfo;
import thrift.DbmsType;

public class HConnectionPool {
	private final static Logger LOG = Logger.getLogger(Constants.LOGGER_NAME);
	private final static int MAX_RETRY = 5;
	static private HConnectionPool pool = null;

	private ArrayList<HConnection> mList;

	public synchronized static HConnectionPool getPool() {
		if (pool == null) {
			pool = new HConnectionPool();
		}
		return pool;
	}

	private HConnectionPool() {
		mList = new ArrayList<>();
	}

	/**
	 * add a new HConnection to the pool according to the dbms information
	 * 
	 * @param dbmsInfo
	 *            the dbms information
	 * @return HConnection which is added to the pool
	 * @throws NoHConnectionException
	 *             if no HConnection is got
	 */
	private HConnection addByDbmsInfo(DbmsInfo dbmsInfo) throws NoHConnectionException {
		HConnection res = null;
		if (dbmsInfo.mType == DbmsType.MYSQL) {
			res = MysqlConnection.getConnection(dbmsInfo);
		} else {
			res = VoltdbConnection.getConnection(dbmsInfo);
		}
		mList.add(res);
		return res;
	}

	/**
	 * get a HConnection according to the dbms information can be null
	 * 
	 * @param dbmsInfo
	 *            the dbms information
	 * @return HConnection,can be null
	 */
	public synchronized HConnection getConnectionByDbmsInfo(DbmsInfo dbmsInfo) {
		HConnection res = null;
		ArrayList<HConnection> removeList = new ArrayList<>();
		for (HConnection tmp : mList) {
			if (tmp == null || !tmp.isUseful()) {
				removeList.add(tmp);
				continue;
			}
			if (tmp.match(dbmsInfo)) {
				res = tmp;
				removeList.add(tmp);
				break;
			}
		}
		for (HConnection tmp : removeList) {
			mList.remove(tmp);
		}
		if (res == null) {
			try {
				res = addByDbmsInfo(dbmsInfo);
				mList.remove(res);
				return res;
			} catch (NoHConnectionException e) {
				LOG.error("can't get a new HConnection for " + e.getDbmsInfo().getMType() + ":" + e.getMessage());
			}
		}
		return res;
	}

	public synchronized void clear() {
		for (HConnection tmp : mList) {
			if (tmp != null) {
				tmp.release();
			}
		}
		mList.clear();
	}

	public synchronized void putConnection(HConnection conn) {
		if (conn == null || !conn.isUseful())
			return;
		mList.add(conn);
	}

	public synchronized int size() {
		return mList.size();
	}

	/**
	 * just for test
	 * 
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		DbmsInfo info1, info2, info3, info4;
		info1 = new DbmsInfo(DbmsType.MYSQL, "jdbc:mysql://192.168.0.30/newhybrid", "remote", "remote", 0);
		info2 = new DbmsInfo(DbmsType.MYSQL, "jdbc:mysql://192.168.0.31/newhybrid", "remote", "remote", 0);
		info3 = new DbmsInfo(DbmsType.VOLTDB, "192.168.0.30", null, null, 2000);
		info4 = new DbmsInfo(DbmsType.VOLTDB, "192.168.0.31", null, null, 2000);
		HConnectionPool pool = HConnectionPool.getPool();
		HConnection connection;
		for (int i = 1; i <= 5; i++) {
			connection = pool.getConnectionByDbmsInfo(info1);
			pool.putConnection(connection);
			connection = pool.getConnectionByDbmsInfo(info2);
			pool.putConnection(connection);
			connection = pool.getConnectionByDbmsInfo(info3);
			pool.putConnection(connection);
			connection = pool.getConnectionByDbmsInfo(info4);
			pool.putConnection(connection);
			System.out.println(pool.size());
		}
		Object o = new Object();
		synchronized (o) {
			o.wait();
		}
	}
}
