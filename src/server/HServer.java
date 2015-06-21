package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringBufferInputStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Appender;
import org.apache.log4j.Layout;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.ErrorHandler;
import org.apache.log4j.spi.Filter;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadedSelectorServer;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TTransportException;
import org.voltdb.VoltTable;
import org.voltdb.client.Client;
import org.voltdb.client.ClientResponse;
import org.voltdb.client.ProcCallException;

import thrift.ServerService;
import utillity.VoltdbConnectionPool;
import newhybrid.HException;
import newhybrid.HeartbeatThread;
import config.Constants;
import config.HConfig;

/*
 * HServer maintains all the server-side information and is used to interact with clients
 */
public class HServer {
	final static private Logger LOG = Logger
			.getLogger(Constants.LOGGER_NAME_SERVER);
	final private HConfig mConf;
	final private String mAddress;
	final private int mPort;
	final private int mVoltdbSpaceTotal;

	private boolean isStarted;
	private Map<Integer, HTenant> mTenants;
	// private Map<Integer, Integer> mTenantsVoltdbID;
	private Map<Integer, List<Integer>> mVoltdbIDList;

	private ServerServiceHandler mServerServiceHandler;
	private TNonblockingServerSocket mServerTNonblockingServerSocket;
	private TServer mServerServiceServer;
	private HeartbeatThread mServerOffloaderThread = null;
	private HeartbeatThread mServerMonitorThread = null;
	private Mover mMover;

	private int mLastThroughput = -1;
	private int mNowTroughput = 0;

	public HServer() throws HException {
		mConf = HConfig.getConf();
		mAddress = mConf.getServerAddress();
		mPort = mConf.getServerPort();
		mVoltdbSpaceTotal = mConf.getVoltdbCapacity();
		mTenants = new HashMap<>();
		mMover = new Mover(this);
		// mTenantsVoltdbID = new HashMap<>();
		mVoltdbIDList = new HashMap<>();
		isStarted = false;
	}

	public void start() throws HException, TTransportException {
		if (isStarted)
			return;
		LOG.info("Server starting....");
		if (mConf.isUseVoltdb()) {
			LOG.info("clearing voltdb contents....");
			clearVoltdb();
			LOG.info("voltdb contents cleared......20%");
		}
		LOG.info("clearing tenants information.......");
		mTenants.clear();
		mVoltdbIDList.clear();
		for (int i = 1; i <= Constants.NUMBER_OF_VOLTDBID; i++) {
			mVoltdbIDList.put(i, new ArrayList<Integer>());
		}
		LOG.info("tenants information cleared......40%");
		LOG.info("getting all registered tenants....");
		// TODO For test : get workloads for all tenants
		for (int i = 1; i <= Constants.NUMBER_OF_TENANTS; i++) {
			HTenant tenant = new HTenant(this, i);
			mTenants.put(i, tenant);
		}
		LOG.info("all registered tenants got.......60%");
		LOG.info("setting up server service......");
		mServerServiceHandler = new ServerServiceHandler(this);
		ServerService.Processor<ServerServiceHandler> serverServiceProcessor = new ServerService.Processor<ServerServiceHandler>(
				mServerServiceHandler);

		mServerTNonblockingServerSocket = new TNonblockingServerSocket(
				new InetSocketAddress(mAddress, mPort));
		mServerServiceServer = new TThreadedSelectorServer(
				new TThreadedSelectorServer.Args(
						mServerTNonblockingServerSocket)
						.processor(serverServiceProcessor)
						.selectorThreads(Constants.SELECTOR_THREADS)
						.acceptQueueSizePerThread(
								Constants.QUEUE_SIZE_PER_SELECTOR)
						.workerThreads(Constants.SERVER_THREADS));
		isStarted = true;
		LOG.info("server service start to work......80%");
		LOG.info("Server@" + mAddress + ":" + mPort + " started!......100%");
		mServerOffloaderThread = new HeartbeatThread("Server_Offloader",
				new ServerOffloaderHeartbeatExecutor(this),
				Constants.OFFLOADER_FIXED_INTERVAL_TIME);
		mServerOffloaderThread.start();
		mServerMonitorThread = new HeartbeatThread("Server_Monitor",
				new ServerMonitorHeartbeatExecutor(this), Constants.SPLIT_TIME);
		mServerMonitorThread.start();
		mServerServiceServer.serve();
	}

