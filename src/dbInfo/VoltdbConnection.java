package dbInfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.voltdb.VoltTable;
import org.voltdb.VoltType;
import org.voltdb.client.Client;
import org.voltdb.client.ClientConfig;
import org.voltdb.client.ClientFactory;
import org.voltdb.client.ClientResponse;
import org.voltdb.client.NoConnectionsException;
import org.voltdb.client.ProcCallException;
import org.voltdb.client.ProcedureCallback;

import config.Constants;
import newhybrid.NoHConnectionException;
import thrift.DbmsException;
import thrift.DbmsInfo;
import thrift.TableInfo;
import thrift.TempTableInfo;

public class VoltdbConnection extends HConnection {
	static private final Logger LOG = Logger.getLogger(Constants.LOGGER_NAME);
	static private final int MAX_RETRY = 5;

	/**
	 * get a voltdb HConnection by dbms information
	 * 
	 * @param dbmsInfo
	 * @return voltdb HConnection,not null
	 * @throws NoHConnectionException
	 */
	static public HConnection getConnection(DbmsInfo dbmsInfo)
			throws NoHConnectionException {
		int cnt = 0;
		Client newConnection = null;
		ClientConfig config = new ClientConfig();
		config.setConnectionResponseTimeout(0);
		config.setProcedureCallTimeout(0);
		newConnection = ClientFactory.createClient(config);
		try {
			while (cnt++ < MAX_RETRY
					&& newConnection.getConnectedHostList().isEmpty()) {
				newConnection
						.createConnection(dbmsInfo.mCompleteConnectionString);
				if (!newConnection.getConnectedHostList().isEmpty())
					break;
			}
			if (newConnection == null
					|| newConnection.getConnectedHostList().isEmpty()) {
				throw new NoHConnectionException(dbmsInfo, "tried " + MAX_RETRY
						+ " times but can not establish connection!");
			}
			HConnection res = new VoltdbConnection(dbmsInfo, newConnection);
			return res;
		} catch (IOException e) {
			throw new NoHConnectionException(dbmsInfo,
					"java network or connection problem");
		}
	}

	private Client mVoltdbConnection;

	public VoltdbConnection(DbmsInfo dbmsInfo, Client voltdbConnection) {
		super(dbmsInfo);
		mVoltdbConnection = voltdbConnection;
	}

	private String getRealTableName(int tenantID, TableInfo tableInfo) {
		return tableInfo.mName + tenantID;
	}

	private String getRealTableName(String tableName, int tenantID) {
		return tableName + tenantID;
	}

	@Override
	public boolean isUseful() {
		if (mVoltdbConnection == null)
			return false;
		return !mVoltdbConnection.getConnectedHostList().isEmpty();
	}

	@Override
	public void release() {
		if (mVoltdbConnection == null)
			return;
		try {
			mVoltdbConnection.drain();
			mVoltdbConnection.close();
		} catch (NoConnectionsException e) {
		} catch (InterruptedException e) {
			LOG.error("voltdb connection is releasing while being interrupted!");
		} finally {
			mVoltdbConnection = null;
		}
	}

	@Override
	public void dropTable(int tenantID, TableInfo tableInfo)
			throws HSQLException {
		String realTableName = getRealTableName(tenantID, tableInfo);
		HResult result = _sql("drop table " + realTableName
				+ " if exists cascade");
		if (!result.isSuccess())
			throw new HSQLException("failed to drop table " + realTableName
					+ ":" + result.getMessage());
	}

	@Override
	public boolean createTable(int tenantID, TableInfo tableInfo)
			throws HSQLException {
		if (tableExist(tenantID, tableInfo))
			return false;
		String realTableName = getRealTableName(tenantID, tableInfo);
		StringBuffer createString = new StringBuffer("create table ");
		Table table = new Table(tableInfo);
		ArrayList<String> definitions;
		createString.append(realTableName);
		createString.append("(");
		definitions = table.getColumnDefinition();
		for (int i = 0; i < definitions.size(); i++) {
			if (i > 0)
				createString.append(",");
			createString.append(definitions.get(i));
		}
		definitions = table.getConstraintDefinition();
		for (int i = 0; i < definitions.size(); i++) {
			createString.append(",");
			createString.append(definitions.get(i));
		}
		createString.append(")");
		HResult result = _sql(createString.toString());
		if (!result.isSuccess())
			throw new HSQLException(result.getMessage());
		result = _sql("partition table " + realTableName + " on column "
				+ table.getColumns().get(table.getPrimaryKeyPos().get(0)).mName);
		if (!result.isSuccess())
			throw new HSQLException("failed to partition table "
					+ realTableName + ":" + result.getMessage());
		return true;
	}

	private ArrayList<String> getAllTableNames() throws HSQLException {
		ArrayList<String> res = new ArrayList<>();
		try {
			ClientResponse response = mVoltdbConnection.callProcedure(
					"@SystemCatalog", "TABLES");
			if (response.getStatus() == ClientResponse.SUCCESS) {
				VoltTable tables = response.getResults()[0];
				while (tables.advanceRow()) {
					String name = tables.getString("TABLE_NAME");
					res.add(name);
				}
			} else {
				throw new HSQLException("voltdb unsuccess:"
						+ response.getStatusString());
			}
		} catch (IOException | ProcCallException e) {
			throw new HSQLException("voltdb exception:" + e.getMessage());
		}
		return res;
	}

	@Override
	public boolean tableExist(int tenantID, TableInfo tableInfo)
			throws HSQLException {
		String realTableName = getRealTableName(tenantID, tableInfo);
		ArrayList<String> allNames = getAllTableNames();
		return allNames.contains(realTableName.toLowerCase())
				|| allNames.contains(realTableName.toUpperCase());
	}

