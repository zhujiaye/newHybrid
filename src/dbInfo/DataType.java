package dbInfo;

import java.util.ArrayList;
import java.util.Random;

import thrift.DType;
import thrift.Type;
import utility.ValueGenerator;

/**
 * this class must be the same as the DType class in package thrift,except this
 * class has its own method
 * 
 * @author zhujiaye
 *
 */
public class DataType {
	private final String mDataTypeString;
	private final DType mDType;

	static public DataType getDataTypeByDType(DType dType) {
		return new DataType(dType);
	}

	public DataType(DType dType) {
		mDType = dType;
		switch (mDType.type) {
		case TINY_INT:
			mDataTypeString = "tinyint";
			break;
		case SMALL_INT:
			mDataTypeString = "smallint";
			break;
		case INT:
			mDataTypeString = "integer";
			break;
		case BIG_INT:
			mDataTypeString = "bigint";
			break;
		case DECIMAL:
			if (mDType.paras.size() > 0) {
				StringBuilder builder = new StringBuilder();
				builder.append("decimal(");
				for (int i = 0; i < mDType.paras.size(); i++) {
					if (i > 0)
						builder.append(",");
					builder.append(mDType.paras.get(i));
				}
				builder.append(")");
				mDataTypeString = builder.toString();
			} else {
				mDataTypeString = "decimal";
			}
			break;
		case FLOAT:
			mDataTypeString = "float";
			break;
		case VARCHAR:
			mDataTypeString = "varchar(" + mDType.paras.get(0) + ")";
			break;
		case TIMESTAMP:
			mDataTypeString = "timestamp";
			break;
		default:
			mDataTypeString = "unknown";
			break;
		}
	}

	public String getDataTypeString() {
		return mDataTypeString;
	}

	public boolean isStringKind() {
		return mDType.type == Type.TIMESTAMP || mDType.type == Type.VARCHAR;
	}

	public String generateRandomValue() {
		Random random = new Random(System.nanoTime());
		int op = (random.nextInt() % 2 == 0 ? -1 : 1);
		switch (mDType.type) {
		case TINY_INT:
			return String.valueOf(op * random.nextInt(128));
		case SMALL_INT:
			return String.valueOf(op * random.nextInt(32768));
		case INT:
			return String.valueOf(op * random.nextInt());
		case BIG_INT:
			return String.valueOf(op * random.nextLong());
		case DECIMAL:
			return String.valueOf(op * random.nextFloat() * 1000);
		case FLOAT:
			return String.valueOf(op * random.nextFloat() * 1000);
		case VARCHAR:
			return ValueGenerator.MakeAlphaString(1, mDType.paras.get(0));
		case TIMESTAMP:
			return String.valueOf(ValueGenerator.getTimeStamp());
		default:
			return null;
		}
	}

	/**
	 * just for test
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("tinyint:"
				+ new DataType(new DType(Type.TINY_INT,
						new ArrayList<Integer>())).generateRandomValue());
		System.out.println("smallint:"
				+ new DataType(new DType(Type.SMALL_INT,
						new ArrayList<Integer>())).generateRandomValue());
		System.out.println("int:"
				+ new DataType(new DType(Type.INT, new ArrayList<Integer>()))
						.generateRandomValue());
		System.out.println("bigint:"
				+ new DataType(
						new DType(Type.BIG_INT, new ArrayList<Integer>()))
						.generateRandomValue());
		System.out.println("decimal:"
				+ new DataType(
						new DType(Type.DECIMAL, new ArrayList<Integer>()))
						.generateRandomValue());
		System.out.println("float:"
				+ new DataType(new DType(Type.FLOAT, new ArrayList<Integer>()))
						.generateRandomValue());
		ArrayList<Integer> varcharParas = new ArrayList<>();
		varcharParas.add(10);
		System.out.println("varchar(10):"
				+ new DataType(new DType(Type.VARCHAR, varcharParas))
						.generateRandomValue());
		varcharParas.clear();
		varcharParas.add(50);
		System.out.println("varchar(50):"
				+ new DataType(new DType(Type.VARCHAR, varcharParas))
						.generateRandomValue());
		System.out.println("timestamp:"
				+ new DataType(new DType(Type.TIMESTAMP,
						new ArrayList<Integer>())).generateRandomValue());
		ArrayList<Integer> decimalParas = new ArrayList<>();
		decimalParas.add(12);
		System.out.println(new DataType(new DType(Type.DECIMAL, decimalParas))
				.getDataTypeString());
		decimalParas.add(8);
		System.out.println(new DataType(new DType(Type.DECIMAL, decimalParas))
				.getDataTypeString());
	}
}
