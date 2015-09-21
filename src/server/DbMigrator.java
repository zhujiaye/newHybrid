package server;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import config.Constants;
import dbInfo.HConnection;
import dbInfo.HConnectionPool;
import newhybrid.ClientShutdownException;
import newhybrid.NoServerConnectionException;
import thrift.DbStatus;
import thrift.DbStatusInfo;
import thrift.DbmsException;
import thrift.DbmsInfo;
import thrift.NoTenantException;
import thrift.NoWorkerException;
import thrift.Operation;
import thrift.OperationPara;
import thrift.ServerWorkerInfo;
import thrift.TableInfo;
import thrift.TempDbInfo;
import thrift.TempTableInfo;
import worker.WorkerClient;

public class DbMigrator extends Thread {
	static private final Logger LOG = Logger.getLogger(Constants.LOGGER_NAME);
	private final ServerInfo SERVERINFO;
	private final int TENANTID;
	private final ServerWorkerInfo FROM;
	private final WorkerClient FROMCLIENT;

	private WorkerClient TOCLIENT;
	private ServerWorkerInfo TO;
	private ServerWorkerInfo newTO;
	private boolean mIsCanceled = false;
	private boolean mIsFinished = false;
	private boolean mIsExporting = false;
	private boolean mIsExported = false;
	private boolean mIsMoving = false;
	private boolean mIsMoved = false;
	private boolean mIsReplaying = false;
	private boolean mIsReplayed = false;
	private boolean mChangingTo = false;

	private boolean mIsLocked = false;
	private TempDbInfo tempDbInfo;

	private List<Operation> mOperations;

	public DbMigrator(ServerInfo serverInfo, int tenantID, ServerWorkerInfo from, ServerWorkerInfo to) {
		SERVERINFO = serverInfo;
		TENANTID = tenantID;
		FROM = from;
		TO = to;
		FROMCLIENT = new WorkerClient(FROM.mAddress, FROM.mPort);
		TOCLIENT = new WorkerClient(TO.mAddress, TO.mPort);
		mOperations = new ArrayList<>();
	}

	private void exportTempDb() throws NoTenantException, NoWorkerException, DbmsException {
		ArrayList<TableInfo> tablesInfo = SERVERINFO.getTablesForTenant(TENANTID);
		ArrayList<TempTableInfo> tempTablesInfo = new ArrayList<>();
		int nTables = tablesInfo.size();
		for (int i = 0; i < nTables; i++) {
			TableInfo currentTableInfo = tablesInfo.get(i);
			String tempPath = "tenant" + TENANTID + "_" + currentTableInfo.mName;
			TempTableInfo currentTempTableInfo = new TempTableInfo(currentTableInfo, tempPath);
			tempTablesInfo.add(currentTempTableInfo);
		}
		tempDbInfo = new TempDbInfo(tempTablesInfo);
		synchronized (this) {
			try {
				mIsExporting = true;
				FROMCLIENT.async_tenant_exportTempDb(TENANTID, tempDbInfo,
						new ExportTempDbResultHandler(FROMCLIENT, this));
			} catch (ClientShutdownException | NoServerConnectionException e) {
				mIsExporting = false;
				throw new DbmsException(e.getMessage());
			}
		}
	}

	private void moveTempDb() throws DbmsException {
		synchronized (this) {
			try {
				mIsMoving = true;
				FROMCLIENT.async_tenant_moveTempDb(TENANTID, tempDbInfo, TO,
						new MoveTempDbResultHandler(FROMCLIENT, this));
			} catch (ClientShutdownException | NoServerConnectionException e) {
				mIsMoving = false;
				throw new DbmsException(e.getMessage());
			}
		}
	}

	private void replay() throws DbmsException {
		synchronized (this) {
			try {
				mIsReplaying = true;
				synchronized (mOperations) {
					for (int i = 0; i < mOperations.size(); i++)
						LOG.debug(mOperations.get(i).toString());
					TOCLIENT.async_replay(mOperations, new ReplayResultHandler(TOCLIENT, this));
				}
			} catch (ClientShutdownException | NoServerConnectionException e) {
				mIsReplayed = false;
				throw new DbmsException(e.getMessage());
			}
		}
	}

	private synchronized void try_lock() throws NoTenantException, InterruptedException {
		if (mIsLocked)
			return;
		SERVERINFO.lockLockForTenant(TENANTID);
		LOG.debug("++++++++++++++lock++++++++++++++++++");
		mIsLocked = true;
	}

