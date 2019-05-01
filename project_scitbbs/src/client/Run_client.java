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
	// socket, bufferedReader, printWriter는 각각
	// 서버 연결, socket통해 키보드 입력 발송, socket통해 수신한 메세지 출력
	// thread 이용 송/수신 동시진행
	private Socket socket;
	private BufferedReader br, serverBr;
	private PrintWriter pw;
	Scanner scan = new Scanner(System.in);

	public void go() throws UnknownHostException, IOException {
		System.out.print("접속할 ip주소를 입력하세요. Enter를 치시면 기본값으로 접속합니다. : ");
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
			System.out.println("서버에 연결할 수 없습니다.");
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
		System.out.println("클라이언트를 종료합니다.");
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