package server;

import org.apache.log4j.Logger;
import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;

import config.Constants;
import thrift.DbmsException;
import thrift.WorkerService;
import thrift.WorkerService.AsyncClient;
import thrift.WorkerService.AsyncClient.replay_call;
import worker.WorkerClient;

public class ReplayResultHandler implements AsyncMethodCallback<WorkerService.AsyncClient.replay_call> {
	static private final Logger LOG = Logger.getLogger(Constants.LOGGER_NAME);
	private final WorkerClient WCLIENT;
	private final DbMigrator DBMIGRATOR;

	public ReplayResultHandler(WorkerClient workerClient, DbMigrator dbMigrator) {
		WCLIENT = workerClient;
		DBMIGRATOR = dbMigrator;
	}

	@Override
	public void onComplete(replay_call response) {
		try {
			response.getResult();
			DBMIGRATOR.finishReplay(true);
		} catch (DbmsException e) {
			LOG.error(e.getMessage());
			DBMIGRATOR.finishReplay(false);
		} catch (TException e) {
			LOG.error(e.getMessage());
			DBMIGRATOR.finishReplay(false);
		} finally {
			WCLIENT.setAsyncClientUseful((AsyncClient) response.getClient());
		}
	}

	@Override
	public void onError(Exception exception) {
		LOG.error(exception.getMessage());
		DBMIGRATOR.finishReplay(false);
	}
}
