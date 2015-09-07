package server;

import java.util.ArrayList;

import dbInfo.Table;
import thrift.DbmsInfo;
import thrift.TableInfo;
import thrift.TenantInfo;

public class ServerTenant {
	private final int ID;

	private ArrayList<TableInfo> mTables;
	private DbmsInfo mDbmsInfo;
	private boolean mLoggedIn = false;

	public ServerTenant(TenantInfo tenantInfo, ArrayList<TableInfo> tablesInfo, DbmsInfo dbmsInfo) {
		ID = tenantInfo.mId;
		mTables = tablesInfo;
		mDbmsInfo = dbmsInfo;
	}

	public void addTable(TableInfo tableInfo) {
		mTables.add(tableInfo);
	}

	public void dropTable(String tableName) {
		int drop_index = -1;
		for (int i = 0; i < mTables.size(); i++) {
			if (mTables.get(i).mName.equals(tableName)) {
				drop_index = i;
				break;
			}
		}
		if (drop_index == -1)
			return;
		mTables.remove(drop_index);
	}

	public TenantInfo generateTenantInfo() {
		TenantInfo tenantInfo = new TenantInfo(ID);
		return tenantInfo;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<TableInfo> generateTablesInfo() {
		return (ArrayList<TableInfo>) mTables.clone();
	}

	public int getID() {
		return ID;
	}

	public DbmsInfo getDbmsInfo() {
		return mDbmsInfo;
	}

	public void setDbms(DbmsInfo dbmsInfo) {
		mDbmsInfo = dbmsInfo;
	}

	public boolean login() {
		if (mLoggedIn)
			return false;
		mLoggedIn = true;
		return true;
	}

	public boolean logout() {
		if (!mLoggedIn)
			return false;
		mLoggedIn = false;
		return true;
	}
}
