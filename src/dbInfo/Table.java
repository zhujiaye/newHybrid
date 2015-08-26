package dbInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import newhybrid.Pair;
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
	/**
	 * convert some key/value pairs to an <i>AND</i> expression
	 * 
	 * @param pairs
	 * @return
	 */
	static public String convertPairsToAND(ArrayList<Pair<String, String>> pairs) {
		StringBuilder res = new StringBuilder();
		for (int i = 0; i < pairs.size(); i++) {
			if (i > 0)
				res.append(" and ");
			res.append(pairs.get(i).first());
			res.append("=");
			res.append("'");
			res.append(pairs.get(i).second());
			res.append("'");
		}
		return res.toString();
	}

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

	/**
	 * randomly generate a <b>where clause</b>
	 * 
	 * @param primary
	 *            whether the where condition comes from primary key
	 * @return <b>a where clause</b>
	 */
	public String generateWhereClause(boolean primary) {
		ArrayList<Pair<String, String>> pairs = new ArrayList<>();
		if (primary) {
			for (int i = 0; i < PRIMARY_KEY_POS.size(); i++) {
				ColumnInfo nowColumn = COLUMNS.get(PRIMARY_KEY_POS.get(i));
				pairs.add(new Pair<String, String>(nowColumn.mName,
						DataType.getDataTypeByValue(nowColumn.mDType.getValue()).getRandomValue()));
			}
		} else {
			Random random = new Random(System.currentTimeMillis());
			ColumnInfo nowColumn = COLUMNS.get(random.nextInt(COLUMNS.size()));
			pairs.add(new Pair<String, String>(nowColumn.mName,
					DataType.getDataTypeByValue(nowColumn.mDType.getValue()).getRandomValue()));
		}
		return convertPairsToAND(pairs);
	}
}
