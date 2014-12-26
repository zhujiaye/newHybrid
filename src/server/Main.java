package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import config.HConfig;

public class Main {
	public static void main(String[] args) {
		HConfig.load();
		HConfig.print();
		ServerSocket sSocket = null;
		try {
			sSocket = new ServerSocket(HConfig.SOCKET_PORT);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Socket cSocket;
		Monitor monitor = new Monitor();
		new Thread(monitor).start();
		int acceptedSockets = 0;
		while (true) {
			try {
				cSocket = sSocket.accept();
				if (cSocket != null) {
					acceptedSockets++;
					Sender sender = new Sender(cSocket);
					Receiver receiver = new Receiver(cSocket, monitor);
					if (monitor.mapSocket(receiver, sender)) {
						// new Thread(sender).start();
						new Thread(receiver).start();
					}
					if (acceptedSockets >= HConfig.CONCURRENCY_PROCESS)
						break;
				}
			} catch (SocketTimeoutException e) {
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