	private synchronized void try_release() {
		if (!mIsLocked)
			return;
		SERVERINFO.releaseLockForTenant(TENANTID);
		LOG.debug("---------------unlock---------------");
		mIsLocked = false;
	}

	public void addOperation(Operation operation) {
		synchronized (mOperations) {
			mOperations.add(operation);
		}
	}

	public void finishExport(boolean success) {
		synchronized (this) {
			if (mIsFinished || mIsCanceled)
				return;
			mIsExporting = false;
			if (!success) {
				mIsCanceled = true;
			} else {
				mIsExported = true;
				LOG.debug(String.format("export DB for tenant %d finished....%n", TENANTID));
			}
			this.notify();
		}
	}

	public void finishMoving(boolean success) {
		synchronized (this) {
			if (mIsCanceled || mIsFinished)
				return;
			mIsMoving = false;
			if (!success) {
				mIsCanceled = true;
			} else {
				mIsMoved = true;
				LOG.debug(String.format("move DB for tenant %d finished....%n", TENANTID));
			}
			this.notifyAll();
		}
	}

	public void finishReplay(boolean success) {
		synchronized (this) {
			if (mIsCanceled || mIsFinished)
				return;
			mIsReplaying = false;
			if (!success) {
				mIsCanceled = true;
			} else {
				mIsReplayed = true;
				LOG.debug(String.format("replay for tenant %d finished....%n", TENANTID));
			}
			this.notifyAll();
		}
	}

	// public void cancel() {
	// synchronized (this) {
	// mIsCanceled = true;
	// }
	// }

	public void migrateToNewWorker(ServerWorkerInfo newWorker) {
		synchronized (this) {
			mChangingTo = true;
			newTO = newWorker;
		}
	}

	public int getTenantID() {
		return TENANTID;
	}

	public synchronized boolean isMigrating() {
		return !mIsCanceled && !mIsFinished || mIsExporting || mIsMoving || mIsReplaying;
	}

	@Override
	public void run() {
		synchronized (this) {
			try {
				while (isMigrating()) {
					if (mIsExporting || mIsMoving || mIsReplaying) {
						this.wait();
					} else if (mChangingTo) {
						LOG.debug(String.format("change destination to new worker for tenant %d....%n", TENANTID));
						mChangingTo = false;
						TOCLIENT.shutdown();
						TO = newTO;
						TOCLIENT = new WorkerClient(TO.mAddress, TO.mPort);
						mIsExported = mIsMoved = mIsReplayed = false;
						synchronized (mOperations) {
							mOperations.clear();
						}
						try_lock();
						SERVERINFO.setDbStatusForTenant(TENANTID, DbStatus.NORMAL);
						try_release();
					} else if (!mIsExported) {
						try_lock();
						LOG.debug(String.format("export DB for tenant %d....%n", TENANTID));
						exportTempDb();
					} else if (!mIsMoved) {
						try_lock();
						LOG.debug(String.format("mark tenant %d's DB as MIGRATING....%n", TENANTID));
						SERVERINFO.setDbStatusForTenant(TENANTID, DbStatus.MIGRATING);
						try_release();
						LOG.debug(String.format("move DB for tenant %d....%n", TENANTID));
						moveTempDb();
					} else if (!mIsReplayed) {
						try_lock();
						LOG.debug(String.format("replay for tenant %d....%n", TENANTID));
						replay();
					} else {
						try_lock();
						LOG.debug(String.format("mark tenant %d's DB as NORMAL....%n", TENANTID));
						SERVERINFO.setDbStatusForTenant(TENANTID, DbStatus.NORMAL);
						SERVERINFO.setDbmsForTenant(TENANTID, TO.mDbmsInfo);
						SERVERINFO.writeToImage();
						LOG.debug(String.format("migrate for tenant %d succeeded!....%n", TENANTID));
						try_release();
						mIsFinished = true;
					}
				}
				if (mIsCanceled) {
					LOG.debug(String.format("migrate for tenant %d canceled!....%n", TENANTID));
				}
			} catch (NoTenantException | InterruptedException | NoWorkerException | DbmsException e) {
				LOG.error(e.getMessage());
				mIsCanceled = true;
				return;
			} finally {
				FROMCLIENT.shutdown();
				TOCLIENT.shutdown();
				try {
					try_lock();
					SERVERINFO.setDbStatusForTenant(TENANTID, DbStatus.NORMAL);
				} catch (NoTenantException | InterruptedException e) {
					LOG.error(e.getMessage());
				}
				try_release();
				LOG.debug("thread finish....");
			}
		}
	}

}
