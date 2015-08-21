package worker;

import org.apache.log4j.Logger;

import client.ServerClient;
import config.Constants;
import config.WorkerConf;
import thrift.DbmsType;

public class Worker {
	private final static Logger LOG = Logger.getLogger(Constants.LOGGER_NAME);
	private final WorkerConf mWorkerConf;
	private boolean isStarted = false;
	private ServerClient mServerClient;

	public Worker() {
		mWorkerConf = WorkerConf.getConf();
		mServerClient = new ServerClient(mWorkerConf.SERVER_ADDRESS, mWorkerConf.SERVER_PORT);
	}

	public void start() {
		if (isStarted)
			return;
		LOG.info("try to start a worker@" + mWorkerConf.WORKER_ADDRESS + ":" + mWorkerConf.WORKER_PORT);
		if (mWorkerConf.WORKER_DBMS_TYPE == DbmsType.VOLTDB) {
			
		}
	}
}