	public void stop() throws HException {
		if (!isStarted)
			return;
		LOG.info("Server stopping......");
		LOG.info("Loggint out for all tenants......");
		for (Entry<Integer, HTenant> tenantEntry : mTenants.entrySet()) {
			HTenant tenant = tenantEntry.getValue();
			tenantLogout(tenant.getID());
			LOG.debug("Tenant " + tenant.getID() + " logged out...");
		}
		if (mServerServiceServer != null) {
			LOG.info("ServerService stopping...");
			mServerServiceServer.stop();
		}
		if (mServerTNonblockingServerSocket != null) {
			LOG.info("ServerSocket closing...");
			mServerTNonblockingServerSocket.close();
		}
		if (mServerOffloaderThread != null) {
			LOG.info("ServerOffloader shutting down...");
			mServerOffloaderThread.shutdown();
		}
		isStarted = false;
		LOG.info("Server@" + mAddress + ":" + mPort + " stopped!");
	}

	public String getAddress() {
		return mAddress;
	}

	public int getPort() {
		return mPort;
	}

	public boolean tenantLogin(int id) throws HException {
		HTenant tenant;
		tenant = mTenants.get(id);
		if (tenant != null) {
			if (tenant.isLoggedIn())
				return true;
			if (mConf.getInitdb().equals(Constants.INITDB_VOLTDB)
					&& mConf.isUseVoltdb()) {
				int tenant_id = tenant.getID();
				int voltdbID = getAndAddNewVoltdbIDForTenant(tenant_id);
				MysqlToVoltdbMoverThread thread = new MysqlToVoltdbMoverThread(
						tenant, voltdbID, false);
				tenant.startMovingToVoltdb(thread);
				thread.start();
				LOG.debug("Moving tenant " + tenant.getID()
						+ " 's data to voltdb(" + voltdbID + ").....");
				try {
					thread.join();
				} catch (InterruptedException e) {
					throw new HException(
							"ERROR when moving data to voltdb: interrupted!");
				}
			}
			tenant.login();
			return true;
		}
		return false;
	}

	public boolean tenantLogout(int id) throws HException {
		HTenant tenant;
		tenant = mTenants.get(id);
		if (tenant != null) {
			if (!tenant.isLoggedIn())
				return true;
			synchronized (tenant) {
				tenant.logout();
				if (tenant.isBeingMovingToMysql()) {
					tenant.getMovingToMysqlThread().cancel();
					tenant.cancelMoving();
				}
				if (tenant.isBeingMovingToVoltdb()) {
					tenant.getMovingToVoltdbThread().cancel();
					tenant.cancelMoving();
					removeVoltdbIDForTenant(tenant.getID());
				}
			}
			if (!tenant.isInMysql() && mConf.isUseMysql()) {
				VoltdbToMysqlMoverThread thread = new VoltdbToMysqlMoverThread(
						tenant, false);
				tenant.startMovingToMysql(thread);
				thread.start();
				LOG.debug("Writing tenant " + tenant.getID()
						+ " 's data back to mysql.....");
				try {
					thread.join();
				} catch (InterruptedException e) {
					throw new HException(
							"ERROR when moving data back to mysql: interrupted!");
				}
			}
			return true;
		}
		return false;
	}

	public boolean tenantStart(int id) {
		HTenant tenant;
		tenant = mTenants.get(id);
		if (tenant != null) {
			tenant.start();
			return true;
		}
		return false;
	}

	public boolean tenantStop(int id) {
		HTenant tenant;
		tenant = mTenants.get(id);
		if (tenant != null) {
			tenant.stop();
			return true;
		}
		return false;
	}

	public boolean tenantIsLoggedIn(int id) {
		HTenant tenant;
		tenant = mTenants.get(id);
		if (tenant != null) {
			return tenant.isLoggedIn();
		}
		return false;
	}

	public boolean tenantIsStarted(int id) {
		HTenant tenant;
		tenant = mTenants.get(id);
		if (tenant != null) {
			return tenant.isStarted();
		}
		return false;
	}

	public int tenantGetIDInVoltdb(int id) {
		HTenant tenant;
		tenant = mTenants.get(id);
		if (tenant != null) {
			return tenant.getIDInVoltdb();
		}
		return -1;
	}

	public int tenantGetDataSize(int id) {
		HTenant tenant;
		tenant = mTenants.get(id);
		if (tenant != null) {
			return tenant.getDataSize();
		}
		return -1;
	}

	public int tenantGetDataSizeKind(int id) {
		HTenant tenant;
		tenant = mTenants.get(id);
		if (tenant != null) {
			return tenant.getDataSizeKind();
		}
		return -1;
	}

