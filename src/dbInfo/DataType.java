package dbInfo;

import java.util.Random;

import utillity.ValueGenerator;

/**
 * this class must be the same as the DType class in package thrift,except this
 * class has its own method
 * 
 * @author zhujiaye
 *
 */
public enum DataType {
	INT(0, "integer"), FLOAT(1, "float"), VARCHAR(2, "varchar(20)");
	private final int mValue;
	private final String mDataTypeString;

	static public DataType getDataTypeByValue(int value) {
		switch (value) {
		case 0:
			return INT;
		case 1:
			return FLOAT;
		case 2:
			return VARCHAR;
		default:
			return null;
		}
	}

	private DataType(int value, String dataTypeString) {
		mValue = value;
		mDataTypeString = dataTypeString;
	}

	public int getValue() {
		return mValue;
	}

	public String getDataTypeString() {
		return mDataTypeString;
	}

	public String getRandomValue() {
		Random random = new Random(System.nanoTime());
		switch (mValue) {
		case 0:
			return String.valueOf(random.nextInt(100));
		case 1:
			return String.valueOf(random.nextFloat() * 100.0);
		case 2:
			return ValueGenerator.MakeAlphaString(1, 20);
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
		System.out.println(DataType.INT.getRandomValue());
		System.out.println(DataType.FLOAT.getRandomValue());
		System.out.println(DataType.VARCHAR.getRandomValue());
	}
}
