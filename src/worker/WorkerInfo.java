package worker;

import org.apache.log4j.Logger;

import config.Constants;
import dbInfo.HConnection;
import newhybrid.NoHConnectionException;
import thrift.DbmsInfo;
import utillity.HConnectionPool;

public class WorkerInfo {
	static private final Logger LOG = Logger.getLogger(Constants.LOGGER_NAME);
	private final String WORKER_ADDRESS;
	private final int WORKER_PORT;
	private final DbmsInfo DBMSINFO;

	public WorkerInfo(String worker_address, int worker_port, DbmsInfo dbmsInfo) {
		WORKER_ADDRESS = worker_address;
		WORKER_PORT = worker_port;
		DBMSINFO = dbmsInfo;
	}

	public void init() {
		// TODO
	}

	public void copyDBForTenant(int ID) {
		HConnectionPool pool = HConnectionPool.getPool();
		try {
			HConnection hConnection = pool.getConnectionByDbmsInfo(DBMSINFO);
		} catch (NoHConnectionException e) {
			LOG.error(e.getMessage());
		}

	}
}
