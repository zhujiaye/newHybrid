package utility;

import java.nio.ByteBuffer;

public class Utils {
	/**
	 * format a string to fixed length, if old string's length is less than
	 * <b>length</b> then remain space will fill with char <b>c</b>, also you
	 * can define the string's alignment
	 * 
	 * @param old
	 *            the string need to be formatted
	 * @param length
	 *            formatted string's length
	 * @param c
	 *            use to fill the remain free space
	 * @param alignRight
	 *            <b>true</b> if the old string need to be aligned to right,
	 *            <b>false</b> to left
	 * @return formatted string
	 */
	static public String FormatStringToFixedLength(String old, int length, char c, boolean alignRight) {
		if (old.length() > length)
			return old;
		int remainLen = length - old.length();
		StringBuilder builder = new StringBuilder();
		if (!alignRight)
			builder.append(old);
		for (int i = 0; i < remainLen; i++)
			builder.append(c);
		if (alignRight)
			builder.append(old);
		return builder.toString();
	}

	/**
	 * generate corrent bytebuffer from thrift results
	 * 
	 * @param data
	 *            the thrift bytebuffer result
	 * @return the correct byte buffer
	 */
	static public ByteBuffer generateNewByteBufferFromThriftRPCResults(ByteBuffer data) {
		ByteBuffer correctData = ByteBuffer.allocate(data.limit() - data.position());
		correctData.put(data);
		correctData.flip();
		return correctData;
	}

	static public void main(String[] args) {
		System.out.println(FormatStringToFixedLength("xx", 10, 'c', false));
	}
}
