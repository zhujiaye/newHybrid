package dbInfo;

import java.util.ArrayList;
import java.util.List;

import newhybrid.Tenant;
import thrift.ColumnInfo;
import thrift.TableInfo;

/**
 * a high-level table class
 * 
 * @author zhujiaye
 *
 */
public class Table {
	protected final Tenant TENANT;
	protected final String NAME;
	protected final List<ColumnInfo> COLUMNS;
	protected final List<Integer> PRIMARY_KEY_POS;

	protected Table(Tenant tenant, TableInfo tableInfo) {
		TENANT = tenant;
		NAME = tableInfo.mName;
		COLUMNS = tableInfo.mColumns;
		PRIMARY_KEY_POS = tableInfo.mPrimaryKeyPos;
	}

	public Tenant getTenant() {
		return TENANT;
	}

	public String getName() {
		return NAME;
	}

	public List<ColumnInfo> getColumns() {
		return COLUMNS;
	}

	public List<Integer> getPrimaryKeyPos() {
		return PRIMARY_KEY_POS;
	}

	/**
	 * randomly generate a row for this table
	 * 
	 * @return a string list contain every column value of the generated row
	 */
	public ArrayList<String> generateOneRow() {
		ArrayList<String> res = new ArrayList<>();
		for (int i = 0; i < COLUMNS.size(); i++) {
			String value = DataType.getDataTypeByValue(COLUMNS.get(i).mDType.getValue()).getRandomValue();
			res.add(value);
		}
		return res;
	}
}
