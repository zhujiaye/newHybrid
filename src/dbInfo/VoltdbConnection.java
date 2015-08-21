package dbInfo;

import org.apache.log4j.Logger;
import org.voltdb.client.Client;
import org.voltdb.client.NoConnectionsException;

import config.Constants;
import thrift.DbmsInfo;

public class VoltdbConnection extends HConnection {
	private final static Logger LOG = Logger.getLogger(Constants.LOGGER_NAME);
	private Client mVoltdbConnection;

	public VoltdbConnection(DbmsInfo dbmsInfo,Client voltdbConnection) {
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
}
