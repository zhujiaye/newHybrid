package server;

public class MysqlToVoltdbMoverThread implements Runnable {
	private HTenant mTenant;
	private int mVoltdbID;

	public MysqlToVoltdbMoverThread(HTenant tenant, int voltdbID) {
		mTenant = tenant;
		mVoltdbID = voltdbID;
		tenant.startMovingToVoltdb();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

}
