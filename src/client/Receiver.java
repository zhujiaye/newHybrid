package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

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
					//System.out.println("Tenant " + id + " confirmed");
					monitor.setConfirmedForTenant(id);
				}
				if (strs[0].equals("1")) {
					break;
				}
				if (strs[0].equals("3")) {
					System.out.println("All confirmed");
					monitor.setAllConfirmed(true);
				}
				if (strs[0].equals("2")) {
					int id = Integer.valueOf(strs[1]);
					int voltDB_id = Integer.valueOf(strs[2]);
					System.out
							.println("Tenant "
									+ id
									+ " is about to using VoltDB in next interval and voltDB's slot id="
									+ voltDB_id);
					monitor.changeToVoltDBForTenant(id, voltDB_id);
				}
				if (strs[0].equals("4")) {
					int id = Integer.valueOf(strs[1]);
					int voltDB_id = Integer.valueOf(strs[2]);
					System.out.println("Tenant " + id
							+ " is about to using MySQL in next interval");
					monitor.changeToMySQLDBForTenant(id);
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
