package test;

import newhybrid.HException;
import client.HTenantClient;

public class Test {
	public static void main(String[] args) throws HException {
		HTenantClient[] clients = new HTenantClient[5];
		for (int i = 0; i < 5; i++) {
			clients[i] = new HTenantClient(i + 1);
		}
		for (int i = 0; i < 5; i++) {
			System.out.println(clients[i].login());
		}
	}
}
