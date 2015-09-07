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
struct TenantInfo{
	1: i32 mId; //this is unique across the whole system
	//more information about tenant can be added here
}
enum DType{
	INT=0,
	FLOAT=1,
	VARCHAR=2, //we now fix the varchar length to 20,can be changed if needed
}
struct ColumnInfo{
	1:string mName;
	2:DType mDType;
}
struct TableInfo{
	1:string mName;
	2:list<ColumnInfo> mColumns;
	3:list<i32> mPrimaryKeyPos; // it's 0-base index
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
	5: i32 mVoltdbCapacityMB;
}
struct ServerWorkerInfo{
	1: string mAddress;
	2: i32 mPort;
	3: DbmsInfo mDbmsInfo;
}
exception NoWorkerException{
	1:string message;
}
exception NoTenantException{
	1:string message;
}
service ServerService{
	i32 tenant_createTenant();
	bool tenant_login(1:i32 ID) throws (1:NoTenantException e);
	bool tenant_logout(1:i32 ID) throws (1:NoTenantException e);
	bool tenant_createTable(1:i32 ID, 2:TableInfo tableInfo) throws (1:NoWorkerException eA, 2:NoTenantException eB);
	list<TableInfo> tenant_getTables(1:i32 ID) throws (1:NoTenantException e);
	list<TableInfo> tenant_getTable(1:i32 ID, 2:string tableName) throws (1:NoTenantException e);
	void tenant_dropAllTables(1:i32 ID) throws (1:NoTenantException eA, 2:NoWorkerException eB);
	void tenant_dropTable(1:i32 ID, 2:string tableName) throws (1:NoTenantException eA, 2:NoWorkerException eB);
	bool worker_register(1:ServerWorkerInfo workerInfo);
}
service WorkerService{
	
}
