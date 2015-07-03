package test;

import newhybrid.HException;
import client.ServerClient;

public class Test {

	public static void main(String[] args) throws HException {
		int n = 3000;
		ServerClient[] clients = new ServerClient[n];
		for (int i = 0; i < n; i++) {
			clients[i] = new ServerClient();
			clients[i].connect();
		}
		for (int i = 0; i < n; i++) {
			clients[i].tenantLogin(i + 1);
		}
		System.out.println("Logged in for all");
		for (int i = 0; i < n; i++) {
			clients[i].tenantStart(i + 1);
		}
		System.out.println("Start for all");
		for (int i = 0; i < n; i++) {
			clients[i].tenantStart(i + 1);
		}
		System.out.println("Start for all");
		for (int i = 0; i < n; i++) {
			clients[i].tenantStart(i + 1);
		}
		System.out.println("Start for all");
		
	}
}
