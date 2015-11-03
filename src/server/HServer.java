package server;

import java.net.InetSocketAddress;

import org.apache.log4j.Logger;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadedSelectorServer;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TTransportException;

import thrift.DbmsInfo;
import thrift.DbmsType;
import thrift.ServerService;
import thrift.ServerWorkerInfo;
import newhybrid.HeartbeatThread;
import config.Constants;
import config.ServerConf;

/**
 * HServer maintains all the server-side information and is used to interact
 * with clients
 */
public class HServer {
	static private final Logger LOG = Logger.getLogger(Constants.LOGGER_NAME);
	private final String ADDRESS;
	private final int PORT;
	private final int SELECTOR_THREADS;
	private final int QUEUE_SIZE_PER_SELECTOR;
	private final int SERVER_THREADS;
	private final boolean USE_MEMMONITOR;
	private final boolean MODEL_DETERMINISTIC;

	private ServerInfo mServerInfo;
	private ServerServiceHandler mServerServiceHandler = null;
	private TNonblockingServerSocket mServerTNonblockingServerSocket = null;
	private TServer mServerServiceServer = null;
	private HeartbeatThread mServerOffloaderThread = null;
	private HeartbeatThread mServerMonitorThread = null;

	private volatile boolean mIsStarted = false;

	public HServer(String address, int port, int selector_threads,
			int queue_size_per_selector, int server_threads,
			boolean use_memmonitor, boolean model_deterministic,
			String imagePath) {
		ADDRESS = address;
		PORT = port;
		SELECTOR_THREADS = selector_threads;
		QUEUE_SIZE_PER_SELECTOR = queue_size_per_selector;
		SERVER_THREADS = server_threads;
		USE_MEMMONITOR = use_memmonitor;
		MODEL_DETERMINISTIC = model_deterministic;
		mServerInfo = new ServerInfo(ADDRESS, PORT, imagePath);
	}

	public void start() throws TTransportException {
		if (mIsStarted)
			return;
		LOG.info("starting server@" + ADDRESS + ":" + PORT + "......");
		LOG.info("retrivering all system information from journal......");
		mServerInfo.init();
	//	mServerInfo.importTpccTable();
		LOG.info("setting up server service......");
		mServerServiceHandler = new ServerServiceHandler(mServerInfo);
		ServerService.Processor<ServerServiceHandler> serverServiceProcessor = new ServerService.Processor<ServerServiceHandler>(
				mServerServiceHandler);
		mServerTNonblockingServerSocket = new TNonblockingServerSocket(
				new InetSocketAddress(ADDRESS, PORT));
		mServerServiceServer = new TThreadedSelectorServer(
				new TThreadedSelectorServer.Args(
						mServerTNonblockingServerSocket)
						.processor(serverServiceProcessor)
						.selectorThreads(SELECTOR_THREADS)
						.acceptQueueSizePerThread(QUEUE_SIZE_PER_SELECTOR)
						.workerThreads(SERVER_THREADS));
		LOG.info("setting up server offloader......");
		mServerOffloaderThread = new HeartbeatThread("server_offloader",
				new ServerOffloaderHeartbeatExecutor(mServerInfo),
				Constants.OFFLOADER_FIXED_INTERVAL_TIME);
		mServerOffloaderThread.start();
		LOG.info("setting up server monitor......");
		mServerMonitorThread = new HeartbeatThread("server_monitor",
				new ServerMonitorHeartbeatExecutor(mServerInfo),
				Constants.SPLIT_TIME);
		mServerMonitorThread.start();
		mIsStarted = true;
		LOG.info("server@" + ADDRESS + ":" + PORT + " started!......");
		new Thread(new Runnable() {
			@Override
			public void run() {
				mServerServiceServer.serve();
			}
		}).start();
	}

	public void stop() {
		if (!mIsStarted)
			return;
		LOG.info("stopping server@" + ADDRESS + ":" + PORT + "......");
		if (mServerServiceServer != null) {
			LOG.info("server service stopping......");
			mServerServiceServer.stop();
		}
		if (mServerTNonblockingServerSocket != null) {
			LOG.info("server socket closing......");
			mServerTNonblockingServerSocket.close();
		}
		if (mServerOffloaderThread != null) {
			LOG.info("server offloader shutting down......");
			mServerOffloaderThread.shutdown();
		}
		if (mServerMonitorThread != null) {
			LOG.info("server monitor shutting down......");
			mServerMonitorThread.shutdown();
		}
		mIsStarted = false;
		LOG.info("server@" + ADDRESS + ":" + PORT + " stopped!");
	}

	public String getAddress() {
		return ADDRESS;
	}

	public int getPort() {
		return PORT;
	}

	public static void main(String[] args) throws TTransportException {
		ServerConf conf = ServerConf.getConf();
		HServer server = new HServer(conf.SERVER_ADDRESS, conf.SERVER_PORT,
				conf.SERVER_SELECTOR_THREADS,
				conf.SERVER_QUEUE_SIZE_PER_SELECTOR,
				conf.SERVER_SERVER_THREADS, conf.SERVER_USE_MEMMONITOR,
				conf.MODEL_DETERMINISTIC, conf.SERVER_IMAGE_PATH);
		server.start();
	}
}
