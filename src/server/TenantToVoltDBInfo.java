package server;

public class TenantToVoltDBInfo {
	private int tenantid;
	private int voltDB_id;
	private Sender sender;

	public TenantToVoltDBInfo(int tenantid, int voltDB_id, Sender sender) {
		this.tenantid = tenantid;
		this.voltDB_id = voltDB_id;
		this.sender = sender;
	}

	public int getTenantID() {
		return tenantid;
	}

	public int getVoltDB_id() {
		return voltDB_id;
	}

	public Sender getSender() {
		return sender;
	}

	public void setSender(Sender sender) {
		this.sender = sender;
	}
}
