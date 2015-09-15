package worker;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;
import org.apache.thrift.async.TAsyncClientManager;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingSocket;
import org.apache.thrift.transport.TNonblockingTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import client.TenantClient;
import config.Constants;
import newhybrid.ClientShutdownException;
import newhybrid.NoServerConnectionException;
import newhybrid.Pair;
import thrift.DbmsInfo;
import thrift.DbmsType;
import thrift.NoTenantException;
import thrift.ServerService;
import thrift.ServerWorkerInfo;
import thrift.TableInfo;
import thrift.TempDbInfo;
import thrift.TempTableInfo;
import thrift.WorkerService;
import thrift.WorkerService.AsyncClient;

public class WorkerClient {
	static private final Logger LOG = Logger.getLogger(Constants.LOGGER_NAME);
	private final String WORKER_ADDRESS;
	private final int WORKER_PORT;

	private volatile boolean mIsShutdown = false;
	private volatile boolean mIsConnected = false;
	private TProtocol mProtocol = null;
	private WorkerService.Client mClient = null;

	private Map<WorkerService.AsyncClient, Boolean> mAsyncClientStatus = new HashMap<>();
	private Map<WorkerService.AsyncClient, TNonblockingTransport> mAsyncClientTransport = new HashMap<>();

	public WorkerClient(String address, int port) {
		WORKER_ADDRESS = address;
		WORKER_PORT = port;
	}

	private synchronized void cleanConnect() {
		if (mIsConnected) {
			LOG.debug("worker-client disconnecting from worker@" + WORKER_ADDRESS + ":" + WORKER_PORT);
			mIsConnected = false;
		}
		if (mProtocol != null) {
			mProtocol.getTransport().close();
		}
		// if (!mAsyncClientStatus.isEmpty()) {
		// mAsyncClientStatus.clear();
		// for (TNonblockingTransport transport :
		// mAsyncClientTransport.values()) {
		// transport.close();
		// }
		// mAsyncClientTransport.clear();
		// }
	}

	private synchronized void connect() throws ClientShutdownException, NoServerConnectionException {
		if (mIsShutdown) {
			throw new ClientShutdownException("worker client is already shut down!");
		}
		if (mIsConnected)
			return;
		cleanConnect();
		int tries = 0;
		while (tries++ < Constants.MAX_CONNECT_TRY && !mIsShutdown) {
			mProtocol = new TBinaryProtocol(new TFramedTransport(new TSocket(WORKER_ADDRESS, WORKER_PORT)));
			mClient = new WorkerService.Client(mProtocol);
			try {
				mProtocol.getTransport().open();
			} catch (TTransportException e) {
				LOG.error("Failed to connect (" + tries + ") to server:" + e.getMessage());
				try {
					Thread.sleep(Constants.S / 1000000);
				} catch (InterruptedException e1) {
					LOG.error("Interrupted while waiting to connect to server:" + e1.getMessage());
				}
				continue;
			}
			mIsConnected = true;
			return;
		}

		// Reaching here indicates that we did not successfully connect.
		throw new NoServerConnectionException("Failed to connect to worker@" + WORKER_ADDRESS + ":" + WORKER_PORT
				+ " after " + (tries - 1) + " attempts");
	}

	public void shutdown() {
		if (mIsShutdown)
			return;
		cleanConnect();
		mIsShutdown = true;
	}

	private synchronized WorkerService.AsyncClient getUsefulAsyncClient() throws IOException {
		Iterator<Entry<AsyncClient, Boolean>> iterator = mAsyncClientStatus.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<AsyncClient, Boolean> entry = iterator.next();
			if (entry.getValue() == false) {
				WorkerService.AsyncClient result = entry.getKey();
				mAsyncClientStatus.replace(result, true);
				return result;
			}
		}
		// System.out.println(mAsyncClientStatus.size() + " creating new ");
		TNonblockingTransport asyncTransport = new TNonblockingSocket(WORKER_ADDRESS, WORKER_PORT);
		WorkerService.AsyncClient asyncClient = new WorkerService.AsyncClient(new TBinaryProtocol.Factory(),
				new TAsyncClientManager(), asyncTransport);
		mAsyncClientStatus.put(asyncClient, true);
		mAsyncClientTransport.put(asyncClient, asyncTransport);
		// System.out.println("create complete " + mAsyncClientStatus.size());
		return asyncClient;
	}

	public synchronized void setAsyncClientUseful(WorkerService.AsyncClient asyncClient) {
		mAsyncClientStatus.replace(asyncClient, false);
		// System.out.println("set async client useful:" +
		// asyncClient.toString());
	}

	public void async_tenant_exportTempDb(int ID, TempDbInfo tempDbInfo,
			AsyncMethodCallback<WorkerService.AsyncClient.tenant_exportTempDb_call> resultHandler)
					throws ClientShutdownException {
		while (!mIsShutdown) {
			try {
				WorkerService.AsyncClient asyncClient = getUsefulAsyncClient();
				asyncClient.tenant_exportTempDb(ID, tempDbInfo, resultHandler);
				return;
			} catch (TException | IOException e) {
				LOG.error(e.getMessage());
			}
		}
		throw new ClientShutdownException("worker client is already shut down");
	}

	public void async_tenant_moveTempDb(int ID, TempDbInfo tempDbInfo, ServerWorkerInfo workerInfo,
			AsyncMethodCallback<WorkerService.AsyncClient.tenant_moveTempDb_call> resultHandler)
					throws ClientShutdownException {
		while (!mIsShutdown) {
			try {
				WorkerService.AsyncClient asyncClient = getUsefulAsyncClient();
				asyncClient.tenant_moveTempDb(ID, tempDbInfo, workerInfo, resultHandler);
				return;
			} catch (TException | IOException e) {
				LOG.error(e.getMessage());
			}
		}
		throw new ClientShutdownException("worker client is already shut down");
	}

	public static void main(String[] args) throws InterruptedException, NoTenantException, ClientShutdownException {
		WorkerClient wclient = new WorkerClient("192.168.0.30", 54322);
		ServerWorkerInfo mysqlWorker = new ServerWorkerInfo("192.168.0.30", 54321,
				new DbmsInfo(DbmsType.MYSQL, "jdbc:mysql://192.168.0.30/newhybrid", "remote", "remote", 2000));
		ServerWorkerInfo voltdbWorker = new ServerWorkerInfo("192.168.0.30", 54322,
				new DbmsInfo(DbmsType.VOLTDB, "192.168.0.30", "remote", "remote", 2000));
		TenantClient tclient = new TenantClient(1, "192.168.0.35", 12345);
		List<TableInfo> tablesInfo = tclient.getTables();
		List<TempTableInfo> tempTablesInfo = new ArrayList<>();
		for (int i = 0; i < tablesInfo.size(); i++) {
			tempTablesInfo.add(new TempTableInfo(tablesInfo.get(i), "tenant1_" + tablesInfo.get(i).mName));
		}
		TempDbInfo tempDbInfo = new TempDbInfo(tempTablesInfo);
		wclient.async_tenant_exportTempDb(1, tempDbInfo, new ExportTempDbResultHandler(wclient));
		Thread.sleep(5000);
		wclient.async_tenant_moveTempDb(1, tempDbInfo, voltdbWorker, new MoveTempDbResultHandler(wclient));
		wclient.shutdown();
		System.out.println("-------------");
		Object obj = new Object();
		synchronized (obj) {
			obj.wait();
		}
	}
}
