package dbInfo;


public class Data {
	private final DataType TYPE;
	private final String VALUE;

	public Data(DataType type, String value) {
		TYPE = type;
		VALUE = value;
	}
	public String getValue(){
		return VALUE;
	}
	public DataType getType(){
		return TYPE;
	}
}
