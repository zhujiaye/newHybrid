package dbInfo;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.voltdb.client.Client;
import org.voltdb.client.ClientConfig;
import org.voltdb.client.ClientFactory;
import org.voltdb.client.NoConnectionsException;

import config.Constants;
import newhybrid.NoHConnectionException;
import thrift.DbmsInfo;

public class VoltdbConnection extends HConnection {
	static private final Logger LOG = Logger.getLogger(Constants.LOGGER_NAME);
	static private final int MAX_RETRY = 5;

	/**
	 * get a voltdb HConnection by dbms information
	 * 
	 * @param dbmsInfo
	 * @return voltdb HConnection,not null
	 * @throws NoHConnectionException
	 */
	static public HConnection getConnection(DbmsInfo dbmsInfo) throws NoHConnectionException {
		int cnt = 0;
		Client newConnection = null;
		ClientConfig config = new ClientConfig();
		config.setConnectionResponseTimeout(0);
		config.setProcedureCallTimeout(0);
		newConnection = ClientFactory.createClient(config);
		try {
			while (cnt++ < MAX_RETRY && newConnection.getConnectedHostList().isEmpty()) {
				newConnection.createConnection(dbmsInfo.mCompleteConnectionString);
				if (!newConnection.getConnectedHostList().isEmpty())
					break;
			}
			if (newConnection == null || newConnection.getConnectedHostList().isEmpty()) {
				throw new NoHConnectionException(dbmsInfo,
						"tried " + MAX_RETRY + " times but can not establish connection!");
			}
			HConnection res = new VoltdbConnection(dbmsInfo, newConnection);
			return res;
		} catch (IOException e) {
			throw new NoHConnectionException(dbmsInfo, "java network or connection problem");
		}
	}

	private Client mVoltdbConnection;

	public VoltdbConnection(DbmsInfo dbmsInfo, Client voltdbConnection) {
		super(dbmsInfo);
		mVoltdbConnection = voltdbConnection;
	}

	@Override
	public boolean isUseful() {
		if (mVoltdbConnection == null)
			return false;
		return !mVoltdbConnection.getConnectedHostList().isEmpty();
	}

	@Override
	public void release() {
		if (mVoltdbConnection == null)
			return;
		try {
			mVoltdbConnection.drain();
			mVoltdbConnection.close();
		} catch (NoConnectionsException e) {
		} catch (InterruptedException e) {
			LOG.error("voltdb connection is releasing while being interrupted!");
		} finally {
			mVoltdbConnection = null;
		}
	}

	@Override
	public void dropAll() {
		// TODO Auto-generated method stub
	}

	@Override
	public HResult doRandomSelect(Table table) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HResult doRandomUpdate(Table table) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void dropTable(Table table) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean createTable(Table table) {
		// TODO Auto-generated method stub
		return false;
	}

}
