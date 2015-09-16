package client;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import config.ClientConf;
import config.Constants;
import dbInfo.HResult;
import dbInfo.HSQLException;
import dbInfo.Table;
import thrift.ColumnInfo;
import thrift.DType;
import thrift.DbStatusInfo;
import thrift.DbmsException;
import thrift.LockException;
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
			String str = in.nextLine();
			String command;
			StringTokenizer tokenizer = new StringTokenizer(str);
			try {
				if (tokenizer.hasMoreTokens()) {
					command = tokenizer.nextToken();
					if (command.equals("quit") || command.equals("exit")) {
						return;
					} else if (command.equals("help")) {
						help();
					} else if (command.equals("register")) {
						System.out.println(CLIENT.createTenant());
					} else if (command.equals("login")) {
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
					} else if (command.equals("logout")) {
						if (TCLIENT == null) {
							System.out.println("no tenant logged in!");
						} else {
							try {
								TCLIENT.lock_lock();
								System.out.println(TCLIENT.logout());
								TCLIENT.lock_release();
							} catch (NoTenantException | LockException e) {
								LOG.error(e.getMessage());
							} finally {
								TCLIENT = null;
							}
						}
					} else if (command.equals("createTable")) {
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
								TCLIENT.lock_lock();
								System.out.println(TCLIENT.createTable(tableInfo));
								TCLIENT.lock_release();
							} catch (NoWorkerException | NoTenantException | LockException e) {
								LOG.error(e.getMessage());
							}
						}
					} else if (command.equals("showTables")) {
						if (TCLIENT == null) {
							System.out.println("no tenant logged in!");
						} else {
							try {
								TCLIENT.lock_lock();
								List<TableInfo> tables = TCLIENT.getTables();
								TCLIENT.lock_release();
								for (int i = 0; i < tables.size(); i++) {
									TableInfo current = tables.get(i);
									System.out.println(current.mName);
								}
							} catch (NoTenantException | LockException e) {
								LOG.error(e.getMessage());
							}
						}
					} else if (command.equals("showTable")) {
						if (TCLIENT == null) {
							System.out.println("no tenant logged in!");
						} else {
							try {
								String tableName = tokenizer.nextToken();
								List<TableInfo> tables;
								TCLIENT.lock_lock();
								tables = TCLIENT.getTable(tableName);
								for (int i = 0; i < tables.size(); i++) {
									TableInfo current = tables.get(i);
									System.out.println(current.mName);
								}
								TCLIENT.lock_release();
							} catch (NoTenantException | LockException e) {
								LOG.error(e.getMessage());
							}
						}
					} else if (command.equals("dropAllTables")) {
						if (TCLIENT == null) {
							System.out.println("no tenant logged in!");
						} else {
							try {
								TCLIENT.lock_lock();
								TCLIENT.dropAllTables();
								TCLIENT.lock_release();
								System.out.println("success");
							} catch (NoTenantException | NoWorkerException | LockException e) {
								LOG.error(e.getMessage());
							}
						}
					} else if (command.equals("dropTable")) {
						if (TCLIENT == null) {
							System.out.println("no tenant logged in!");
						} else {
							try {
								String tableName = tokenizer.nextToken();
								TCLIENT.lock_lock();
								TCLIENT.dropTable(tableName);
								TCLIENT.lock_release();
								System.out.println("success");
							} catch (NoTenantException | NoWorkerException | LockException e) {
								LOG.error(e.getMessage());
							}
						}
					} else if (command.equals("select") || command.equals("insert") || command.equals("update")
							|| command.equals("delete")) {
						if (TCLIENT == null) {
							System.out.println("no tenant logged in!");
						} else {
							try {
								TCLIENT.lock_lock();
								HResult result = TCLIENT.executeSql(str);
								TCLIENT.lock_release();
								if (result != null) {
									if (result.isSuccess()) {
										result.print(System.out);
									} else {
										System.out.println("unsuccess:" + result.getMessage());
									}
									result.release();
								}
							} catch (NoWorkerException | NoTenantException | DbmsException e) {
								LOG.error(e.getMessage());
							} catch (HSQLException e) {
								LOG.error(e.getMessage());
							} catch (LockException e) {
								LOG.error(e.getMessage());
							}
						}
					} else if (command.equals("status")) {
						if (TCLIENT == null) {
							System.out.println("no tenant logged in");
						} else {
							System.out.println("Tenant ID:" + TCLIENT.getID());
							try {
								TCLIENT.lock_lock();
								DbStatusInfo dbInfo = TCLIENT.getDbStatusInfo();
								TCLIENT.lock_release();
								System.out.println("DB status:" + dbInfo.mDbStatus.name());
								System.out.println("DBMS status:" + dbInfo.mDbmsInfo.mCompleteConnectionString);
							} catch (NoWorkerException | NoTenantException | LockException e) {
								LOG.error(e.getMessage());
							}
						}
					} else {
						System.out.println("please input correct commands!");
						help();
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
