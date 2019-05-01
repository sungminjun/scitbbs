package server.MAIN;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import server.UI.Bbs_work;

public class Run_serv {
	private ServerSocket serverSocket;
	private ArrayList<Bbs_work> list;

	public void go() throws IOException {
		serverSocket = new ServerSocket(5432);
		System.out.println("**Running SCIT MASTER BBS Server...**");
		list = new ArrayList<>();
		while (true) {
			Socket socket = serverSocket.accept();
			if (socket != null) {
				Bbs_work mgr = new Bbs_work(socket, list);
				Thread t = new Thread(mgr);
				t.start();
			}
		}
	}

	public void closeAll() throws IOException {
		if (serverSocket != null)
			serverSocket.close();
	}

	public static void main(String[] args) {
		Run_serv ts = new Run_serv();
		try {
			ts.go();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				ts.closeAll();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
