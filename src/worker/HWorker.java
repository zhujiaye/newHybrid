package worker;

import java.net.InetSocketAddress;

import org.apache.log4j.Logger;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.THsHaServer;
import org.apache.thrift.server.TNonblockingServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.server.TThreadedSelectorServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TTransportException;

import config.Constants;
import config.WorkerConf;
import dbInfo.HConnection;
import dbInfo.HConnectionPool;
import dbInfo.HSQLException;
import newhybrid.ClientShutdownException;
import newhybrid.NoHConnectionException;
import server.ServerClient;
import server.ServerServiceHandler;
import thrift.DbmsInfo;
import thrift.DbmsType;
import thrift.ServerService;
import thrift.ServerWorkerInfo;
import thrift.WorkerService;

public class HWorker {
	static private final Logger LOG = Logger.getLogger(Constants.LOGGER_NAME);
	private final String SERVER_ADDRESS;
	private final int SERVER_PORT;
	private final String ADDRESS;
	private final int PORT;
	private final int SELECTOR_THREADS;
	private final int QUEUE_SIZE_PER_SELECTOR;
	private final int SERVER_THREADS;
	private final DbmsInfo DBMSINFO;

	private WorkerInfo mWorkerInfo;
	private WorkerServiceHandler mWorkerServiceHandler = null;
	private TNonblockingServerSocket mWorkerTNonblockingServerSocket = null;
	private TServer mWorkerServiceServer = null;

	private volatile boolean mIsStarted = false;

	public static void main(String[] args) throws TTransportException {
		WorkerConf conf = WorkerConf.getConf();
		DbmsInfo dbmsInfo;
		if (conf.WORKER_DBMS_TYPE == DbmsType.MYSQL)
			dbmsInfo = new DbmsInfo(conf.WORKER_DBMS_TYPE, conf.MYSQL_COMPLETE_CONNECTION_STRING, conf.MYSQL_USERNAME,
					conf.MYSQL_PASSWORD, 0);
		else
			dbmsInfo = new DbmsInfo(conf.WORKER_DBMS_TYPE, conf.VOLTDB_COMPLETE_CONNECTION_STRING, null, null,
					conf.VOLTDB_CAPACITY_MB);

		HWorker worker = new HWorker(conf.WORKER_ADDRESS, conf.WORKER_PORT, conf.WORKER_SELECTOR_THREADS,
				conf.WORKER_QUEUE_SIZE_PER_SELECTOR, conf.WORKER_SERVER_THREADS, conf.SERVER_ADDRESS, conf.SERVER_PORT,
				dbmsInfo, conf.WORKER_TEMP_FOLDER);
		worker.start();
	}

	public HWorker(String address, int port, int selector_threads, int queue_size_per_selector, int server_threads,
			String server_address, int server_port, DbmsInfo dbmsInfo, String tempFolder) {
		SERVER_ADDRESS = server_address;
		SERVER_PORT = server_port;
		ADDRESS = address;
		PORT = port;
		SELECTOR_THREADS = selector_threads;
		QUEUE_SIZE_PER_SELECTOR = queue_size_per_selector;
		SERVER_THREADS = server_threads;
		DBMSINFO = dbmsInfo;
		mWorkerInfo = new WorkerInfo(address, port, dbmsInfo, tempFolder);
	}

	public void start() throws TTransportException {
		if (mIsStarted)
			return;
		LOG.info("starting a " + (DBMSINFO.mType == DbmsType.MYSQL ? Constants.MYSQL_FLAG : Constants.VOLTDB_FLAG)
				+ " worker@" + ADDRESS + ":" + PORT + "......");
		LOG.info("setting up server service......");
		mWorkerInfo.init();
		mWorkerServiceHandler = new WorkerServiceHandler(mWorkerInfo);
		WorkerService.Processor<WorkerServiceHandler> workerServiceProcessor = new WorkerService.Processor<WorkerServiceHandler>(
				mWorkerServiceHandler);
		mWorkerTNonblockingServerSocket = new TNonblockingServerSocket(new InetSocketAddress(ADDRESS, PORT));
		mWorkerServiceServer = new TThreadedSelectorServer(
				new TThreadedSelectorServer.Args(mWorkerTNonblockingServerSocket).processor(workerServiceProcessor)
						.selectorThreads(SELECTOR_THREADS).acceptQueueSizePerThread(QUEUE_SIZE_PER_SELECTOR)
						.workerThreads(SERVER_THREADS));
//		mWorkerServiceServer = new TNonblockingServer(new TNonblockingServer.Args(mWorkerTNonblockingServerSocket)
//				.processor(workerServiceProcessor).transportFactory(new TFramedTransport.Factory())
//				.protocolFactory(new TBinaryProtocol.Factory()));
		new Thread(new Runnable() {

			@Override
			public void run() {
				mWorkerServiceServer.serve();

			}
		}).start();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			LOG.error(e);
		}
		mIsStarted = true;
		LOG.info("worker@" + ADDRESS + ":" + PORT + " started!......");
		LOG.info("registering this worker to server......");
		try {
			ServerClient serverClient = new ServerClient(SERVER_ADDRESS, SERVER_PORT);
			ServerWorkerInfo serverWorkerInfo = new ServerWorkerInfo(ADDRESS, PORT, DBMSINFO);
			if (serverClient.worker_register(serverWorkerInfo)) {
				LOG.info("register succeeded!");
			} else {
				LOG.warn("faild to register this worker: worker already exists");
				return;
			}
			serverClient.shutdown();
		} catch (ClientShutdownException e) {
			LOG.error(e.getMessage());
			return;
		}
	}
}
