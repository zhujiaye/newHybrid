package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;

import utillity.HTenantDynamicInfo;

public class Receiver implements Runnable {
	private Socket socket;
	private Monitor monitor;
	private BufferedReader in;

	public Receiver(Socket socket, Monitor monitor) {
		this.socket = socket;
		this.monitor = monitor;
		try {
			in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		String str = null;
		System.out.println("Socket " + socket.toString() + " start receiving!");
		try {
			while ((str = in.readLine()) != null) {
				String[] strs = str.split("&");
				if (strs[0].equals("0")) {
					int id = Integer.valueOf(strs[1]);
					monitor.getReadyForTenant(id, this);
				}
				if (strs[0].equals("1")) {
					int id=Integer.valueOf(strs[1]);
					HTenantDynamicInfo tmp=new HTenantDynamicInfo();
					tmp.decode(strs[2]);
					monitor.updateInfoForTenant(id,tmp);
				}
				if (strs[0].equals("2")) {
					int id = Integer.valueOf(strs[1]);
					monitor.getFinishedForTenant(id);
				}
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Socket " + socket.toString() + " stop receiving!");
	}

}
