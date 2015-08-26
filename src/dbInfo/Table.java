package dbInfo;

import java.util.ArrayList;
import java.util.List;

import thrift.ColumnInfo;
import thrift.TableInfo;

/**
 * a high-level table class
 * 
 * @author zhujiaye
 *
 */
public class Table {
	protected final String mName;
	protected final List<ColumnInfo> mColumns;
	protected final List<Integer> mPrimaryKeyPos;

	protected Table(TableInfo tableInfo) {
		mName = tableInfo.mName;
		mColumns = tableInfo.mColumns;
		mPrimaryKeyPos = tableInfo.mPrimaryKeyPos;
	}

	/**
	 * randomly generate a row for this table
	 * 
	 * @return a string list contain every column value of the generated row
	 */
	public ArrayList<String> generateOneRow() {
		ArrayList<String> res = new ArrayList<>();
		for (int i = 0; i < mColumns.size(); i++) {
			String value = DataType.getDataTypeByValue(mColumns.get(i).mDType.getValue()).getRandomValue();
			res.add(value);
		}
		return res;
	}
}
