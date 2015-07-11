package utillity;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Iterator;

import newhybrid.NoVoltdbConnectionException;

import org.apache.log4j.Logger;
import org.voltdb.client.Client;
import org.voltdb.client.ClientConfig;
import org.voltdb.client.ClientFactory;
import org.voltdb.client.ClientResponse;
import org.voltdb.client.ClientStatusListenerExt;
import org.voltdb.client.NoConnectionsException;
import org.voltdb.client.ProcedureCallback;

import config.Constants;
import config.HConfig;

public class VoltdbConnectionPool {
	final static private Logger LOG = Logger.getLogger(Constants.LOGGER_NAME);
	final static private int MAX_RETRY = 5;
	static private VoltdbConnectionPool pool = null;

	private HashSet<Client> mPool;
	private HConfig mConf;

	public static synchronized VoltdbConnectionPool getPool() {
		if (pool == null) {
			pool = new VoltdbConnectionPool();
		}
		return pool;
	}

	private VoltdbConnectionPool() {
		this(0);
	}

	private VoltdbConnectionPool(int num) {
		mPool = new HashSet<Client>();
		mConf = HConfig.getConf();
		for (int i = 0; i < num; i++) {
			try {
				add();
			} catch (NoVoltdbConnectionException e) {
				LOG.error("Can't get a new voltdb connection:" + e.getMessage());
			}
		}
	}

	/**
	 * add a new voltdb connection to the pool
	 * 
	 * @throws NoVoltdbConnectionException
	 *             if a new voltdb connection can't be got
	 */
	private void add() throws NoVoltdbConnectionException {
		Client newConnection = null;
		ClientConfig config = new ClientConfig();
		config.setConnectionResponseTimeout(0);
		config.setProcedureCallTimeout(0);
		newConnection = ClientFactory.createClient(config);
		try {
			int cnt = 0;
			while (cnt++ < MAX_RETRY
					&& newConnection.getConnectedHostList().isEmpty()) {
				newConnection.createConnection(mConf.getVoltdbServerAddress());
			}
			if (newConnection == null
					|| newConnection.getConnectedHostList().isEmpty()) {
				throw new NoVoltdbConnectionException("Can't connect to voldb!");
			}
			mPool.add(newConnection);
		} catch (IOException e) {
			LOG.warn("java network or connection problem:" + e.getMessage());
		}

	}

	public synchronized void clear() {
		Client tmp;
		Iterator<Client> iter = null;
		iter = mPool.iterator();
		if (iter != null) {
			for (; iter.hasNext();) {
				tmp = iter.next();
				if (tmp != null) {
					try {
						tmp.drain();
						tmp.close();
					} catch (InterruptedException | NoConnectionsException e) {
						LOG.error(e.getMessage());
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
	 * @return null if there is no useful voltdb connection
	 */
	public synchronized Client getConnection() {
		Client res = null;
		Iterator<Client> iter = null;
		if (mPool.size() == 0) {
			try {
				add();
			} catch (NoVoltdbConnectionException e) {
				LOG.error("Can't get a new voltdb connection:" + e.getMessage());
			}
		}
		iter = mPool.iterator();
		while (iter != null && iter.hasNext()) {
			res = iter.next();
			mPool.remove(res);
			if (res == null || res.getConnectedHostList().isEmpty())
				continue;
			break;
		}
		return res;
	}

	public synchronized void putConnection(Client conn) {
		if (conn == null || conn.getConnectedHostList().isEmpty())
			return;
		mPool.add(conn);
	}
}
