namespace java thrift

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
enum DbStatus{
	NORMAL=0,
	MIGRATING=1,
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
struct DbStatusInfo{
	1:DbStatus mDbStatus;
	2:DbmsInfo mDbmsInfo;
}
struct TempTableInfo{
	1:TableInfo mTableInfo;
	2:string mTablePath;
}
struct TempDbInfo{
	1:list<TempTableInfo> mTempTablesInfo;
}
enum OperationType{
	TABLE_CREATE=1,
	TABLE_DROP=2,
	EXECUTE_SQL=3,
}
struct TableOperationPara{
	1:i32 mTenantID;
	2:TableInfo mTableInfo;
}
struct SqlOperationPara{
	1:i32 mTenantID;
	2:string mSqlString;
}
union OperationPara{
	1:TableOperationPara mTableOpPara;
	2:SqlOperationPara mSqlOpPara;
}
struct Operation{
	1:OperationType mType;
	2:OperationPara mParas;
}
exception DbmsException{
	1:string message;
}
exception NoWorkerException{
	1:string message;
}
exception NoTenantException{
	1:string message;
}
exception LockException{
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
	DbStatusInfo tenant_getDbStatusInfo(1:i32 ID) throws (1:NoTenantException eA, 2:NoWorkerException eB);
	bool worker_register(1:ServerWorkerInfo workerInfo);
	void tenant_lock_lock(1:i32 ID) throws (1:LockException eA, 2:NoTenantException eB);
	void tenant_lock_release(1:i32 ID);
	void addOperationToMigrator(1:i32 ID, 2:Operation operation);
}
service WorkerService{
	void tenant_exportTempDb(1:i32 ID, 2:TempDbInfo tempDbInfo) throws (1:DbmsException e); 
	void tenant_moveTempDb(1:i32 ID, 2:TempDbInfo tempDbInfo, 3:ServerWorkerInfo workerInfo) throws (1:DbmsException e); 
	void replay(1:list<Operation> operations) throws (1:DbmsException e);
}
