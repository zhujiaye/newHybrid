package worker;

import org.apache.log4j.Logger;
import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;

import config.Constants;
import thrift.WorkerService;
import thrift.WorkerService.AsyncClient;
import thrift.WorkerService.AsyncClient.async_tenant_copyDB_call;

public class CopyDBResultHandler implements AsyncMethodCallback<WorkerService.AsyncClient.async_tenant_copyDB_call> {
	static private final Logger LOG = Logger.getLogger(Constants.LOGGER_NAME);
	private final WorkerClient WCLIENT;
	private final int TENANT_ID;

	public CopyDBResultHandler(WorkerClient workerClient, int tenantID) {
		WCLIENT = workerClient;
		TENANT_ID = tenantID;
	}

	@Override
	public void onComplete(async_tenant_copyDB_call response) {
		try {
			response.getResult();
			//TODO
		} catch (TException e) {
			LOG.error(e.getMessage());
		} finally {	
			WCLIENT.setAsyncClientUseful((AsyncClient) response.getClient());
		}
	}

	@Override
	public void onError(Exception exception) {
		LOG.error(exception.getMessage());
	}
}
