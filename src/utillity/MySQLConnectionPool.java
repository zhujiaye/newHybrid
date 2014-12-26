package utillity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.util.HashSet;
import java.util.Iterator;

import config.HConfig;

public class MySQLConnectionPool {
	private HashSet<Connection> pool;
	private int created;
	private boolean test = false;

	public MySQLConnectionPool() {
		pool = new HashSet<Connection>();
		created = 0;
		// System.out.println("MySQLConnectionPool init finish!.......");
	}

	public MySQLConnectionPool(int num) {
		pool = new HashSet<Connection>();
		created = 0;
		for (int i = 0; i < num; i++)
			add();
		// System.out.println("MySQLConnectionPool init finish!.......");
	}

	public MySQLConnectionPool(int num, boolean test) {
		this.test = test;
		pool = new HashSet<Connection>();
		created = 0;
		for (int i = 0; i < num; i++)
			add();
		// System.out.println("MySQLConnectionPool init finish!.......");
	}

	private void add() {
		Connection newConn = null;
		try {
			try {
				Class.forName("com.mysql.jdbc.Driver").newInstance();
			} catch (InstantiationException e) {
				System.out.println(e.getMessage());
			} catch (IllegalAccessException e) {
				System.out.println(e.getMessage());
			} catch (ClassNotFoundException e) {
				System.out.println(e.getMessage());
			}
			while (newConn == null) {
				newConn = DriverManager.getConnection(HConfig.urlOfMySQL,
						HConfig.userOfMySQL, HConfig.passwordOfMySQL);
			}
			created++;
			if (test) {
				System.out
						.println("-------MySQL Connection Pool add a Connection "
								+ created + "------");
			}
		} catch (SQLTimeoutException e) {
			System.out
					.println("**************SQLTimeoutException:Can't get a connection!!!!!!!!!************");
			System.exit(1);
			// System.out.println(e.getMessage());
		} catch (SQLException e1) {
			System.out
					.println("**************SQLException:Can't get a connection and try to get a new connection!***********");
			try {
				Class.forName("com.mysql.jdbc.Driver").newInstance();
			} catch (InstantiationException e) {
				System.out.println(e.getMessage());
			} catch (IllegalAccessException e) {
				System.out.println(e.getMessage());
			} catch (ClassNotFoundException e) {
				System.out.println(e.getMessage());
			}
			while (newConn == null) {
				try {
					newConn = DriverManager.getConnection(HConfig.urlOfMySQL,
							HConfig.userOfMySQL, HConfig.passwordOfMySQL);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					System.out
							.println("@@@@@@@@@@@@SQLException:Can't get a connection again!***********");
					System.exit(1);
				}
			}
			created++;
			System.out.println("-----MySQL Connection Pool add a Connection "
					+ created + "------");
			// System.out.println(e.getMessage());
		} finally {
			if (newConn != null) {
				pool.add(newConn);
			}
		}
	}

	public void clear() {
		Connection tmp;
		Iterator<Connection> iter = null;
		iter = pool.iterator();
		if (iter != null) {
			for (; iter.hasNext();) {
				tmp = iter.next();
				if (tmp != null) {
					try {
						tmp.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		pool.clear();
	}

	public synchronized Connection getConnection() {
		Connection res = null;
		Iterator<Connection> iter = null;
		if (pool.size() == 0)
			add();
		iter = pool.iterator();
		if (iter != null && iter.hasNext()) {
			res = iter.next();
			pool.remove(res);
		}
		return res;
	}

	public synchronized void putConnection(Connection conn) {
		pool.add(conn);
	}
}