	public boolean tenantIsUseMysql(int id) {
		HTenant tenant;
		tenant = mTenants.get(id);
		if (tenant != null) {
			return tenant.isInMysql();
		}
		return false;
	}

	public boolean tenantCompleteOneQuery(int id) {
		HTenant tenant;
		tenant = mTenants.get(id);
		if (tenant != null) {
			synchronized (tenant) {
				if (!tenant.isStarted())
					return false;
				synchronized (this) {
					mNowTroughput++;
					return true;
				}
			}
		}
		return false;
	}

	/*
	 * TODO make this better
	 */
	public void offloadWorkloads() throws HException {
		LOG.info("offloader checking...");
		if (!mConf.isUseMysql() || !mConf.isUseVoltdb()) {
			return;
		}
		HTenant[] tenantsInMysqlAhead;
		HTenant[] tenantsInMysqlNow;
		HTenant[] tenantsInVoltdbAhead;
		HTenant[] tenantsInVoltdbNow;
		int workloadLimitInMysql;
		int workloadInMysqlAhead, freeWorkloadInMysqlAhead;
		int workloadInMysqlNow, freeWorkloadInMysqlNow;
		int availableSpaceInVoltdbNow, availableSpaceInVoltdbAhead;
		workloadLimitInMysql = getWorkloadLimitInMysql();
		tenantsInMysqlAhead = findTenantsInMysqlAhead();
		tenantsInMysqlNow = findTenantsInMysqlNow();
		tenantsInVoltdbAhead = findTenantsInVoltdbAhead();
		tenantsInVoltdbNow = findTenantsInVoltdbNow();
		workloadInMysqlAhead = getWorkloadAhead(tenantsInMysqlAhead);
		workloadInMysqlNow = getWorkloadNow(tenantsInMysqlNow);
		availableSpaceInVoltdbNow = mVoltdbSpaceTotal
				- getSpace(tenantsInVoltdbNow);
		availableSpaceInVoltdbAhead = mVoltdbSpaceTotal
				- getSpace(tenantsInVoltdbAhead);
		freeWorkloadInMysqlAhead = workloadLimitInMysql - workloadInMysqlAhead;
		freeWorkloadInMysqlNow = workloadLimitInMysql - workloadInMysqlNow;

		int numberOfTenantsMovingToVoltdb = 0;
		int numberOfTenantsMovingToMysql = 0;
		for (int i = 0; i < tenantsInMysqlNow.length; i++)
			if (tenantsInMysqlNow[i].isBeingMovingToVoltdb())
				numberOfTenantsMovingToVoltdb++;
		for (int i = 0; i < tenantsInVoltdbNow.length; i++)
			if (tenantsInVoltdbNow[i].isBeingMovingToMysql())
				numberOfTenantsMovingToMysql++;
		LOG.info(String.format("MySQL:%d(%d) tenants  VoltDB:%d(%d) tenants",
				tenantsInMysqlNow.length, numberOfTenantsMovingToVoltdb,
				tenantsInVoltdbNow.length, numberOfTenantsMovingToMysql));
		if (LOG.isDebugEnabled()) {
			LOG.debug(String.format("MySQL workload NOW:%d(%d)",
					workloadInMysqlNow, workloadLimitInMysql));
			LOG.debug(String.format("MySQL workload AHEAD:%d(%d)",
					workloadInMysqlAhead, workloadLimitInMysql));
			LOG.debug(String.format("VoltDB memory NOW:%d(%d)",
					availableSpaceInVoltdbNow, mVoltdbSpaceTotal));
			LOG.debug(String.format("VoltDB memory AHEAD:%d(%d)",
					availableSpaceInVoltdbAhead, mVoltdbSpaceTotal));
			LOG.debug(String.format("%20s%10s%20s", "MySQL", "", "VoltDB"));
			for (Entry<Integer, HTenant> tenantEntry : mTenants.entrySet()) {
				HTenant tenant = tenantEntry.getValue();
				synchronized (tenant) {
					if (!tenant.isStarted())
						continue;
					StringBuilder str = new StringBuilder();
					str.append(tenant.getID() + "(" + tenant.getWorkloadAhead()
							+ "," + tenant.getDataSize() + ")");
					if (tenant.isInMysql()) {
						StringBuilder tmp = new StringBuilder();
						if (tenant.isBeingMovingToVoltdb()) {
							tmp.append("----");
							tmp.append(tenant.getMovingToVoltdbThread()
									.getVoltdbID());
							tmp.append("-------->");
							LOG.debug(String.format("%20s", str.toString())
									+ String.format("%10s", tmp));
						} else
							LOG.debug(String.format("%20s", str.toString())
									+ String.format("%10s", ""));
					} else {
						if (tenant.isBeingMovingToMysql())
							LOG.debug(String.format("%20s", "")
									+ String.format("%10s", "<------------")
									+ String.format("%20s", str.toString()));
						else
							LOG.debug(String.format("%20s", "")
									+ String.format("%10s", "")
									+ String.format("%20s", str.toString()));
					}
				}
			}
		}
		if (freeWorkloadInMysqlAhead < 0) {
			Arrays.sort(tenantsInMysqlAhead);
			Arrays.sort(tenantsInVoltdbAhead);
			int workloadNeedToOffload = -freeWorkloadInMysqlAhead;
			int j = tenantsInVoltdbAhead.length - 1;
			for (int i = 0; i < tenantsInMysqlAhead.length
					&& workloadNeedToOffload > 0; i++) {
				HTenant tenantToOffload = tenantsInMysqlAhead[i];
				while (availableSpaceInVoltdbAhead < tenantToOffload
						.getDataSize() && j >= 0) {
					HTenant tenantToWriteBack = tenantsInVoltdbAhead[j];
					synchronized (tenantToWriteBack) {
						if (!tenantToWriteBack.isStarted())
							continue;
						if (tenantToWriteBack.getWorkloadAhead() > tenantToOffload
								.getWorkloadAhead()) {
							j--;
							continue;
						}
						if (tenantToWriteBack.isBeingMovingToVoltdb()) {
							tenantToWriteBack.getMovingToVoltdbThread()
									.cancel();
							tenantToWriteBack.cancelMoving();
							removeVoltdbIDForTenant(tenantToWriteBack.getID());
						} else {
							VoltdbToMysqlMoverThread thread = new VoltdbToMysqlMoverThread(
									tenantToWriteBack, true);
							tenantToWriteBack.startMovingToMysql(thread);
							mMover.addThread(thread);
						}
						availableSpaceInVoltdbAhead += tenantToWriteBack
								.getDataSize();
						workloadNeedToOffload += tenantToWriteBack
								.getWorkloadAhead();

						j--;
					}
				}
				if (availableSpaceInVoltdbAhead >= tenantToOffload
						.getDataSize()) {
					synchronized (tenantToOffload) {
						if (!tenantToOffload.isStarted())
							continue;
						if (tenantToOffload.isBeingMovingToMysql()) {
							tenantToOffload.getMovingToMysqlThread().cancel();
							tenantToOffload.cancelMoving();
						} else {
							int tenantToOffload_id = tenantToOffload.getID();
							int voltdbID = getAndAddNewVoltdbIDForTenant(tenantToOffload_id);
							MysqlToVoltdbMoverThread thread = new MysqlToVoltdbMoverThread(
									tenantToOffload, voltdbID, true);
							tenantToOffload.startMovingToVoltdb(thread);
							mMover.addThread(thread);
						}
						workloadNeedToOffload -= tenantToOffload
								.getWorkloadAhead();
						availableSpaceInVoltdbAhead -= tenantToOffload
								.getDataSize();
					}
				} else
					break;
			}
		}
		mMover.updateConcurrencyLimit(getConcurrencyLimit(freeWorkloadInMysqlNow));
		mMover.trigger();
		LOG.debug("Number of running moverthreads:"
				+ mMover.getNumberOfRunningThreads());
	}

