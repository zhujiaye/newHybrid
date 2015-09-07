package client;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.StringTokenizer;

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

	static public void main(String[] args) {
		CONF = ClientConf.getConf();
		CLIENT = new Newhybrid(CONF.SERVER_ADDRESS, CONF.SERVER_PORT);
		TCLIENT = null;
		if (!CLIENT.serverExist()) {
			LOG.error("server not start!");
			return;
		}
		Scanner in = new Scanner(System.in);
		while (true) {
			if (TCLIENT != null)
				System.out.format("tenant" + TCLIENT.getID() + "@");
			System.out.format("newhybrid(%s:%d) > ", CONF.SERVER_ADDRESS, CONF.SERVER_PORT);
			String command = in.nextLine();
			StringTokenizer tokenizer = new StringTokenizer(command);
			try {
				if (tokenizer.hasMoreTokens()) {
					command = tokenizer.nextToken();
					if (command.startsWith("quit") || command.startsWith("exit")) {
						return;
					}
					if (command.startsWith("help")) {
						help();
					}
					if (command.startsWith("register")) {
						System.out.println(CLIENT.createTenant());
					}
					if (command.startsWith("login")) {
						int ID = Integer.parseInt(tokenizer.nextToken());
						if (TCLIENT != null) {
							System.out.println("already logged in as tenant with ID=" + TCLIENT.getID());
						} else {
							TCLIENT = new TenantClient(ID, CONF.SERVER_ADDRESS, CONF.SERVER_PORT);
							try {
								System.out.println(TCLIENT.login());
							} catch (NoTenantException e) {
								LOG.error(e.getMessage());
								TCLIENT = null;
							}
						}
					}
					if (command.startsWith("logout")) {
						if (TCLIENT == null) {
							System.out.println("no tenant logged in!");
						} else {
							try {
								System.out.println(TCLIENT.logout());
							} catch (NoTenantException e) {
								LOG.error(e.getMessage());
							} finally {
								TCLIENT = null;
							}
						}
					}
					if (command.startsWith("createTable")) {
						if (TCLIENT == null) {
							System.out.println("no tenant logged in!");
						} else {
							String tableName = tokenizer.nextToken();
							List<ColumnInfo> columns = new ArrayList<>();
							List<Integer> primary_key_pos = new ArrayList<>();
							columns.add(new ColumnInfo("id", DType.INT));
							columns.add(new ColumnInfo("value1", DType.INT));
							columns.add(new ColumnInfo("value2", DType.FLOAT));
							columns.add(new ColumnInfo("value3", DType.VARCHAR));
							primary_key_pos.add(0);
							TableInfo tableInfo = new TableInfo(tableName, columns, primary_key_pos);
							try {
								System.out.println(TCLIENT.createTable(tableInfo));
							} catch (NoWorkerException | NoTenantException e) {
								LOG.error(e.getMessage());
							}
						}
					}
				} else {
					help();
				}
			} catch (NoSuchElementException e) {
				System.out.println("wrong command syntax");
				help();
			}
		}
	}
}
