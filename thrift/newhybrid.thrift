namespace java thrift

struct SplitResult{
 	1: i32 mSplitID;
	2: i32 mRemainQueriesBefore;
	3: i32 mWorkload;
	4: i32 mRemainQueriesAfter;
	5: i32 mSentQueries;
	6: i32 mSuccessQueries;
}
struct SuccessQueryResult{
	1: i32 mID;
	2: bool mIsInMysql;
	3: bool mIsRead;
	4: i64 	mStartTime;
	5: i64  mEndTime;
	6: i64  mLatency;
	7: i32 mSplit;
}
struct TenantResult{
	1: i32 mID;
	2: i32 mSLO;
	3: i32 mDataSize;
	4: i32 mWH;
	5: list<SplitResult> mSplitResults;
	6: list<SuccessQueryResult> mQueryResults;
}
enum DbmsType{
	MYSQL=0,
	VOLTDB=1,
}
struct DbmsInfo{
	1: DbmsType mType;
	2: string mCompleteConnectionString;
	3: string mMysqlUsername;
	4: string mMysqlPassword;
	5: string mVoltdbCapacityMB;
}
struct ServerWorkerInfo{
	1: string mAddress;
	2: i32 mPort;
	3: DbmsInfo mDbmsInfo;
}
service ServerService{
	bool worker_register(1: ServerWorkerInfo workerInfo);
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
	void server_reportResult(1: TenantResult tenantResult, 2: string outputFileName);
	void server_reconfigure(1:bool isMysqlOnly, 2: i32 voltdbCapacity);
	void test_reportSplit(1: i32 splitID, 2: i32 splitViolatedTenants 3: i32 splitViolatedQueries);
	bool test_clientNeedToStop(); 
}
service WorkerService{
	
}
