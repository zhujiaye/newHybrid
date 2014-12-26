package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import config.HConfig;

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

	public synchronized void sendMoveToVoltDBMessage(int tenantid, int voltDB_id) {
		sendMessage("2&" + tenantid + "&" + voltDB_id
				+ "&(sendMoveToVoltDBmessage)");
	}

	public synchronized void sendMoveToMySQLMessage(int tenantid, int voltDB_id) {
		sendMessage("4&" + tenantid + "&" + voltDB_id
				+ "&(sendMoveToMySQLmessage)");
	}

	public void sendConfirmedMessage(int tenantid) {
		sendMessage("0&" + tenantid + "&(sendConfirmedMessage)");
	}

	public void sendAllFinishedMessage() {
		sendMessage("1" + "&(sendAllFinishedMessage)");
	}

	public void sendAllConfirmedMessage() {
		sendMessage("3" + "&(sendAllConfirmedMessage)");
	}

	private void sendMessage(String msg) {
		if (HConfig.debug)
			System.out.println("sent:" + msg);
		writer.println(msg);
		writer.flush();
	}
	// @Override
	// public void run() {
	// // TODO Auto-generated method stub
	// System.out.println("Socket "+socket.toString()+" start sending!");
	// while (true){
	// break;
	// }
	// System.out.println("Socket "+socket.toString()+" stop sending!");
	// }
}