	public static void main(String[] args) throws HException,
			TTransportException {
		HServer server = new HServer();
		server.start();
	}

	public void completeOneMoverThread() {
		mMover.completeOne();
	}

	public void trigger() {
		mMover.trigger();
	}

	private int getAndAddNewVoltdbIDForTenant(int tenant_id) {
		int voltdbID = -1;
		int min = -1;
		synchronized (mVoltdbIDList) {
			for (int i = 1; i <= Constants.NUMBER_OF_VOLTDBID; i++) {
				int tmp = mVoltdbIDList.get(i).size();
				if (min == -1 || min > tmp) {
					min = tmp;
					voltdbID = i;
				}
			}
			if (voltdbID != -1) {
				if (mVoltdbIDList.get(voltdbID).contains(tenant_id)) {
					return -1;
				} else {
					mVoltdbIDList.get(voltdbID).add(tenant_id);
				}
			}
			return voltdbID;
		}
	}

	public boolean removeVoltdbIDForTenant(int tenant_id) {
		synchronized (mVoltdbIDList) {
			for (int i = 1; i <= Constants.NUMBER_OF_VOLTDBID; i++) {
				if (mVoltdbIDList.get(i).contains(tenant_id)) {
					int index = mVoltdbIDList.get(i).indexOf(tenant_id);
					mVoltdbIDList.get(i).remove(index);
					return true;
				}
			}
			return false;
		}
	}

