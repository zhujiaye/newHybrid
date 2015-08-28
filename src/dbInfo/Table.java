package dbInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;

import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import newhybrid.Pair;
import newhybrid.Tenant;
import thrift.ColumnInfo;
import thrift.DType;
import thrift.TableInfo;

/**
 * a high-level table class
 * 
 * @author zhujiaye
 *
 */
public class Table {
	/**
	 * convert some key/value pairs to an expression which is like '
	 * <i>key1=value1 delimiter key2=value2....</i>'
	 * 
	 * @param pairs
	 *            key/value pairs
	 * @param delimiter
	 *            the delimiter used in the expression
	 * @return result string
	 */
	static public String convertPairs(ArrayList<Pair<String, Data>> pairs, String delimiter) {
		StringBuilder res = new StringBuilder();
		for (int i = 0; i < pairs.size(); i++) {
			Pair<String, Data> pair = pairs.get(i);
			if (i > 0)
				res.append(" " + delimiter + " ");
			res.append(pair.first());
			res.append("=");
			if (pair.second().getType().isStringKind())
				res.append("'");
			res.append(pairs.get(i).second().getValue());
			if (pair.second().getType().isStringKind())
				res.append("'");
		}
		return res.toString();
	}

	/**
	 * convert a list of values to a value expression like '
	 * <i>(value1,value2,...)</i>'
	 * 
	 * @param values
	 * @return
	 */
	static public String convertValues(ArrayList<Data> values) {
		StringBuilder res = new StringBuilder();
		res.append("(");
		for (int i = 0; i < values.size(); i++) {
			Data value = values.get(i);
			if (i > 0)
				res.append(",");
			if (value.getType().isStringKind())
				res.append("'");
			res.append(value.getValue());
			if (value.getType().isStringKind())
				res.append("'");
		}
		res.append(")");
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
	public ArrayList<Data> generateOneRow() {
		ArrayList<Data> res = new ArrayList<>();
		for (int i = 0; i < COLUMNS.size(); i++) {
			ColumnInfo nowColumn = COLUMNS.get(i);
			DataType dataType = DataType.getDataTypeByValue(nowColumn.mDType.getValue());
			String value = dataType.getRandomValue();
			res.add(new Data(dataType, value));
		}
		return res;
	}

	/**
	 * generate key/value pairs on the corresponding columns
	 * 
	 * @param pos
	 *            the positions of columns which will be generate key/value pair
	 *            from
	 * @return key/value pairs
	 */
	private ArrayList<Pair<String, Data>> generatePairs(List<Integer> pos) {
		ArrayList<Pair<String, Data>> pairs = new ArrayList<>();
		for (int i = 0; i < pos.size(); i++) {
			ColumnInfo nowColumn = COLUMNS.get(pos.get(i));
			DataType dataType = DataType.getDataTypeByValue(nowColumn.mDType.getValue());
			String value = dataType.getRandomValue();
			pairs.add(new Pair<String, Data>(nowColumn.mName, new Data(dataType, value)));
		}
		return pairs;
	}

	/**
	 * randomly generate a <b>where clause</b>. It's like
	 * "key1=value1 and key2=value2 and ..."
	 * 
	 * @param primary
	 *            whether the where condition comes from primary key
	 * @return <b>a where clause</b>
	 */
	public String generateWhereClause(boolean primary) {
		if (primary) {
			return convertPairs(generatePairs(PRIMARY_KEY_POS), "and");
		} else {
			Random random = new Random(System.nanoTime());
			List<Integer> pos = new ArrayList<>();
			pos.add(random.nextInt(COLUMNS.size()));
			return convertPairs(generatePairs(pos), "and");
		}
	}

	/**
	 * randomly generate a <b>set clause</b>. It's like
	 * "key1=value1,key2=value2,...". Notice that here the primary key is not
	 * included
	 * 
	 * @return
	 */
	public String generateSetClause() {
		List<Integer> pos = new ArrayList<>();
		for (int i = 0; i < COLUMNS.size(); i++) {
			if (PRIMARY_KEY_POS.contains(i))
				continue;
			pos.add(i);
		}
		return convertPairs(generatePairs(pos), ",");
	}

	/**
	 * get all columns' definition
	 * 
	 * @return a string list in which every string stands for a column
	 *         definition
	 */
	public ArrayList<String> getColumnDefinition() {
		ArrayList<String> res = new ArrayList<>();
		for (int i = 0; i < COLUMNS.size(); i++) {
			StringBuilder stringBuilder = new StringBuilder();
			ColumnInfo nowColumn = COLUMNS.get(i);
			stringBuilder.append(nowColumn.mName);
			stringBuilder.append(" ");
			DataType dataType = DataType.getDataTypeByValue(nowColumn.mDType.getValue());
			stringBuilder.append(dataType.getDataTypeString());
			stringBuilder.append(" not null");
			res.add(stringBuilder.toString());
		}
		return res;
	}

	/**
	 * get all constraint definition
	 * 
	 * @return a string list in which every string stands for a constraint
	 *         definition
	 */
	public ArrayList<String> getConstraintDefinition() {
		ArrayList<String> res = new ArrayList<>();
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("primary key(");
		for (int i = 0; i < PRIMARY_KEY_POS.size(); i++) {
			String key_name = COLUMNS.get(PRIMARY_KEY_POS.get(i)).mName;
			if (i > 0)
				stringBuilder.append(",");
			stringBuilder.append(key_name);
		}
		stringBuilder.append(")");
		res.add(stringBuilder.toString());
		return res;
	}
}
