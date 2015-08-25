package test.mysql;

import java.util.Random;

import dbInfo.HSQLTimeOutException;

public class MVirtualTenant extends MTenant {
	private int id;
	private MTenant[] tenant;

	public MVirtualTenant(int id)  {
		this.id = id;
		tenant = new MTenant[MTestMain.TENANT_PER_THREAD];
		for(int i = 0; i < MTestMain.TENANT_PER_THREAD; i++){
			tenant[i] = new MTenant(MTestMain.TENANT_PER_THREAD * this.id + i + 1);
		}
	}
	public void init() {
		for(MTenant t : tenant){
			t.init();
		}
	}
	public void clean() {
		for(MTenant t : tenant){
			t.clean();
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
		} catch (HSQLTimeOutException e) {
			e.printStackTrace();
		}
	}

}
