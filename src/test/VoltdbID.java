package test;

import newhybrid.HException;
import client.HTenantClient;

public class VoltdbID {

	public static void main(String[] args) throws HException{
		HTenantClient htc=new HTenantClient(3000);
		System.out.println(htc.getIDInVoltdb());
	}
}
