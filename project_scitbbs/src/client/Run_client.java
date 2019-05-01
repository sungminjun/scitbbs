package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Run_client {
	// socket, bufferedReader, printWriter�� ����
	// ���� ����, socket���� Ű���� �Է� �߼�, socket���� ������ �޼��� ���
	// thread �̿� ��/���� ��������
	private Socket socket;
	private BufferedReader br, serverBr;
	private PrintWriter pw;
	Scanner scan = new Scanner(System.in);

	public void go() throws UnknownHostException, IOException {
		System.out.print("������ ip�ּҸ� �Է��ϼ���. Enter�� ġ�ø� �⺻������ �����մϴ�. : ");
		String connectAddr = scan.nextLine();
		if (connectAddr.equals("")) {
			connectAddr = "203.233.196.78";
		}
		socket = new Socket(connectAddr, 5432);
		RcvClass rcv = new RcvClass();
		rcv.start();
		br = new BufferedReader(new InputStreamReader(System.in));
		pw = new PrintWriter(socket.getOutputStream(), true);
		while (true) {
			String str = br.readLine();
			pw.println(str);
		}
	}

	public static void main(String[] args) {
		Run_client tc = new Run_client();
		try {
			tc.go();
		} catch (UnknownHostException e) {
//			e.printStackTrace();
			System.out.println("UnknownHost Exception occured.");
		} catch (ConnectException e ) {
			System.out.println("������ ������ �� �����ϴ�.");
		} catch (IOException e) {
//			e.printStackTrace();
			System.out.println("I/O Exception occured.");
		} finally {
			try {
				tc.closeAll();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Ŭ���̾�Ʈ�� �����մϴ�.");
		return;
	}

	public void closeAll() throws IOException {
		if (serverBr != null)
			serverBr.close();
		if (br != null)
			br.close();
		if (pw != null)
			pw.close();
		if (socket != null)
			socket.close();
	}

	public class RcvClass extends Thread {
		public void run() {
			try {
				serverBr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				while (true) {
					String str = serverBr.readLine();
					if (str != null)
						System.out.println(str);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}