package test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import newhybrid.NoServerConnectionException;

import org.apache.log4j.Logger;

import thrift.NoTenantException;
import client.TenantClient;
import config.ClientConf;
import config.Constants;

public class TestSLO {
	static private final Logger LOG = Logger.getLogger(Constants.LOGGER_NAME);

	private static void printUsage() {
		System.out
				.println("Usage:TestSLO ID1 ID2 SLO result_file\nStart to simulate tenants from ID1 to ID2 with SLO");
	}

	public static void main(String[] args) {
		if (args.length != 4) {
			printUsage();
			return;
		}
		int id1, id2, slo;
		String file_name = null;
		try {
			id1 = Integer.valueOf(args[0]);
			id2 = Integer.valueOf(args[1]);
			slo = Integer.valueOf(args[2]);
			file_name = args[3];
		} catch (NumberFormatException e) {
			System.out.println("Please pass three integer parameters!");
			return;
		}
		if (id1 > id2) {
			System.out.println("ID1 must equal or less than ID2");
			return;
		}
		if (slo <= 0) {
			System.out.println("SLO must greater than zero");
			return;
		}
		File file = new File(file_name);
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(file);
			int totTenants = id2 - id1 + 1;
			TenantThread[] tenantThreads = new TenantThread[totTenants];
			ClientConf conf = ClientConf.getConf();
			for (int i = id1; i <= id2; i++) {
				tenantThreads[i - id1] = new TenantThread(i,
						conf.SERVER_ADDRESS, conf.SERVER_PORT, slo);
				TenantClient client = new TenantClient(i, conf.SERVER_ADDRESS,
						conf.SERVER_PORT);
				client.login();
			}
			for (int i = 0; i < totTenants; i++)
				tenantThreads[i].start();
			int splits = 10;
			for (int i = 0; i < splits + 1; i++) {
				LOG.info("collect last split information......");
				int vqLastSplit = 0, vtLastSplit = 0;
				int totLastSplit = 0, sentLastSplit = 0, successLastSplit = 0;
				for (int j = 0; j < totTenants; j++) {
					SplitResult result = tenantThreads[j]
							.updateSplitAndGetSplitResult(i < splits ? slo : 0);
					if (result != null) {
						int vq = result.getSent() - result.getSuccess();
						totLastSplit += result.getTot();
						sentLastSplit += result.getSent();
						successLastSplit += result.getSuccess();
						vqLastSplit += vq;
						if (vq > 0)
							vtLastSplit++;
					}
				}
				LOG.info(String
						.format("split %d: %d tot,%d sent,%d success,%d violated,%d tenants violated",
								i, totLastSplit, sentLastSplit,
								successLastSplit, vqLastSplit, vtLastSplit));
				if (i == splits)
					break;
				Thread.sleep(Constants.SPLIT_TIME / 1000000);
			}
			LOG.info("waiting for tenants to be shutdown......");
			for (int j = 0; j < totTenants; j++) {
				tenantThreads[j].shutdown();
			}
			for (int j = 0; j < totTenants; j++) {
				tenantThreads[j].join();
			}
			LOG.info("collect statistics......");
			int tot = 0, success = 0;
			for (int j = 0; j < totTenants; j++) {
				if (tenantThreads[j].isSuccess()) {
					tot += tenantThreads[j].getTotSentQuery();
					success += tenantThreads[j].getTotSuccessQuery();
				} else {
					writer.format("ERROR:some tenant failed%n");
					writer.flush();
					return;
				}
			}
			writer.format("%d%n%d%n", tot, success);
		} catch (InterruptedException | FileNotFoundException
				| NoTenantException | NoServerConnectionException e) {
			writer.format("ERROR:%s%n", e.getMessage());
			return;
		} finally {
			if (writer != null) {
				writer.flush();
				writer.close();
			}
		}
	}
}
