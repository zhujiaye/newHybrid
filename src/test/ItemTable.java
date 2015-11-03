package test;

import java.util.ArrayList;

import thrift.ColumnInfo;
import thrift.DType;
import thrift.TableInfo;
import thrift.Type;

public class ItemTable {
	static public TableInfo getTableInfo() {
		ArrayList<Integer> primaryKeyPos = new ArrayList<>();
		ArrayList<ColumnInfo> columns = new ArrayList<>();
		ArrayList<Integer> paras = null;
		paras = new ArrayList<>();
		columns.add(new ColumnInfo("i_id", new DType(Type.INT, paras)));
		paras = new ArrayList<>();
		columns.add(new ColumnInfo("i_im_id", new DType(Type.INT, paras)));
		paras = new ArrayList<>();
		paras.add(24);
		columns.add(new ColumnInfo("i_name", new DType(Type.VARCHAR, paras)));
		paras = new ArrayList<>();
		paras.add(5);
		paras.add(2);
		columns.add(new ColumnInfo("i_price", new DType(Type.DECIMAL, paras)));
		paras = new ArrayList<>();
		paras.add(50);
		columns.add(new ColumnInfo("i_data", new DType(Type.VARCHAR, paras)));
		primaryKeyPos.add(0);
		TableInfo tableInfo = new TableInfo("item", columns, primaryKeyPos);
		return tableInfo;
	}
}