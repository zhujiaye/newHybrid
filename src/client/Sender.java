package client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import config.HConfig;
import utillity.HTenantDynamicInfo;

public class Sender {
	private Socket socket;
	private PrintWriter writer;

	public Sender(Socket socket) {
		this.socket = socket;
		try {
			writer = new PrintWriter(socket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public synchronized void sendInfoMessageForTenant(int id,
			HTenantDynamicInfo info) {
		sendMessage("1&" + id + "&" + info.encode()+"&(sendInfoMessageForTenant)");
	}

	public void sendReadyMessage(int tenantid) {
		sendMessage("0&" + tenantid + "&(sendReadyMessage)");
	}

	public void sendFinishedMessage(int tenantid) {
		sendMessage("2&" + tenantid + "&(sendFinishedMessage)");
	}

	private void sendMessage(String msg) {
		if (HConfig.debug)
			System.out.println("sent:" + msg);
		writer.println(msg);
		writer.flush();
	}
}
