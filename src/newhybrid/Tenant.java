package newhybrid;

import thrift.TenantInfo;

public class Tenant {
	private final int TENANT_ID;

	public Tenant(TenantInfo tenantInfo) {
		TENANT_ID = tenantInfo.mId;
	}

	public int getID() {
		return TENANT_ID;
	}
}
