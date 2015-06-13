package server;

public class VoltdbToMysqlMoverThread implements Runnable{

	private HTenant mTenant;

	public VoltdbToMysqlMoverThread(HTenant tenant) {
		mTenant = tenant;
		mTenant.startMovingToMysql();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

	
}
