package server;

import org.apache.log4j.Logger;
import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;

import config.Constants;
import thrift.DbmsException;
import thrift.WorkerService;
import thrift.WorkerService.AsyncClient;
import thrift.WorkerService.AsyncClient.tenant_exportTempDb_call;
import worker.WorkerClient;

public class ExportTempDbResultHandler
		implements AsyncMethodCallback<WorkerService.AsyncClient.tenant_exportTempDb_call> {
	static private final Logger LOG = Logger.getLogger(Constants.LOGGER_NAME);
	private final WorkerClient WCLIENT;
	private final DbMigrator DBMIGRATOR;

	public ExportTempDbResultHandler(WorkerClient workerClient, DbMigrator dbMigrator) {
		WCLIENT = workerClient;
		DBMIGRATOR = dbMigrator;
	}

	@Override
	public void onComplete(tenant_exportTempDb_call response) {
		try {
			response.getResult();
			DBMIGRATOR.finishExport(true);
		} catch (DbmsException e) {
			LOG.error(e.getMessage());
			DBMIGRATOR.finishExport(false);
		} catch (TException e) {
			LOG.error(e.getMessage());
			DBMIGRATOR.finishExport(false);
		} finally {
			WCLIENT.setAsyncClientUseful((AsyncClient) response.getClient());
		}
	}

	@Override
	public void onError(Exception exception) {
		LOG.error(exception.getMessage());
		DBMIGRATOR.finishExport(false);
	}

}
