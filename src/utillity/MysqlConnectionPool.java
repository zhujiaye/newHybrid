package utillity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.util.HashSet;
import java.util.Iterator;

import org.apache.log4j.Logger;

import newhybrid.NoMysqlConnectionException;
import config.Constants;
import config.HConfig;

public class MysqlConnectionPool {
	final static private Logger LOG = Logger.getLogger(Constants.LOGGER_NAME);
	final static private int MAX_RETRY = 5;
	static private MysqlConnectionPool pool = null;

	private HashSet<Connection> mPool;
	private HConfig mConf;

	public synchronized static MysqlConnectionPool getPool() {
		if (pool == null) {
			pool = new MysqlConnectionPool();
		}
		return pool;
	}

	private MysqlConnectionPool() {
		this(0);
	}

	private MysqlConnectionPool(int num) {
		mPool = new HashSet<Connection>();
		mConf = HConfig.getConf();
		for (int i = 0; i < num; i++) {
			try {
				add();
			} catch (NoMysqlConnectionException e) {
				LOG.error("Can't get a new mysql connection:" + e.getMessage());
			}
		}
	}

	/**
	 * add a new MySQL connection to the pool
	 * 
	 * @throws NoMysqlConnectionException
	 *             if a new mysql connection can't be got
	 */
	private void add() throws NoMysqlConnectionException {
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
			throw new NoMysqlConnectionException("No jdbc driver!");
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
						LOG.error("Access denied!");
					} finally {
						tmp = null;
					}
				}
			}
		}
		mPool.clear();
	}

	/**
	 * 
	 * @return null if there is no useful connection
	 */
	public synchronized Connection getConnection() {
		Connection res = null;
		Iterator<Connection> iter = null;
		if (mPool.size() == 0) {
			try {
				add();
			} catch (NoMysqlConnectionException e) {
				LOG.error("Can't get a new mysql connection" + e.getMessage());
			}
		}
		iter = mPool.iterator();
		while (iter != null && iter.hasNext()) {
			res = iter.next();
			mPool.remove(res);
			try {
				if (res == null || res.isClosed())
					continue;
			} catch (SQLException e) {
				LOG.error("Access denied!");
			}
			break;
		}
		return res;
	}

	public synchronized void putConnection(Connection conn) {
		try {
			if (conn == null || conn.isClosed())
				return;
		} catch (SQLException e) {
			LOG.error("Access denied!");
		}
		mPool.add(conn);
	}
}
