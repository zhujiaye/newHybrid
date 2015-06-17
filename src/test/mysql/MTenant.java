package test.mysql;

import java.util.Random;

import utillity.MysqlConnectionPool;
import utillity.VoltdbConnectionPool;
import newhybrid.HException;
import newhybrid.HQueryResult;
import newhybrid.HSQLTimeOutException;
import client.HTenantClient;

public class MTenant extends Thread {
	public static MysqlConnectionPool mPool;
	public static VoltdbConnectionPool vPool;
	public static void resetPool() throws HException{
		mPool = new MysqlConnectionPool();
		vPool = new VoltdbConnectionPool();
	}
	
	private int id;
	protected final double writePercent = 0.2;
	protected int queryThisInterval = 0;
	protected int readThisInterval = 0;
	protected int writeThisInterval = 0;
	public synchronized int queryNumber(int n){
		if(n < 0){
			queryThisInterval = 0;
		}else{
			queryThisInterval += n;
		}
		return queryThisInterval;
	}
	public synchronized int readNumber(int  n){
		if(n < 0){
			readThisInterval = 0;
		}else{
			readThisInterval += n;
		}
		return readThisInterval;
	}
	public synchronized int writeNumber(int n){
		if(n < 0){
			writeThisInterval = 0;
		}else{
			writeThisInterval += n;
		}
		return writeThisInterval;
	}
	
	public MTenant(){}
	public MTenant(int id){
		this.id = id;
	}
	
	public void init() throws HException{
		htc = new HTenantClient(id);
		htc.login();
		htc.start();
	}
	public void init_pool() throws HException{
		htc = new HTenantClient(id, mPool, vPool);
		htc.login();
		htc.start();
	}
	public void clean() throws HException{
		htc.stop();
		htc.logout();
		htc.shutdown();
	}
	
	public HTenantClient htc;
	public void run(){
		try {
			Random rand = new Random(System.nanoTime());
			HQueryResult result;
			boolean isWrite;
			while(MTestMain.checkIsActive()){
				if(rand.nextDouble() < this.writePercent){
					isWrite = true;
					result = htc.sqlRandomUpdate();
				}else{
					isWrite = false;
					result = htc.sqlRandomSelect();
				}
				if(result != null && result.isSuccess()){
					this.queryNumber(1);
					if(isWrite)	this.writeNumber(1);
					else this.readNumber(1);
				}
			}
		} catch (HException | HSQLTimeOutException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * used by MVirtualTenant
	 * @return
	 * @throws HException
	 * @throws HSQLTimeOutException
	 */
	public boolean doSelect() throws HException, HSQLTimeOutException{
		HQueryResult result = htc.sqlRandomSelect();
		if(result != null && result.isSuccess())
			return true;
		return false;
	}
	/**
	 * used by MVirtualTenant
	 * @return
	 * @throws HException
	 * @throws HSQLTimeOutException
	 */
	public boolean doUpdate() throws HException, HSQLTimeOutException{
		HQueryResult result = htc.sqlRandomUpdate();
		if(result != null && result.isSuccess())
			return true;
		return false;
	}

}
