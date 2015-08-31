package server;

import java.util.ArrayList;

import dbInfo.Table;
import thrift.DbmsInfo;
import thrift.TableInfo;
import thrift.TenantInfo;

public class ServerTenant {
	private final int ID;

	private ArrayList<Table> mTables;
	private DbmsInfo mDbmsInfo;

	public ServerTenant(TenantInfo tenantInfo, ArrayList<TableInfo> tablesInfo, DbmsInfo dbmsInfo) {
		ID = tenantInfo.mId;
		mTables = new ArrayList<>();
		for (int i = 0; i < tablesInfo.size(); i++) {
			TableInfo currentTableInfo = tablesInfo.get(i);
			mTables.add(new Table(ID, currentTableInfo));
		}
		mDbmsInfo = dbmsInfo;
	}

	public TenantInfo generateTenantInfo() {
		TenantInfo tenantInfo = new TenantInfo(ID);
		return tenantInfo;
	}

	public ArrayList<TableInfo> generateTablesInfo() {
		ArrayList<TableInfo> res = new ArrayList<>();
		for (int i = 0; i < mTables.size(); i++) {
			Table currentTable = mTables.get(i);
			res.add(currentTable.generateTableInfo());
		}
		return res;
	}
}
