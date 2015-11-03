package test;

import java.util.ArrayList;

import thrift.ColumnInfo;
import thrift.DType;
import thrift.TableInfo;
import thrift.Type;

public class NewOrdersTable {
	static public TableInfo getTableInfo() {
		ArrayList<Integer> primaryKeyPos = new ArrayList<>();
		ArrayList<ColumnInfo> columns = new ArrayList<>();
		ArrayList<Integer> paras = null;
		paras = new ArrayList<>();
		columns.add(new ColumnInfo("no_o_id", new DType(Type.INT, paras)));
		paras = new ArrayList<>();
		columns.add(new ColumnInfo("no_d_id", new DType(Type.TINY_INT, paras)));
		paras = new ArrayList<>();
		columns.add(new ColumnInfo("no_w_id", new DType(Type.SMALL_INT, paras)));
		primaryKeyPos.add(0);
		primaryKeyPos.add(1);
		// primaryKeyPos.add(2);
		TableInfo tableInfo = new TableInfo("new_orders", columns,
				primaryKeyPos);
		return tableInfo;
	}
}