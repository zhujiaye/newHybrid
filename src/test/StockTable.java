package test;

import java.util.ArrayList;

import thrift.ColumnInfo;
import thrift.DType;
import thrift.TableInfo;
import thrift.Type;

public class StockTable {
	static public TableInfo getTableInfo() {
		ArrayList<Integer> primaryKeyPos = new ArrayList<>();
		ArrayList<ColumnInfo> columns = new ArrayList<>();
		ArrayList<Integer> paras = null;
		paras = new ArrayList<>();
		columns.add(new ColumnInfo("s_i_id", new DType(Type.INT, paras)));
		paras = new ArrayList<>();
		columns.add(new ColumnInfo("s_w_id", new DType(Type.SMALL_INT, paras)));
		paras = new ArrayList<>();
		columns.add(new ColumnInfo("s_quantity", new DType(Type.SMALL_INT,
				paras)));
		paras = new ArrayList<>();
		paras.add(24);
		columns.add(new ColumnInfo("s_dist_01", new DType(Type.VARCHAR, paras)));
		paras = new ArrayList<>();
		paras.add(24);
		columns.add(new ColumnInfo("s_dist_02", new DType(Type.VARCHAR, paras)));
		paras = new ArrayList<>();
		paras.add(24);
		columns.add(new ColumnInfo("s_dist_03", new DType(Type.VARCHAR, paras)));
		paras = new ArrayList<>();
		paras.add(24);
		columns.add(new ColumnInfo("s_dist_04", new DType(Type.VARCHAR, paras)));
		paras = new ArrayList<>();
		paras.add(24);
		columns.add(new ColumnInfo("s_dist_05", new DType(Type.VARCHAR, paras)));
		paras = new ArrayList<>();
		paras.add(24);
		columns.add(new ColumnInfo("s_dist_06", new DType(Type.VARCHAR, paras)));
		paras = new ArrayList<>();
		paras.add(24);
		columns.add(new ColumnInfo("s_dist_07", new DType(Type.VARCHAR, paras)));
		paras = new ArrayList<>();
		paras.add(24);
		columns.add(new ColumnInfo("s_dist_08", new DType(Type.VARCHAR, paras)));
		paras = new ArrayList<>();
		paras.add(24);
		columns.add(new ColumnInfo("s_dist_09", new DType(Type.VARCHAR, paras)));
		paras = new ArrayList<>();
		paras.add(24);
		columns.add(new ColumnInfo("s_dist_10", new DType(Type.VARCHAR, paras)));
		paras = new ArrayList<>();
		paras.add(8);
		paras.add(0);
		columns.add(new ColumnInfo("s_ytd", new DType(Type.DECIMAL, paras)));
		paras = new ArrayList<>();
		columns.add(new ColumnInfo("s_order_cnt", new DType(Type.SMALL_INT,
				paras)));
		paras = new ArrayList<>();
		columns.add(new ColumnInfo("s_remote_cnt", new DType(Type.SMALL_INT,
				paras)));
		paras = new ArrayList<>();
		paras.add(50);
		columns.add(new ColumnInfo("s_data", new DType(Type.VARCHAR, paras)));
		primaryKeyPos.add(0);
		primaryKeyPos.add(1);
		TableInfo tableInfo = new TableInfo("stock", columns, primaryKeyPos);
		return tableInfo;
	}
}
