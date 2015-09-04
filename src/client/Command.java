package client;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.log4j.Logger;

import config.ClientConf;
import config.Constants;
import dbInfo.Table;
import thrift.ColumnInfo;
import thrift.DType;
import thrift.NoTenantException;
import thrift.NoWorkerException;
import thrift.TableInfo;

public class Command {
	static private final Logger LOG = Logger.getLogger(Constants.LOGGER_NAME);
	static private ClientConf CONF;
	static private Newhybrid CLIENT;
	static private TenantClient TCLIENT;

	static private void help() {
		System.out.println("help information shows here");
	}

	static public void main(String[] args) throws NoWorkerException, NoTenantException {
		CONF = ClientConf.getConf();
		CLIENT = new Newhybrid(CONF.SERVER_ADDRESS, CONF.SERVER_PORT);
		if (!CLIENT.serverExist()) {
			LOG.error("server not start!");
			return;
		}
		Scanner in = new Scanner(System.in);
		while (true) {
			System.out.format("newhybrid(%s:%d) > ", CONF.SERVER_ADDRESS, CONF.SERVER_PORT);
			String command = in.nextLine();
			if (command.startsWith("quit") || command.startsWith("exit")) {
				return;
			}
			if (command.startsWith("help")) {
				help();
			}
			if (command.startsWith("create tenant")) {
				System.out.println(CLIENT.createTenant());
			}
			if (command.startsWith("create table")) {
				TCLIENT = new TenantClient(1, CONF.SERVER_ADDRESS, CONF.SERVER_PORT);
				List<ColumnInfo> columns = new ArrayList<>();
				List<Integer> primary_key_pos = new ArrayList<>();
				columns.add(new ColumnInfo("id", DType.INT));
				columns.add(new ColumnInfo("value1", DType.INT));
				columns.add(new ColumnInfo("value2", DType.FLOAT));
				columns.add(new ColumnInfo("value3", DType.VARCHAR));
				primary_key_pos.add(0);
				TableInfo tableInfo = new TableInfo("stock", columns, primary_key_pos);
				System.out.println(TCLIENT.createTable(tableInfo));
			}
		}
	}
}
