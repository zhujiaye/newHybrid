package worker;

import thrift.WorkerService;

public class WorkerServiceHandler implements WorkerService.Iface {
	private final HWorker mWorker;

	public WorkerServiceHandler(HWorker worker) {
		mWorker = worker;
	}
}
