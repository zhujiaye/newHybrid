package test.mysql;

import java.util.Random;

import newhybrid.HException;
import newhybrid.HSQLTimeOutException;

public class MVirtualTenant extends MTenant {
	private int id;
	private MTenant[] tenant;

	public MVirtualTenant(int id) throws HException {
		this.id = id;
		tenant = new MTenant[MTestMain.TENANT_PER_THREAD];
		for(int i = 0; i < MTestMain.TENANT_PER_THREAD; i++){
			tenant[i] = new MTenant(MTestMain.TENANT_PER_THREAD * this.id + i + 1);
			tenant[i].init();
		}
	}
	
	public void run(){
		try {
			Random rand = new Random(System.nanoTime());
			boolean result, isWrite;
			while(MTestMain.checkIsActive()){
				if(rand.nextDouble() < this.writePercent){
					isWrite = true;
					result = tenant[rand.nextInt(MTestMain.TENANT_PER_THREAD)+1].doUpdate();
				}else{
					isWrite = false;
					result = tenant[rand.nextInt(MTestMain.TENANT_PER_THREAD)+1].doSelect();
				}
				if(result){
					this.queryNumber(1);
					if(isWrite)	this.writeNumber(1);
					else this.readNumber(1);
				}
			}
		} catch (HException | HSQLTimeOutException e) {
			e.printStackTrace();
		}
	}

}
