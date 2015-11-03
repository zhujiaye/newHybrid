package test;

import java.util.ArrayList;

import thrift.ColumnInfo;
import thrift.DType;
import thrift.TableInfo;
import thrift.Type;

public class OrderLineTable {
	static public TableInfo getTableInfo() {
		ArrayList<Integer> primaryKeyPos = new ArrayList<>();
		ArrayList<ColumnInfo> columns = new ArrayList<>();
		ArrayList<Integer> paras = null;
		paras = new ArrayList<>();
		columns.add(new ColumnInfo("ol_o_id", new DType(Type.INT, paras)));
		paras = new ArrayList<>();
		columns.add(new ColumnInfo("ol_d_id", new DType(Type.TINY_INT, paras)));
		paras = new ArrayList<>();
		columns.add(new ColumnInfo("ol_w_id", new DType(Type.SMALL_INT, paras)));
		paras = new ArrayList<>();
		columns.add(new ColumnInfo("ol_number", new DType(Type.TINY_INT, paras)));
		paras = new ArrayList<>();
		columns.add(new ColumnInfo("ol_i_id", new DType(Type.INT, paras)));
		paras = new ArrayList<>();
		columns.add(new ColumnInfo("ol_supply_w_id", new DType(Type.SMALL_INT,
				paras)));
		paras = new ArrayList<>();
		columns.add(new ColumnInfo("ol_delivery_d", new DType(Type.TIMESTAMP,
				paras)));
		paras = new ArrayList<>();
		columns.add(new ColumnInfo("ol_quantity", new DType(Type.TINY_INT,
				paras)));
		paras = new ArrayList<>();
		paras.add(6);
		paras.add(2);
		columns.add(new ColumnInfo("ol_amount", new DType(Type.DECIMAL, paras)));
		paras = new ArrayList<>();
		paras.add(24);
		columns.add(new ColumnInfo("ol_dist_info", new DType(Type.VARCHAR,
				paras)));
		primaryKeyPos.add(0);
		primaryKeyPos.add(1);
		primaryKeyPos.add(2);
		primaryKeyPos.add(3);
		TableInfo tableInfo = new TableInfo("order_line", columns,
				primaryKeyPos);
		return tableInfo;
	}
}
