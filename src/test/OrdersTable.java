package test;

import java.util.ArrayList;

import thrift.ColumnInfo;
import thrift.DType;
import thrift.TableInfo;
import thrift.Type;

public class OrdersTable {
	static public TableInfo getTableInfo() {
		ArrayList<Integer> primaryKeyPos = new ArrayList<>();
		ArrayList<ColumnInfo> columns = new ArrayList<>();
		ArrayList<Integer> paras = null;
		paras = new ArrayList<>();
		columns.add(new ColumnInfo("o_id", new DType(Type.INT, paras)));
		paras = new ArrayList<>();
		columns.add(new ColumnInfo("o_d_id", new DType(Type.TINY_INT, paras)));
		paras = new ArrayList<>();
		columns.add(new ColumnInfo("o_w_id", new DType(Type.SMALL_INT, paras)));
		paras = new ArrayList<>();
		columns.add(new ColumnInfo("o_c_id", new DType(Type.INT, paras)));
		paras = new ArrayList<>();
		columns.add(new ColumnInfo("o_entry_d",
				new DType(Type.TIMESTAMP, paras)));
		paras = new ArrayList<>();
		columns.add(new ColumnInfo("o_carrier_id", new DType(Type.TINY_INT,
				paras)));
		paras = new ArrayList<>();
		columns.add(new ColumnInfo("o_ol_cnt", new DType(Type.TINY_INT, paras)));
		paras = new ArrayList<>();
		columns.add(new ColumnInfo("o_all_local", new DType(Type.TINY_INT,
				paras)));
		primaryKeyPos.add(0);
		primaryKeyPos.add(1);
		primaryKeyPos.add(2);
		TableInfo tableInfo = new TableInfo("orders", columns, primaryKeyPos);
		return tableInfo;
	}
}