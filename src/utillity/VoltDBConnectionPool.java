package utillity;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;

import org.voltdb.client.Client;
import org.voltdb.client.ClientConfig;
import org.voltdb.client.ClientFactory;
import org.voltdb.client.ClientResponse;
import org.voltdb.client.ClientStatusListenerExt;
import org.voltdb.client.ProcedureCallback;

import config.HConfig;

public class VoltDBConnectionPool {
	private HashSet<Client> pool;
	private int created;
	private boolean test = false;

	public VoltDBConnectionPool() {
		pool = new HashSet<Client>();
		created = 0;
		// System.out.println("VoltDBConnectionPool init finish!.......");
	}

	public VoltDBConnectionPool(int num) {
		pool = new HashSet<Client>();
		created = 0;
		for (int i = 0; i < num; i++) {
			add();
		}
		// System.out.println("VoltDBConnectionPool init finish!.......");
	}

	public VoltDBConnectionPool(int num, boolean test) {
		this.test = test;
		pool = new HashSet<Client>();
		created = 0;
		for (int i = 0; i < num; i++) {
			add();
		}
		// System.out.println("VoltDBConnectionPool init finish!.......");
	}

	private void add() {
		// ClientStatusListenerExt mylistener = new ClientStatusListenerExt() {
		// public void connectionLost(String hostname, int port,
		// int connectionsLeft, DisconnectCause cause) {
		// System.out.printf("A connection to the database been lost. "
		// + "There are %d connections remaining.\n",
		// connectionsLeft);
		// }
		//
		// public void backpressure(boolean status) {
		// System.out.println("Backpressure from the database "
		// + "is causing a delay in processing requests.");
		// }
		//
		// public void uncaughtException(ProcedureCallback callback,
		// ClientResponse r, Throwable e) {
		// System.out
		// .println("An error has occured in a callback "
		// + "procedure. Check the following stack trace for details.");
		// e.printStackTrace();
		// }
		//
		// public void lateProcedureResponse(ClientResponse response,
		// String hostname, int port) {
		// System.out.printf("A procedure that timed out on host %s:%d"
		// + " has now responded.\n", hostname, port);
		// }
		// };

		// ClientConfig myconfig = new ClientConfig("username", "password",
		// mylistener);

		// System.out.println("Try to get a client and a connection");
		Client newConn = null;
		ClientConfig config = new ClientConfig();
		config.setConnectionResponseTimeout(0);
		config.setProcedureCallTimeout(0);
		newConn = ClientFactory.createClient(config);
		try {
			newConn.createConnection(HConfig.VOLTDB_SERVER);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		} finally {
			pool.add(newConn);
			created++;
			if (test) {
				System.out
						.println("-----VoltDB Connection Pool add a Connection "
								+ created + "------");
			}
		}
	}

	public void clear() {
		Client tmp;
		Iterator<Client> iter = null;
		iter = pool.iterator();
		if (iter != null) {
			for (; iter.hasNext();) {
				tmp = iter.next();
				if (tmp != null) {
					try {
						tmp.close();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		pool.clear();
	}

	public synchronized Client getConnection() {
		Client res = null;
		Iterator<Client> iter = null;
		if (pool.size() == 0)
			add();
		iter = pool.iterator();
		if (iter != null && iter.hasNext()) {
			res = iter.next();
			pool.remove(res);
		}
		return res;
		// return ClientFactory.createClient();
	}

	public synchronized void putConnection(Client conn) {
		// if (conn != null) {
		// try {
		// conn.close();
		//
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// // pool.add(conn);
		// }
		pool.add(conn);
	}
}
