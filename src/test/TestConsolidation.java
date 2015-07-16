package test;

import newhybrid.ClientShutdownException;
import newhybrid.HeartbeatThread;
import newhybrid.TenantWorkload;

import org.apache.log4j.Logger;

import thrift.TenantResult;
import utillity.WorkloadLoader;
import client.HTenantClient;
import client.ServerClient;
import config.Constants;

public class TestConsolidation {
	final static private Logger LOG = Logger.getLogger(Constants.LOGGER_NAME);

	public static void main(String[] args) {
		String usage = "TestMain firstTenantID lastTenantID inputWorkloadFileName outputResultFileName";
		if (args.length < 4) {
			LOG.error("not enough arguments!");
			LOG.info(usage);
			return;
		}
		WorkloadLoader workloadLoader;
		String outputFileName;
		workloadLoader = new WorkloadLoader(Constants.WORKLOAD_DIR + "/"
				+ args[2]);
		outputFileName = args[3];

		if (!workloadLoader.load()) {
			LOG.error("no workload file!");
			return;
		} else {
			int start = 0;
			int end = 0;
			try {
				start = Integer.valueOf(args[0]);
				end = Integer.valueOf(args[1]);
			} catch (NumberFormatException e) {
				LOG.error("wrong number format!");
				LOG.info(usage);
				return;
			}
			if (start > end) {
				LOG.error("firstTenantID bigger than lastTenantID");
				LOG.info(usage);
				return;
			}
			ClientThread[] clientThreads = new ClientThread[end - start + 1];
			HeartbeatThread[] clientWorkloadAdderThreads = new HeartbeatThread[end
					- start + 1];
			TenantWorkload[] clientWorkloads = new TenantWorkload[end - start
					+ 1];
			boolean[] good = new boolean[end - start + 1];
			for (int i = start; i <= end; i++) {
				if (workloadLoader.getWorkloadForTenant(i) != null) {
					HTenantClient htc = new HTenantClient(i);
					htc.connect();
					clientThreads[i - start] = new ClientThread(htc,
							workloadLoader.getWorkloadForTenant(i).getWH());
					clientWorkloadAdderThreads[i - start] = null;
					clientWorkloads[i - start] = workloadLoader
							.getWorkloadForTenant(i);
					good[i - start] = true;
				} else
					good[i - start] = false;
			}
			LOG.info(String.format("Test from tenant %d to %d starts(login)",
					start, end));
			int totThreads = clientThreads.length;
			int splits = workloadLoader.getNumberOfSplits();
			for (int i = 0; i < totThreads; i++) {
				if (!good[i])
					continue;
				clientThreads[i].start();
			}
			ServerClient serverClient = new ServerClient();
			while (true) {
				try {
					while (!serverClient.tenantAllLoggedIn()) {
						Thread.sleep(1000);
					}
					break;
				} catch (ClientShutdownException e) {
					LOG.warn("server client is shut down, try to get a new one");
					serverClient.shutdown();
					serverClient = new ServerClient();
				} catch (InterruptedException e) {
					LOG.warn("interrupted while waiting for all tenants' logging:"
							+ e.getMessage());
				}
			}
			for (int i = 0; i < totThreads; i++) {
				if (!good[i])
					continue;
				clientThreads[i].startQuery();
			}
			LOG.info(String.format("Test from tenant %d to %d starts(start)",
					start, end));
			int violatedTenants = 0;
			int violatedQueries = 0;
			// TODO
			splits = 21;
			boolean violated = false;
			for (int i = 0; i < splits; i++) {
				violatedTenants = 0;
				violatedQueries = 0;
				for (int j = 0; j < totThreads; j++) {
					if (!good[j])
						continue;
					int tmp = clientThreads[j]
							.updateSplitAndGetViolatedQueries();
					if (tmp > 0) {
						violatedTenants++;
						violatedQueries += tmp;
					}
					if (clientWorkloadAdderThreads[j] != null)
						clientWorkloadAdderThreads[j].shutdown();
					int totCount = clientWorkloads[j]
							.getActualWorkloadAtSplit(i);
					if (totCount > 0) {
						long tickTime = (Constants.SPLIT_TIME - Constants.S * 1)
								/ totCount;
						LOG.debug("In split " + i + " tenant " + (j + start)
								+ " must be add " + totCount
								+ " workloads speed" + (double) tickTime
								/ 1000000000.0);
						clientWorkloadAdderThreads[j] = new HeartbeatThread(
								"client" + (j + start) + "_workloadAdderThread",
								new ClientWorkloadAdderHeartbeatExecutor(
										clientThreads[j], totCount), tickTime);
						clientWorkloadAdderThreads[j].start();
					}
				}
				if (i - 1 >= 0) {
					LOG.info("Split " + i + ":violated tenant number "
							+ violatedTenants);
					if (i > 5) {
						try {
							serverClient.reportSplit(i, violatedTenants,
									violatedQueries);
						} catch (ClientShutdownException e) {
							LOG.error("server client shut down!");
						}
					}
				}
				try {
					Thread.sleep(Constants.SPLIT_TIME / 1000000);
				} catch (InterruptedException e) {
					LOG.error("Interrupted while sleeping in split " + i + ":"
							+ e.getMessage());
					return;
				}
				try {
					if (serverClient.clientNeedToStop()) {
						violated = true;
						break;
					}
				} catch (ClientShutdownException e) {
					LOG.error("server client shut down!");
				}
			}
			violatedTenants = 0;
			violatedQueries = 0;
			for (int i = 0; i < totThreads; i++) {
				if (!good[i])
					continue;
				if (clientWorkloadAdderThreads[i] != null)
					clientWorkloadAdderThreads[i].shutdown();
				int tmp = clientThreads[i].updateSplitAndGetViolatedQueries();
				if (tmp > 0) {
					violatedTenants++;
					violatedQueries += tmp;
				}
				clientThreads[i].finishQuery();
				synchronized (clientThreads[i]) {
					clientThreads[i].notify();
				}
			}
			LOG.info("Split " + splits + ":violated tenant number "
					+ violatedTenants);
			LOG.info(String
					.format("Test from tenant %d to %d ends(stop querying)",
							start, end));
			try {
				for (int i = 0; i < totThreads; i++) {
					if (!good[i])
						continue;
					clientThreads[i].join();
				}
			} catch (InterruptedException e) {
				LOG.error("Interrupted while waiting client threads to die:"
						+ e.getMessage());
				return;
			}
			for (int i = 0; i < totThreads; i++) {
				if (!good[i])
					continue;
				LOG.debug("report result for tenant " + (i + start));
				TenantWorkload workload = workloadLoader.getWorkloadForTenant(i
						+ start);
				TenantResult result = new TenantResult(workload.getID(),
						workload.getSLO(), workload.getDataSize(),
						workload.getWH(), clientThreads[i].getSplitResults(),
						clientThreads[i].getSuccessQueryResults());
				while (true) {
					try {
						serverClient.serverReportResult(result, outputFileName);
						break;
					} catch (ClientShutdownException e) {
						LOG.warn("server client is shut down, try to get a new one");
						serverClient.shutdown();
						serverClient = new ServerClient();
					}
				}
			}
			serverClient.shutdown();
			LOG.info(String.format(
					"Test from tenant %d to %d ends(logged out)", start, end));
			if (violated)
				System.exit(1);
			else
				System.exit(0);
		}
	}
}