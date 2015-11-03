package test;

import java.util.ArrayList;

import thrift.ColumnInfo;
import thrift.DType;
import thrift.TableInfo;
import thrift.Type;

public class WarehouseTable {
	static public TableInfo getTableInfo() {
		ArrayList<Integer> primaryKeyPos = new ArrayList<>();
		ArrayList<ColumnInfo> columns = new ArrayList<>();
		ArrayList<Integer> paras = null;
		paras = new ArrayList<>();
		columns.add(new ColumnInfo("w_id", new DType(Type.SMALL_INT, paras)));
		paras = new ArrayList<>();
		paras.add(10);
		columns.add(new ColumnInfo("w_name", new DType(Type.VARCHAR, paras)));
		paras = new ArrayList<>();
		paras.add(20);
		columns.add(new ColumnInfo("w_street_1", new DType(Type.VARCHAR, paras)));
		paras = new ArrayList<>();
		paras.add(20);
		columns.add(new ColumnInfo("w_street_2", new DType(Type.VARCHAR, paras)));
		paras = new ArrayList<>();
		paras.add(20);
		columns.add(new ColumnInfo("w_city", new DType(Type.VARCHAR, paras)));
		paras = new ArrayList<>();
		paras.add(2);
		columns.add(new ColumnInfo("w_state", new DType(Type.VARCHAR, paras)));
		paras = new ArrayList<>();
		paras.add(9);
		columns.add(new ColumnInfo("w_zip", new DType(Type.VARCHAR, paras)));
		paras = new ArrayList<>();
		paras.add(4);
		paras.add(2);
		columns.add(new ColumnInfo("w_tax", new DType(Type.DECIMAL, paras)));
		paras = new ArrayList<>();
		paras.add(12);
		paras.add(2);
		columns.add(new ColumnInfo("w_ytd", new DType(Type.DECIMAL, paras)));
		primaryKeyPos.add(0);
		TableInfo tableInfo = new TableInfo("warehouse", columns, primaryKeyPos);
		return tableInfo;
	}
}