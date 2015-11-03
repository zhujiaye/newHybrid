package test;

import java.util.ArrayList;

import thrift.ColumnInfo;
import thrift.DType;
import thrift.TableInfo;
import thrift.Type;

public class CustomerTable {
	static public TableInfo getTableInfo() {
		ArrayList<Integer> primaryKeyPos = new ArrayList<>();
		ArrayList<ColumnInfo> columns = new ArrayList<>();
		columns.add(new ColumnInfo("c_id", new DType(Type.INT,
				new ArrayList<Integer>())));
		columns.add(new ColumnInfo("c_d_id", new DType(Type.TINY_INT,
				new ArrayList<Integer>())));
		columns.add(new ColumnInfo("c_w_id", new DType(Type.SMALL_INT,
				new ArrayList<Integer>())));

		ArrayList<Integer> paras = null;
		paras = new ArrayList<>();
		paras.add(16);
		columns.add(new ColumnInfo("c_first", new DType(Type.VARCHAR, paras)));
		paras = new ArrayList<>();
		paras.add(2);
		columns.add(new ColumnInfo("c_middle", new DType(Type.VARCHAR, paras)));
		paras = new ArrayList<>();
		paras.add(16);
		columns.add(new ColumnInfo("c_last", new DType(Type.VARCHAR, paras)));
		paras = new ArrayList<>();
		paras.add(20);
		columns.add(new ColumnInfo("c_street_1", new DType(Type.VARCHAR, paras)));
		paras = new ArrayList<>();
		paras.add(20);
		columns.add(new ColumnInfo("c_street_2", new DType(Type.VARCHAR, paras)));
		paras = new ArrayList<>();
		paras.add(20);
		columns.add(new ColumnInfo("c_city", new DType(Type.VARCHAR, paras)));
		paras = new ArrayList<>();
		paras.add(2);
		columns.add(new ColumnInfo("c_state", new DType(Type.VARCHAR, paras)));
		paras = new ArrayList<>();
		paras.add(9);
		columns.add(new ColumnInfo("c_zip", new DType(Type.VARCHAR, paras)));
		paras = new ArrayList<>();
		paras.add(16);
		columns.add(new ColumnInfo("c_phone", new DType(Type.VARCHAR, paras)));
		paras = new ArrayList<>();
		columns.add(new ColumnInfo("c_since", new DType(Type.TIMESTAMP, paras)));
		paras = new ArrayList<>();
		paras.add(2);
		columns.add(new ColumnInfo("c_credit", new DType(Type.VARCHAR, paras)));
		paras = new ArrayList<>();
		columns.add(new ColumnInfo("c_credit_lim", new DType(Type.BIG_INT,
				paras)));
		paras = new ArrayList<>();
		paras.add(4);
		paras.add(2);
		columns.add(new ColumnInfo("c_discount", new DType(Type.DECIMAL, paras)));
		paras = new ArrayList<>();
		paras.add(12);
		paras.add(2);
		columns.add(new ColumnInfo("c_balance", new DType(Type.DECIMAL, paras)));
		paras = new ArrayList<>();
		paras.add(12);
		paras.add(2);
		columns.add(new ColumnInfo("c_ytd_payment", new DType(Type.DECIMAL,
				paras)));
		paras = new ArrayList<>();
		columns.add(new ColumnInfo("c_payment_cnt", new DType(Type.SMALL_INT,
				paras)));
		paras = new ArrayList<>();
		columns.add(new ColumnInfo("c_delivery_cnt", new DType(Type.SMALL_INT,
				paras)));
		paras = new ArrayList<>();
		paras.add(500);
		columns.add(new ColumnInfo("c_data", new DType(Type.VARCHAR, paras)));
		primaryKeyPos.add(0);
		primaryKeyPos.add(1);
		primaryKeyPos.add(2);
		TableInfo tableInfo = new TableInfo("customer", columns, primaryKeyPos);
		return tableInfo;
	}
}