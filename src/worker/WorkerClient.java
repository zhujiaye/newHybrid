package worker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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

import config.Constants;
import newhybrid.ClientShutdownException;
import newhybrid.NoServerConnectionException;
import newhybrid.Pair;
import thrift.ServerService;
import thrift.WorkerService;
import thrift.WorkerService.AsyncClient;
import thrift.WorkerService.AsyncClient.async_tenant_copyDB_call;

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
		if (!mAsyncClientStatus.isEmpty()) {
			mAsyncClientStatus.clear();
			for (TNonblockingTransport transport : mAsyncClientTransport.values()) {
				transport.close();
			}
			mAsyncClientTransport.clear();
		}
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
		System.out.println(mAsyncClientStatus.size() + " creating new ");
		TNonblockingTransport asyncTransport = new TNonblockingSocket(WORKER_ADDRESS, WORKER_PORT);
		WorkerService.AsyncClient asyncClient = new WorkerService.AsyncClient(new TBinaryProtocol.Factory(),
				new TAsyncClientManager(), asyncTransport);
		mAsyncClientStatus.put(asyncClient, true);
		mAsyncClientTransport.put(asyncClient, asyncTransport);
		System.out.println("create complete " + mAsyncClientStatus.size());
		return asyncClient;
	}

	public synchronized void setAsyncClientUseful(WorkerService.AsyncClient asyncClient) {
		mAsyncClientStatus.replace(asyncClient, false);
		System.out.println("set async client useful:" + asyncClient.toString());
	}

	public void async_tenant_copyDB(int ID,
			AsyncMethodCallback<WorkerService.AsyncClient.async_tenant_copyDB_call> resultHandler)
					throws ClientShutdownException {
		while (!mIsShutdown) {
			try {
				WorkerService.AsyncClient asyncClient = getUsefulAsyncClient();
				asyncClient.async_tenant_copyDB(ID, resultHandler);
				return;
			} catch (TException | IOException e) {
				LOG.error(e.getMessage());
			}
		}
		throw new ClientShutdownException("worker client is already shut down");
	}

	public static void main(String[] args) throws InterruptedException {

		new Thread(new Runnable() {
			@Override
			public void run() {
				WorkerClient client = new WorkerClient("10.0.2.15", 54321);
				for (int i = 1; i <= 20; i++) {
					System.out.println("start " + i);
					try {
						client.async_tenant_copyDB(i, new CopyDBResultHandler(client, i));
					} catch (ClientShutdownException e) {
						LOG.error(e.getMessage());
					}
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}).start();
		Thread.sleep(100000);
	}
}