	private HResult _sql(String sqlString) {
		// System.out.println(sqlString);
		ClientResponse response = null;
		try {
			response = mVoltdbConnection.callProcedure("@AdHoc", sqlString);
			if (response.getStatus() == ClientResponse.SUCCESS) {
				VoltTable[] results = response.getResults();
				if (results.length == 0) {
					LOG.warn("VoltTable length equals zero");
					return new VoltdbResult(QueryType.UNKNOWN, true, "success",
							-1);
				} else {
					VoltTable result = results[0];
					if (result.getColumnName(0).equals("modified_tuples")) {
						result.advanceRow();
						return new VoltdbResult(QueryType.WRITE, true,
								"success", (int) result.getLong(0));
					} else
						return new VoltdbResult(QueryType.READ, true,
								"success", result);
				}
			} else {
				return new VoltdbResult(QueryType.FAILED, false,
						response.getStatusString(), -1);
			}
		} catch (IOException | ProcCallException e) {
			return new VoltdbResult(QueryType.FAILED, false, e.getMessage(), -1);
		}
	}

	@Override
	public HResult executeSql(int tenantID, String sqlString) {
		StringTokenizer tokenizer = new StringTokenizer(sqlString);
		StringBuilder builder = new StringBuilder();
		String current = null, pre = null;
		QueryType type = null;
		int cnt = 0;
		for (; tokenizer.hasMoreTokens();) {
			pre = current;
			current = tokenizer.nextToken();
			cnt++;
			if (cnt == 1) {
				type = QueryType.getByString(current);
				if (type == QueryType.INSERT)
					current = "upsert";
			}
			if (pre != null && pre.equals("from")
					&& (type == QueryType.SELECT || type == QueryType.DELETE)) {
				current = getRealTableName(current, tenantID);
			}
			if (pre != null && pre.equals("update") && cnt == 2
					&& type == QueryType.UPDATE) {
				current = getRealTableName(current, tenantID);
			}
			if (pre != null && pre.equals("into") && cnt == 3
					&& type == QueryType.INSERT) {
				current = getRealTableName(current, tenantID);
			}
			builder.append(current + " ");
		}
		return _sql(builder.toString());
	}

	@Override
	public void exportTempTable(int tenantID, TableInfo tableInfo,
			String tempPath) throws DbmsException {
		String realTableName = getRealTableName(tableInfo.mName, tenantID);
		HResult result = _sql("select * from " + realTableName);
		if (result.isSuccess()) {
			FileOutputStream out = null;
			try {
				File file = new File(tempPath);
				if (!file.exists()) {
					file.createNewFile();
				}
				out = new FileOutputStream(file);
				while (result.hasNext()) {
					ArrayList<String> list = result.getColumnValues();
					for (int i = 0; i < list.size(); i++) {
						if (i > 0)
							out.write(",".getBytes());
						out.write(("\"" + list.get(i) + "\"").getBytes());
					}
					out.write("\n".getBytes());
				}
			} catch (FileNotFoundException e) {
				throw new DbmsException(e.getMessage());
			} catch (IOException e) {
				throw new DbmsException(e.getMessage());
			} catch (HSQLException e) {
				throw new DbmsException(e.getMessage());
			} finally {
				try {
					if (out != null)
						out.close();
				} catch (IOException e) {
					throw new DbmsException(e.getMessage());
				}
			}
		} else
			throw new DbmsException(result.getMessage());
	}

	@Override
	public void importTempTable(int tenantID, TableInfo tableInfo,
			String tempPath) throws DbmsException {
		String realTableName = getRealTableName(tableInfo.mName, tenantID);
		InetSocketAddress host = mVoltdbConnection.getConnectedHostList()
				.get(0);
		try {
			dropTable(tenantID, tableInfo);
			createTable(tenantID, tableInfo);
		} catch (HSQLException e) {
			throw new DbmsException(e.getMessage());
		}
		try {
			// long t1, t2;
			// t1 = System.nanoTime();
			BufferedReader reader = new BufferedReader(new FileReader(tempPath));
			ArrayList<String> lines = new ArrayList<>();
			String line;
			while ((line = reader.readLine()) != null) {
				lines.add(line);
			}
			int totLines = lines.size();
			Object lock = new Object();
			reader.close();
			CountProcedureCallback procedureCallback = new CountProcedureCallback(
					totLines, lock);
			for (int i = 0; i < lines.size(); i++) {
				line = lines.get(i);
				String[] values = line.split(",");
				for (int j = 0; j < values.length; j++) {
					int len = values[j].length();
					values[j] = values[j].substring(1, len - 1);
				}
				mVoltdbConnection.callProcedure(procedureCallback,
						realTableName + ".upsert", (Object[]) values);
			}
			synchronized (lock) {
				while (procedureCallback.getCount() < totLines) {
					try {
						lock.wait();
					} catch (InterruptedException e) {
						LOG.error(e.getMessage());
					}
				}
			}
			// t2 = System.nanoTime();
			// System.out.println("one row one time:" + (t2 - t1) /
			// 1000000000.0);
		} catch (FileNotFoundException e) {
			throw new DbmsException(e.getMessage());
		} catch (IOException e) {
			throw new DbmsException(e.getMessage());
		}
	}

	static private class CountProcedureCallback implements ProcedureCallback {
		private final int COUNT;
		private final Object LOCK;
		private int mCount = 0;

		public CountProcedureCallback(int count, Object lock) {
			COUNT = count;
			LOCK = lock;
		}

		@Override
		public void clientCallback(ClientResponse clientResponse)
				throws Exception {
			synchronized (LOCK) {
				mCount++;
				if (mCount >= COUNT) {
					LOCK.notify();
				}
			}
		}

		public int getCount() {
			synchronized (LOCK) {
				return mCount;
			}
		}
	}
}
