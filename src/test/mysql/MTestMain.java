package test.mysql;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;

import newhybrid.HException;

public class MTestMain {
	public static boolean ACTIVE = true;
	public static int TOTAL_TENANT = 100;
	public static int TOTAL_INTERVAL = 1;
	public static int TIME_PER_INTERVAL = 1*60*1000;
	public static MTenant TENANT[];
	//MVirtualTenant
	public static int TOTAL_THREAD = 100;
	public static int TENANT_PER_THREAD = 30;
	public static boolean USE_VIRTUAL_TENANT = false;
	
	public static int[] START = {1, 1501, 2401};
	public static double[] PERCENT = {0.5, 0.3, 0.2};
	public static void main(String[] args){
		//fetch command line arguments
		if(args.length > 0){
			TOTAL_TENANT = Integer.parseInt(args[0]);
		}
		if(args.length > 1){
			TOTAL_INTERVAL = Integer.parseInt(args[1]);
		}
		if(args.length > 2){
			TIME_PER_INTERVAL = Integer.parseInt(args[2]) * 60 * 1000;
		}
		if(args.length > 3){
			int tmp = Integer.parseInt(args[3]);
			if(tmp == 0)	USE_VIRTUAL_TENANT = false;
			else USE_VIRTUAL_TENANT = true;
		}
		
		if(USE_VIRTUAL_TENANT)	DoTest_V();
		else	DoTest();
	}
	
	public static void DoTest(){
		try {
			//initialize
			TENANT = new MTenant[TOTAL_TENANT];
			int index = 0;
			for(int i = 0; i < 2; i++){
				int number = (int) (TOTAL_TENANT * PERCENT[i]);
				for(int j = 0; j < number; j++){
					TENANT[index] = new MTenant(START[i] + j);
					TENANT[index].init();
					index++;
				}
			}
			for(int j = 0; index < TOTAL_TENANT; index++, j++){
				TENANT[index] = new MTenant(START[2]+j);
				TENANT[index].init();
			}
			BufferedWriter out = new BufferedWriter(new FileWriter("MTest.txt", true));
			DecimalFormat df = new DecimalFormat(".0000");
			System.out.println("Initialization finished!");
			System.out.println("Start testing...");
			System.out.println("\tTotal tenant: "+TOTAL_TENANT);
			System.out.println("\tTotal interval: "+TOTAL_INTERVAL);
			System.out.println("\tTime per interval: "+TIME_PER_INTERVAL/60000+" minutes.");
			//run test
			for(MTenant tenant : TENANT){
				tenant.start();
			}
			
			for(int interval = 0; interval < TOTAL_INTERVAL; interval++){
				for(MTenant tenant : TENANT){
					tenant.queryNumber(-1);
					tenant.readNumber(-1);
					tenant.writeNumber(-1);
				}
				Thread.sleep(TIME_PER_INTERVAL);
				int query = 0, read = 0, write = 0;
				for(MTenant tenant : TENANT){
					query += tenant.queryNumber(0);
					read += tenant.readNumber(0);
					write += tenant.writeNumber(0);
				}
				//output
				out.write(""+interval+" "+query);
				out.newLine(); out.flush();
				System.out.println("Interval "+(interval+1)+" finished! Throughput: "+query+".(Total: "+TOTAL_INTERVAL+" intervals...)");
				System.out.println("Read: "+read+". Write: "+write+". Write percent: "+df.format(write*1.0/(read+write)));
			}
			
			//clean up
			out.close();
			setActive(false);
			Thread.sleep(10*1000);
			for(int i = 0; i < TOTAL_TENANT; i++){
				TENANT[i].clean();
				Thread.sleep(10);
			}
			System.out.println("End.");
		} catch (HException | InterruptedException | IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void DoTest_V(){
		try {
			//initialize
			TENANT = new MVirtualTenant[TOTAL_THREAD];
			for(int i = 0; i < TOTAL_THREAD; i++){
				TENANT[i] = new MVirtualTenant(i);
				TENANT[i].init();
			}
			BufferedWriter out = new BufferedWriter(new FileWriter("MTest.txt", true));
			DecimalFormat df = new DecimalFormat(".0000");
			System.out.println("Initialization finished!");
			System.out.println("Start testing...");
			System.out.println("\tTotal tenant: "+TOTAL_TENANT);
			System.out.println("\tTotal interval: "+TOTAL_INTERVAL);
			System.out.println("\tTime per interval: "+TIME_PER_INTERVAL/60000+" minutes.");
			//run test
			for(MTenant tenant : TENANT){
				tenant.start();
			}
			
			for(int interval = 0; interval < TOTAL_INTERVAL; interval++){
				for(MTenant tenant : TENANT){
					tenant.queryNumber(-1);
					tenant.readNumber(-1);
					tenant.writeNumber(-1);
				}
				Thread.sleep(TIME_PER_INTERVAL);
				int query = 0, read = 0, write = 0;
				for(MTenant tenant : TENANT){
					query += tenant.queryNumber(0);
					read += tenant.readNumber(0);
					write += tenant.writeNumber(0);
				}
				//output
				out.write(""+interval+" "+query);
				out.newLine(); out.flush();
				System.out.println("Interval "+(interval+1)+" finished! Throughput: "+query+".(Total: "+TOTAL_INTERVAL+" intervals...)");
				System.out.println("Read: "+read+". Write: "+write+". Write percent: "+df.format(write*1.0/(read+write)));
			}
			
			//clean up
			out.close();
			setActive(false);
			Thread.sleep(10*1000);
			for(int i = 0; i < TOTAL_TENANT; i++){
				TENANT[i].clean();
				Thread.sleep(10);
			}
			System.out.println("End.");
		} catch (HException | InterruptedException | IOException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean checkIsActive(){
		return ACTIVE;
	}
	public static void setActive(boolean is){
		ACTIVE = is;
	}

}
