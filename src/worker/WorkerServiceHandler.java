package worker;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

import org.apache.thrift.TException;

import thrift.DbmsException;
import thrift.DbmsInfo;
import thrift.ServerWorkerInfo;
import thrift.TempDbInfo;
import thrift.WorkerService;
import utility.Utils;

public class WorkerServiceHandler implements WorkerService.Iface {
	private final WorkerInfo mWorkerInfo;

	public WorkerServiceHandler(WorkerInfo worker) {
		mWorkerInfo = worker;
	}

	@Override
	public void tenant_exportTempDb(int ID, TempDbInfo tempDbInfo) throws DbmsException, TException {
		mWorkerInfo.exportTempDbForTenant(ID, tempDbInfo);

	}

	@Override
	public void tenant_moveTempDb(int ID, TempDbInfo tempDbInfo, ServerWorkerInfo workerInfo)
			throws DbmsException, TException {
		mWorkerInfo.moveTempDbForTenant(ID, tempDbInfo, workerInfo);
	}

}
