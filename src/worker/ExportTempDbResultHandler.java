package worker;

import org.apache.log4j.Logger;
import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;

import config.Constants;
import thrift.DbmsException;
import thrift.WorkerService;
import thrift.WorkerService.AsyncClient;
import thrift.WorkerService.AsyncClient.tenant_exportTempDb_call;

public class ExportTempDbResultHandler implements AsyncMethodCallback<WorkerService.AsyncClient.tenant_exportTempDb_call> {
	static private final Logger LOG = Logger.getLogger(Constants.LOGGER_NAME);
	private final WorkerClient WCLIENT;

	public ExportTempDbResultHandler(WorkerClient workerClient) {
		WCLIENT = workerClient;
	}

	@Override
	public void onComplete(tenant_exportTempDb_call response) {
		try {
			response.getResult();
			System.out.println("export db complete");
		} catch (DbmsException e) {
			LOG.error(e.getMessage());
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