	private int getConcurrencyLimit(int freeWorkloadInMysqlNow) {
		// TODO complete this method
		return 1;
	}

	public synchronized void updateWorkloadLimitInMysql() {
		// if (mNowTroughput > mLastThroughput)
		mLastThroughput = mNowTroughput;
		mNowTroughput = 0;
		LOG.info("Troughput now:" + mLastThroughput);
	}

	// TODO complete this method
	private synchronized int getWorkloadLimitInMysql() {
		return 1000000;
	}

	private int getWorkloadNow(HTenant[] tenants) {
		int res = 0;
		for (int i = 0; i < tenants.length; i++)
			res += tenants[i].getWorkloadNow();
		return res;
	}

	private int getWorkloadAhead(HTenant[] tenants) {
		int res = 0;
		for (int i = 0; i < tenants.length; i++)
			res += tenants[i].getWorkloadAhead();
		return res;
	}

	private int getSpace(HTenant[] tenants) {
		int res = 0;
		for (int i = 0; i < tenants.length; i++)
			res += tenants[i].getDataSize();
		return res;
	}

	private HTenant[] findTenantsInMysqlNow() {
		ArrayList<HTenant> list = new ArrayList<>();
		HTenant[] res = null;
		for (Entry<Integer, HTenant> tenantEntry : mTenants.entrySet()) {
			HTenant tenant = tenantEntry.getValue();
			if (!tenant.isStarted())
				continue;
			if (!tenant.isInMysql())
				continue;
			list.add(tenant);
		}
		res = new HTenant[list.size()];
		list.toArray(res);
		return res;
	}

	private HTenant[] findTenantsInMysqlAhead() {
		ArrayList<HTenant> list = new ArrayList<>();
		HTenant[] res = null;
		for (Entry<Integer, HTenant> tenantEntry : mTenants.entrySet()) {
			HTenant tenant = tenantEntry.getValue();
			if (!tenant.isStarted())
				continue;
			if (tenant.isInMysqlAhead())
				list.add(tenant);
		}
		res = new HTenant[list.size()];
		list.toArray(res);
		return res;
	}

	private HTenant[] findTenantsInVoltdbNow() {
		ArrayList<HTenant> list = new ArrayList<>();
		HTenant[] res = null;
		for (Entry<Integer, HTenant> tenantEntry : mTenants.entrySet()) {
			HTenant tenant = tenantEntry.getValue();
			if (!tenant.isStarted())
				continue;
			if (tenant.isInMysql())
				continue;
			list.add(tenant);
		}
		res = new HTenant[list.size()];
		list.toArray(res);
		return res;
	}

	private HTenant[] findTenantsInVoltdbAhead() {
		ArrayList<HTenant> list = new ArrayList<>();
		HTenant[] res = null;
		for (Entry<Integer, HTenant> tenantEntry : mTenants.entrySet()) {
			HTenant tenant = tenantEntry.getValue();
			if (!tenant.isStarted())
				continue;
			if (tenant.isInVoltdbAhead())
				list.add(tenant);
		}
		res = new HTenant[list.size()];
		list.toArray(res);
		return res;
	}

	private void clearVoltdb() throws HException {
		VoltdbConnectionPool pool = new VoltdbConnectionPool();
		Client voltdbConnection = pool.getConnection();
		ClientResponse response;
		VoltTable[] tables;
		VoltTable table;
		String tableName;
		if (voltdbConnection != null) {
			try {
				response = voltdbConnection.callProcedure("@SystemCatalog",
						"TABLES");
				if (response != null
						&& response.getStatus() == ClientResponse.SUCCESS) {
					tables = response.getResults();
					for (int i = 0; i < tables.length; i++) {
						table = tables[i];
						while (table.advanceRow()) {
							tableName = table.getString("TABLE_NAME");
							response = voltdbConnection.callProcedure("@AdHoc",
									"delete from " + tableName + ";");
							if (response.getStatus() != ClientResponse.SUCCESS) {
								throw new HException("Can not delete table "
										+ tableName);
							} else {
								LOG.debug("voltdb table " + tableName
										+ " cleared");
							}
						}
					}
				} else {
					throw new HException("Failed response");
				}
			} catch (IOException | ProcCallException e) {
				throw new HException(e.getMessage());
			}
		} else {
			throw new HException("Can't connect to voltdb server");
		}
		pool.clear();
	}
}
