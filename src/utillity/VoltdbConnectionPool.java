package utillity;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;

import newhybrid.HException;
import newhybrid.NoVoltdbConnectionException;

import org.voltdb.client.Client;
import org.voltdb.client.ClientConfig;
import org.voltdb.client.ClientFactory;
import org.voltdb.client.ClientResponse;
import org.voltdb.client.ClientStatusListenerExt;
import org.voltdb.client.NoConnectionsException;
import org.voltdb.client.ProcedureCallback;

import config.HConfig;

public class VoltdbConnectionPool {
	private final static int MAX_RETRY = 5;
	private HashSet<Client> mPool;
	private HConfig mConf;

	public VoltdbConnectionPool() throws HException {
		this(0);
	}

	public VoltdbConnectionPool(int num) throws HException {
		mPool = new HashSet<Client>();
		mConf = HConfig.getConf();
		for (int i = 0; i < num; i++) {
			try {
				add();
			} catch (NoVoltdbConnectionException e) {
			}
		}
	}

	private void add() throws NoVoltdbConnectionException, HException {
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
			throw new HException(e.getMessage());
		}

	}

	public void clear() {
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
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		mPool.clear();
	}

	public synchronized Client getConnection() throws HException {
		Client res = null;
		Iterator<Client> iter = null;
		if (mPool.size() == 0) {
			try {
				add();
			} catch (NoVoltdbConnectionException e) {
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

	public synchronized void putConnection(Client conn) {
		mPool.add(conn);
	}
}
