package worker;

import org.apache.thrift.TException;

import thrift.DbmsInfo;
import thrift.WorkerService;

public class WorkerServiceHandler implements WorkerService.Iface {
	private final WorkerInfo mWorkerInfo;

	public WorkerServiceHandler(WorkerInfo worker) {
		mWorkerInfo = worker;
	}

	@Override
	public void async_tenant_copyDB(int ID) throws TException {
		mWorkerInfo.copyDBForTenant(ID);
	}

	@Override
	public void async_tenant_moveDB(int ID, DbmsInfo dbmsInfo) throws TException {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
}
