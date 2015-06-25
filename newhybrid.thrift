namespace java thrift

service ServerService{
	i32 tenant_getIDInVoltdb(1: i32 id);
	i32 tenant_getDataSize(1: i32 id);
	i32 tenant_getDataSizeKind(1: i32 id);
	bool tenant_isUseMysql(1: i32 id);
	bool tenant_login(1: i32 id);
	bool tenant_logout(1: i32 id);
	bool tenant_start(1: i32 id);
	bool tenant_stop(1: i32 id);
	bool tenant_isLoggedIn(1: i32 id);
	bool tenant_isStarted(1: i32 id);
	bool tenant_completeOneQuery(1: i32 id);
	bool tenant_isAllLoggedIn();
	bool server_reloadWorkloadFile(1: string workloadFileName);
	void server_stop();
}