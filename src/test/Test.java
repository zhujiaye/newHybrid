package test;

import java.util.Arrays;

import server.HTenant;
import server.MysqlToVoltdbMoverThread;
import server.SortableTenant;
import server.VoltdbToMysqlMoverThread;

public class Test {

	public static void main(String[] args) {
		System.out.println("--------MYSQL-------");
		for (int i = 0; i < 20; i++) {
			System.out.println("test " + i);
			MysqlToVoltdbMoverThread[] threads = new MysqlToVoltdbMoverThread[9];
			for (int j = 0; j < 9; j++) {
				threads[j] = new MysqlToVoltdbMoverThread(new HTenant(null, i
						* 10 + j + 1), j + 1, false);
			}
			for (int j = 0; j < 9; j++)
				threads[j].start();
			System.out.println("all started");
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for (int j = 0; j < 9; j++)
				threads[j].cancel();
			System.out.println("all canceled");
			for (int j = 0; j < 9; j++)
				try {
					threads[j].join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			System.out.println("all finished");
		}
		System.out.println("--------VOLTDB-------");
		for (int i = 0; i < 20; i++) {
			System.out.println("test " + i);
			VoltdbToMysqlMoverThread[] threads = new VoltdbToMysqlMoverThread[9];
			for (int j = 0; j < 9; j++) {
				HTenant tenant = new HTenant(null, i * 10 + j + 1);
				tenant.finishMovingToVoltdb(j + 1);
				threads[j] = new VoltdbToMysqlMoverThread(tenant, false);
			}
			for (int j = 0; j < 9; j++)
				threads[j].start();
			System.out.println("all started");
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for (int j = 0; j < 9; j++)
				threads[j].cancel();
			System.out.println("all canceled");
			for (int j = 0; j < 9; j++)
				try {
					threads[j].join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			System.out.println("all finished");
		}
	}
}
