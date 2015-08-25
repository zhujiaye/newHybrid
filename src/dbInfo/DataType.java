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
	INT(0), FLOAT(1), VARCHAR(2);
	private final int mValue;

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

	private DataType(int value) {
		mValue = value;
	}

	public int getValue() {
		return mValue;
	}

	public String getRandomValue() {
		Random random = new Random(System.currentTimeMillis());
		switch (mValue) {
		case 0:
			return String.valueOf(random.nextInt());
		case 1:
			return String.valueOf(random.nextFloat() * 1e10);
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
