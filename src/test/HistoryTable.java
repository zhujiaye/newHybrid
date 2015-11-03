package test;

import java.util.ArrayList;

import thrift.ColumnInfo;
import thrift.DType;
import thrift.TableInfo;
import thrift.Type;

public class HistoryTable {
	static public TableInfo getTableInfo() {
		ArrayList<Integer> primaryKeyPos = new ArrayList<>();
		ArrayList<ColumnInfo> columns = new ArrayList<>();
		ArrayList<Integer> paras = null;
		paras = new ArrayList<>();
		columns.add(new ColumnInfo("h_c_id", new DType(Type.INT, paras)));
		paras = new ArrayList<>();
		columns.add(new ColumnInfo("h_c_d_id", new DType(Type.TINY_INT, paras)));
		paras = new ArrayList<>();
		columns.add(new ColumnInfo("h_c_w_id", new DType(Type.SMALL_INT, paras)));
		paras = new ArrayList<>();
		columns.add(new ColumnInfo("h_d_id", new DType(Type.TINY_INT, paras)));
		paras = new ArrayList<>();
		columns.add(new ColumnInfo("h_w_id", new DType(Type.SMALL_INT, paras)));
		paras = new ArrayList<>();
		columns.add(new ColumnInfo("h_date", new DType(Type.TIMESTAMP, paras)));
		paras = new ArrayList<>();
		paras.add(6);
		paras.add(2);
		columns.add(new ColumnInfo("h_amount", new DType(Type.INT, paras)));
		paras = new ArrayList<>();
		paras.add(24);
		columns.add(new ColumnInfo("h_data", new DType(Type.VARCHAR, paras)));
		primaryKeyPos.add(0);
		primaryKeyPos.add(1);
		primaryKeyPos.add(2);
		TableInfo tableInfo = new TableInfo("history", columns, primaryKeyPos);
		return tableInfo;
	}
}
