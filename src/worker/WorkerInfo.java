package worker;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import config.Constants;
import dbInfo.HConnection;
import dbInfo.HConnectionPool;
import newhybrid.NoHConnectionException;
import thrift.DbmsException;
import thrift.DbmsInfo;
import thrift.ServerWorkerInfo;
import thrift.TableInfo;
import thrift.TempDbInfo;
import thrift.TempTableInfo;

public class WorkerInfo {
	static private final Logger LOG = Logger.getLogger(Constants.LOGGER_NAME);
	private final String WORKER_ADDRESS;
	private final int WORKER_PORT;
	private final DbmsInfo DBMSINFO;
	private final String TEMPFOLDER;

	public WorkerInfo(String worker_address, int worker_port, DbmsInfo dbmsInfo, String tempFolder) {
		WORKER_ADDRESS = worker_address;
		WORKER_PORT = worker_port;
		DBMSINFO = dbmsInfo;
		TEMPFOLDER = tempFolder;
	}

	public void init() {
		// TODO
	}

	public void exportTempDbForTenant(int tenantID, TempDbInfo tempDbInfo) throws DbmsException {
		HConnectionPool pool = HConnectionPool.getPool();
		HConnection hConnection = null;
		try {
			hConnection = pool.getConnectionByDbmsInfo(DBMSINFO);
			List<TempTableInfo> tempTablesInfo = tempDbInfo.mTempTablesInfo;
			int nTables = tempTablesInfo.size();
			for (int i = 0; i < nTables; i++) {
				TempTableInfo tempTableInfo = tempTablesInfo.get(i);
				hConnection.exportTempTable(tenantID, tempTableInfo.mTableInfo,
						TEMPFOLDER + "/" + tempTableInfo.mTablePath);
			}
		} catch (NoHConnectionException e) {
			throw new DbmsException(e.getMessage());
		} finally {
			pool.putConnection(hConnection);
		}
	}

	public void moveTempDbForTenant(int tenantID, TempDbInfo tempDbInfo, ServerWorkerInfo workerInfo)
			throws DbmsException {
		HConnectionPool pool = HConnectionPool.getPool();
		HConnection hConnection = null;
		try {
			hConnection = pool.getConnectionByDbmsInfo(workerInfo.mDbmsInfo);
			List<TempTableInfo> tempTablesInfo = tempDbInfo.mTempTablesInfo;
			int nTables = tempTablesInfo.size();
			for (int i = 0; i < nTables; i++) {
				TempTableInfo tempTableInfo = tempTablesInfo.get(i);
				hConnection.importTempTable(tenantID, tempTableInfo.mTableInfo,
						TEMPFOLDER + "/" + tempTableInfo.mTablePath);
			}
		} catch (NoHConnectionException e) {
			throw new DbmsException(e.getMessage());
		} finally {
			pool.putConnection(hConnection);
		}
	}
}
