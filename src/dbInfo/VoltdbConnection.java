package dbInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.voltdb.VoltTable;
import org.voltdb.client.Client;
import org.voltdb.client.ClientConfig;
import org.voltdb.client.ClientFactory;
import org.voltdb.client.ClientResponse;
import org.voltdb.client.NoConnectionsException;
import org.voltdb.client.ProcCallException;

import config.Constants;
import newhybrid.NoHConnectionException;
import thrift.DbmsInfo;

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
	static public HConnection getConnection(DbmsInfo dbmsInfo) throws NoHConnectionException {
		int cnt = 0;
		Client newConnection = null;
		ClientConfig config = new ClientConfig();
		config.setConnectionResponseTimeout(0);
		config.setProcedureCallTimeout(0);
		newConnection = ClientFactory.createClient(config);
		try {
			while (cnt++ < MAX_RETRY && newConnection.getConnectedHostList().isEmpty()) {
				newConnection.createConnection(dbmsInfo.mCompleteConnectionString);
				if (!newConnection.getConnectedHostList().isEmpty())
					break;
			}
			if (newConnection == null || newConnection.getConnectedHostList().isEmpty()) {
				throw new NoHConnectionException(dbmsInfo,
						"tried " + MAX_RETRY + " times but can not establish connection!");
			}
			HConnection res = new VoltdbConnection(dbmsInfo, newConnection);
			return res;
		} catch (IOException e) {
			throw new NoHConnectionException(dbmsInfo, "java network or connection problem");
		}
	}

	private Client mVoltdbConnection;

	public VoltdbConnection(DbmsInfo dbmsInfo, Client voltdbConnection) {
		super(dbmsInfo);
		mVoltdbConnection = voltdbConnection;
	}

	private String getRealTableName(Table table) {
		return table.getName() + "_" + table.getTenantID();
	}

	private String getRealTableName(String name, int tenantID) {
		return name + "_" + tenantID;
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
	public void dropAll() throws HSQLException {
		ArrayList<String> allNames = getAllTableNames();
		for (int i = 0; i < allNames.size(); i++) {
			String name = allNames.get(i);
			HResult result = _sql("drop table " + name + " if exists cascade");
			if (!result.isSuccess())
				throw new HSQLException("failed to drop table " + name + ":" + result.getMessage());
		}
	}

	@Override
	public void dropTable(Table table) throws HSQLException {
		String realTableName = getRealTableName(table);
		HResult result = _sql("drop table " + realTableName + " if exists cascade");
		if (!result.isSuccess())
			throw new HSQLException("failed to drop table " + realTableName + ":" + result.getMessage());
	}

	@Override
	public boolean createTable(Table table) throws HSQLException {
		if (tableExist(table))
			return false;
		String realTableName = getRealTableName(table);
		StringBuffer createString = new StringBuffer("create table ");
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
			throw new HSQLException("failed to partition table " + realTableName + ":" + result.getMessage());
		return true;
	}

	@Override
	public ArrayList<String> getAllTableNames() throws HSQLException {
		ArrayList<String> res = new ArrayList<>();
		try {
			ClientResponse response = mVoltdbConnection.callProcedure("@SystemCatalog", "TABLES");
			if (response.getStatus() == ClientResponse.SUCCESS) {
				VoltTable tables = response.getResults()[0];
				while (tables.advanceRow()) {
					String name = tables.getString("TABLE_NAME");
					res.add(name);
				}
			} else {
				throw new HSQLException("voltdb unsuccess:" + response.getStatusString());
			}
		} catch (IOException | ProcCallException e) {
			throw new HSQLException("voltdb exception:" + e.getMessage());
		}
		return res;
	}

	@Override
	public HResult doRandomSelect(Table table) {
		String realTableName = getRealTableName(table);
		return _sql("select * from " + realTableName + " where " + table.generateWhereClause(true));
	}

	@Override
	public HResult doRandomUpdate(Table table) {
		String realTableName = getRealTableName(table);
		return _sql("update " + realTableName + " set " + table.generateSetClause() + " where "
				+ table.generateWhereClause(true));
	}

	@Override
	public HResult doRandomInsert(Table table) {
		String realTableName = getRealTableName(table);
		String valueString = Table.convertValues(table.generateOneRow());
		return _sql("upsert into " + realTableName + " values " + valueString);
	}

	@Override
	public HResult doRandomDelete(Table table) {
		String realTableName = getRealTableName(table);
		return _sql("delete from " + realTableName + " where " + table.generateWhereClause(true));
	}

	@Override
	public boolean tableExist(Table table) throws HSQLException {
		String realTableName = getRealTableName(table);
		ArrayList<String> allNames = getAllTableNames();
		return allNames.contains(realTableName.toLowerCase()) || allNames.contains(realTableName.toUpperCase());
	}

	private HResult _sql(String sqlString) {
		//System.out.println(sqlString);
		ClientResponse response = null;
		try {
			response = mVoltdbConnection.callProcedure("@AdHoc", sqlString);
			if (response.getStatus() == ClientResponse.SUCCESS) {
				VoltTable[] results = response.getResults();
				if (results.length == 0) {
					LOG.warn("VoltTable length equals zero");
					return new VoltdbResult(QueryType.UNKNOWN, true, "success", -1);
				} else {
					VoltTable result = results[0];
					if (result.getColumnName(0).equals("modified_tuples")) {
						result.advanceRow();
						return new VoltdbResult(QueryType.WRITE, true, "success", (int) result.getLong(0));
					} else
						return new VoltdbResult(QueryType.READ, true, "success", result);
				}
			} else {
				return new VoltdbResult(QueryType.FAILED, false, response.getStatusString(), -1);
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
			if (pre != null && pre.equals("from") && (type == QueryType.SELECT || type == QueryType.DELETE)) {
				current = getRealTableName(current, tenantID);
			}
			if (pre != null && pre.equals("update") && cnt == 2 && type == QueryType.UPDATE) {
				current = getRealTableName(current, tenantID);
			}
			if (pre != null && pre.equals("into") && cnt == 3 && type == QueryType.INSERT) {
				current = getRealTableName(current, tenantID);
			}
			builder.append(current + " ");
		}
		return _sql(builder.toString());
	}

}
