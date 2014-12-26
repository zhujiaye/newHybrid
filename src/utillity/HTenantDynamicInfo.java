package utillity;

public class HTenantDynamicInfo {
	private int interval;
	private boolean usingVoltDB;
	private int useVoltDBID;
	private boolean isActive;
	private boolean isBurst;

	public HTenantDynamicInfo() {
	}

	public HTenantDynamicInfo(int interval, boolean usingVoltDB,
			int useVoltDBID, boolean isActive, boolean isBurst) {
		this.interval = interval;
		this.usingVoltDB = usingVoltDB;
		this.useVoltDBID = useVoltDBID;
		this.isActive = isActive;
		this.isBurst = isBurst;

	}

	public String encode() {
		String res = "";
		res = res.concat(String.valueOf(interval) + "@");
		res = res.concat(String.valueOf(usingVoltDB) + "@");
		res = res.concat(String.valueOf(useVoltDBID) + "@");
		res = res.concat(String.valueOf(isActive) + "@");
		res = res.concat(String.valueOf(isBurst));
		return res;
	}

	public void decode(String str) {
		String[] strs = str.split("@");
		interval = Integer.valueOf(strs[0]);
		usingVoltDB = Boolean.valueOf(strs[1]);
		useVoltDBID = Integer.valueOf(strs[2]);
		isActive = Boolean.valueOf(strs[3]);
		isBurst = Boolean.valueOf(strs[4]);
	}

	public int getInterval() {
		return interval;
	}

	public boolean getUsingVoltDB() {
		return usingVoltDB;
	}

	public int getUseVoltDBID() {
		return useVoltDBID;
	}

	public boolean getIsActive() {
		return isActive;
	}

	public boolean getIsBurst() {
		return isBurst;
	}

}
