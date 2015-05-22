package utillity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.util.HashSet;
import java.util.Iterator;

import newhybrid.HException;
import newhybrid.NoMysqlConnectionException;
import config.HConfig;

public class MysqlConnectionPool {
	private final static int MAX_RETRY = 5;
	private HashSet<Connection> mPool;
	private HConfig mConf;

	public MysqlConnectionPool() throws HException {
		this(0);
	}

	public MysqlConnectionPool(int num) throws HException {
		mPool = new HashSet<Connection>();
		mConf = HConfig.getConf();
		for (int i = 0; i < num; i++) {
			try {
				add();
			} catch (NoMysqlConnectionException e) {
				e.printStackTrace();
			}
		}
	}

	private void add() throws NoMysqlConnectionException, HException {
		Connection newConnection = null;
		int cnt = 0;
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			while (cnt++ < MAX_RETRY && newConnection == null) {
				newConnection = DriverManager.getConnection(
						mConf.getMysqlCompleteAddress(),
						mConf.getMysqlUsername(), mConf.getMysqlPassword());
			}
			if (newConnection == null) {
				throw new NoMysqlConnectionException("No connection!");
			}
			mPool.add(newConnection);
		} catch (SQLTimeoutException e) {
			throw new NoMysqlConnectionException("Time out!");
		} catch (SQLException e1) {
			throw new NoMysqlConnectionException("Access denied!");
		} catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException e) {
			throw new HException("No jdbc driver!");
		}
	}

	public synchronized void clear() {
		Connection tmp;
		Iterator<Connection> iter = null;
		iter = mPool.iterator();
		if (iter != null) {
			for (; iter.hasNext();) {
				tmp = iter.next();
				if (tmp != null) {
					try {
						tmp.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}
		mPool.clear();
	}

	public synchronized Connection getConnection() throws HException {
		Connection res = null;
		Iterator<Connection> iter = null;
		if (mPool.size() == 0) {
			try {
				add();
			} catch (NoMysqlConnectionException e) {
				e.printStackTrace();
			}
		}
		iter = mPool.iterator();
		if (iter != null && iter.hasNext()) {
			res = iter.next();
			mPool.remove(res);
		}
		return res;
	}

	public synchronized void putConnection(Connection conn) {
		mPool.add(conn);
	}
}
