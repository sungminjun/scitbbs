package server.UI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import server.MGR.BbsManager;
import server.VO.BoardVO;
import server.VO.UsersVO;

public class Bbs_work extends Thread {

	ArrayList<Bbs_work> list;
	ArrayList<Bbs_work> chatter;

	private Socket socket;
	private BufferedReader br;
	private PrintWriter pw;

	private String current_user_id;
	private int current_user_auth;
	private String current_user_locat;
	private String current_user_locat_prev;
	private boolean current_user_chat_status = false;
//	private String current_user_chat_room_no;	// ���ȣ, ä�ù��� ������ ���� �� ������ ������ ���
//	private String current_user_chat_room_status;	// �������, kick�̳� ä�ù� ������ ���� �� ������ ������ ���

	BbsManager mgr = new BbsManager();	

	//
	//
	// �⺻ ���� �۵� ����
	public Bbs_work(Socket socket, ArrayList<Bbs_work> list) throws IOException { // �⺻ client���� ���� ������
		this.socket = socket;
		this.list = list;
		br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		pw = new PrintWriter(socket.getOutputStream(), true);
		String message = "New connection: " + socket.getInetAddress();
		System.out.println(socket.getInetAddress() + " : " + message);

		synchronized (list) {
			list.add(this);
		}
	}

	public void work() throws Exception { // ������ ������ ���ϴ� �޼ҵ�
		current_user_locat = "999";
		while (true) {
			try {
				sendMessage("\n\n\n");
				userInterface(current_user_locat);
				br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				String str = br.readLine();
				userInputResponse(str);
//			broadcasting(socket.getInetAddress() + " says: " + str);
				System.out.println("Server Thread run() rcving msg : " + socket.getInetAddress() + " " + str);
				str = null;
			} catch (NumberFormatException e) {
				System.out.println("numforException occured");
				sendMessage("��ɾ Ȯ���� �� �����ϴ�.");
			}
		}

	}

	@Override
	public void run() { // ������ ���ư�
		try {
			work();
		} catch (IOException e) {
			System.out.println("disconnected by IOException : " + socket.getInetAddress());
		} catch (Exception e) {
			System.out.println("occured some Exception : " + socket.getInetAddress());
			e.printStackTrace();
		} finally {
			try {
				removeConnection(this);
				closeAll();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void closeAll() throws IOException { // pw, br, socket�� �ݰ� ������ ������.
		if (pw != null)
			pw.close();
		if (br != null)
			br.close();
		if (socket != null)
			socket.close();
	}

	public void sendMessage(String message) { // client�� �����ϰ� ���� String�� ���� (sysout)
		pw.println(message);
		pw.flush();
	}

	public void broadcasting(String message) { // ��ü ������ broadcasting �޼ҵ�, ��� ���ӿ� �޼����� ������.
		for (Bbs_work serverWorker : list) {
			serverWorker.sendMessage(message);
		}
	}

	public void chatMsg(String message) { // ä�ù�� chatMsg�޼ҵ�, ä�ù濡 ������ �ο����Ը� �޼����� ������.
		for (Bbs_work chat_true : list) {
			if (chat_true.current_user_chat_status) {
				chat_true.sendMessage("[" + current_user_id + "(" + mgr.idName(current_user_id) + ") \t]  " + message);
			}
		}
	}

	public void whisper(String message, String rcvId) { // ����(�ӼӸ�)�� �޼ҵ�, ������ID�� ��ġ�ϴ� ��쿡�� �޼����� ������.
		boolean sentWhisperStatus = false;
		for (Bbs_work whisper : list) {
			if (whisper.current_user_id.equalsIgnoreCase(rcvId)) {
				whisper.sendMessage(
						"[" + current_user_id + "(" + mgr.idName(current_user_id) + ")(��)�κ��� ��ſ��� �ӼӸ� \t]  " + message);
				sentWhisperStatus = true;
			}
		}
		if (sentWhisperStatus) {
			for (Bbs_work whisper : list) {
				if (whisper.current_user_id.equalsIgnoreCase(current_user_id)) {
					whisper.sendMessage("[" + rcvId + "(" + mgr.idName(rcvId) + ")���� �ӼӸ� \t]  " + message);
				}
			}
		} else {
			sendMessage("����� �����ϴ�.");
		}
	}

	public void removeConnection(Bbs_work worker) { // ���� ����, Ŀ�ؼ� ȸ�� ���
		list.remove(worker);
//		broadcasting(socket.getInetAddress() + "disconnected.");
	}

	//
	//
	// �Էºб⿡ ���� ui �����
	private void userInterface(String current_user_locat) throws IOException { // ��ġ������ ���� �������� �����ֵ��� �Ѵ�.
		switch (current_user_locat) {
		case "994":
			showMenu994();
			break;

		case "995":
			showMenu995();
			break;

		case "996":
			showMenu996();
			break;

		case "997":
			showMenu997();
			break;

		case "998":
			showMenu998();
			break;

		case "999":
			showMenu999();
			requestLogIn(rcvLogin());
			current_user_locat = "000";
			break;

		case "000":
			showMenu000();
			break;

		case "001":
			showMenu001();
			break;

		case "002":
			showMenu002();
			break;

		case "011":
			showMenu011();
			break;

		case "012":
			showMenu012();
			break;

		case "013":
			showMenu013();
			break;

		case "021":
			showMenu021();
			break;
		}

		return;
	}

	//
	//
	// �Էºб�ó����
	private void userInputResponse(String userInput) throws IOException { // ����ڷκ��� �Է� ���� �������� ��ɾ� ���� �Ǵ�
		Integer tempInt;
		System.out.println(current_user_id + " press : " + userInput + " from " + socket.getInetAddress());
		// user���� üũ �� ��� ��ɾ�
		if (current_user_auth >= 5 && userInput.equals("TERMINATE_THIS_SERVER")) {
			terminateServer();

			// ����, ������ ���� ��ɾ� T, X, C, GO xxx, Memo <somebody> <contents>
		} else if (userInput.equals("")) { // ���� (����) �Է� ��ȣ�� �ƹ� �ൿ�� ���� �ʴ´�.
		} else if (userInput.equalsIgnoreCase("T") || current_user_locat.equalsIgnoreCase("TOP")
				|| current_user_locat.equalsIgnoreCase("��")) { // �ʱ�ȭ������ �̵�
			current_user_locat_prev = current_user_locat;
			current_user_locat = "000";
		} else if (userInput.equalsIgnoreCase("X") || current_user_locat.equalsIgnoreCase("EXIT")
				|| current_user_locat.equalsIgnoreCase("QUIT") || userInput.equalsIgnoreCase("��")) { // �����û
			closeConn();
		} else if (userInput.equalsIgnoreCase("C") || userInput.equalsIgnoreCase("HELP")
				|| userInput.equalsIgnoreCase("��")) { // ��ɾ���ȸȭ�� �̵�
			help();
		} else if (userInput.equalsIgnoreCase("P") || userInput.equals("��")) { // ����ȭ������
			current_user_locat = current_user_locat_prev;
		} else if (userInput.equalsIgnoreCase("pf")) {
			sendMessage("���� Ȯ�� [userId : " + current_user_id + ", ����:" + current_user_auth + ", ��ġ:"
					+ current_user_locat + "] �Դϴ�.");
			if (current_user_auth >= 5) {
				sendMessage("��밡�� ��� ��ɾ� : TERMINATE_THIS_SERVER");
			}
			// �̱�����Ȳ. Ÿ�� id�� ���� ������ get �Ұ����� ������ ����

			//
			// TOPȭ��
		} else if (current_user_locat.equals("000") && userInput.equalsIgnoreCase("1")) {
			current_user_locat_prev = current_user_locat;
			current_user_locat = "001";
		} else if (current_user_locat.equals("000") && userInput.equalsIgnoreCase("2")) {
			current_user_locat_prev = current_user_locat;
			current_user_locat = "002";
		} else if (current_user_locat.equals("000") && userInput.equalsIgnoreCase("11")) {
			current_user_locat_prev = current_user_locat;
			current_user_locat = "011";
		} else if (current_user_locat.equals("000") && userInput.equalsIgnoreCase("12")) {
			current_user_locat_prev = current_user_locat;
			current_user_locat = "012";
		} else if (current_user_locat.equals("000") && userInput.equalsIgnoreCase("13") && current_user_auth == 5) {
			current_user_locat_prev = current_user_locat;
			current_user_locat = "013";
		} else if (current_user_locat.equals("000") && userInput.equalsIgnoreCase("21")) {
			current_user_locat_prev = current_user_locat;
			current_user_locat = "021";

			// ȸ������ �޴� ȭ��
		} else if (current_user_locat.equals("002") && userInput.equalsIgnoreCase("1")) { // guest���Խ�û, �Ϲ� Ż���û, ������
			current_user_locat_prev = current_user_locat;
			current_user_locat = "998";

		} else if (current_user_locat.equals("002") && userInput.equalsIgnoreCase("2") && current_user_auth >= 5) { // ������
			current_user_locat_prev = current_user_locat;
			current_user_locat = "996";

		} else if (current_user_locat.equals("002") && userInput.equalsIgnoreCase("2") && current_user_auth < 5
				&& current_user_auth >= 3) { // ������ Ż�����
			current_user_locat_prev = current_user_locat;
			current_user_locat = "994";

		} else if (current_user_locat.equals("002") && userInput.equalsIgnoreCase("11")) { // ������ ��üȸ����ȸ
			current_user_locat_prev = current_user_locat;
			current_user_locat = "995";

			// �Խ��� ȭ��, �۾��� �Է¹ޱ�
		} else if ((current_user_locat.equals("001") || current_user_locat.equals("011")
				|| current_user_locat.equals("012")) && userInput.equalsIgnoreCase("W")) {
			if (current_user_auth >= 3) {
				bbs_write(current_user_locat);
			} else {
				sendMessage("�մ��� ����� �� ���� ����Դϴ�.");
			}

			// ����������, ȸ���±�(���Խ�û ����) �Ǵ� Ż�� ó�� �� �� parseInt
		} else if (current_user_locat.equals("997") && current_user_auth >= 5) {
			tempInt = Integer.parseInt(userInput);
			if (tempInt != null) {
				users_adjAuth(current_user_locat, tempInt);
			}
		} else if (current_user_locat.equals("996") && current_user_auth >= 5) {
			tempInt = Integer.parseInt(userInput);
			if (tempInt != null) {
				users_removeUser(tempInt);
			}

			// �Խ��� �޴������� ����, parseInt ó���� ����
		} else if ((userInput.startsWith("MEMO ") || userInput.startsWith("memo ") || userInput.startsWith("TO ")
				|| userInput.startsWith("to "))) {
			int getIdStart = userInput.indexOf(" ") + 1;
			int getIdEnd = userInput.indexOf(" ", getIdStart);
			if (getIdEnd != 0) {
				String rcvWhisId = userInput.substring(getIdStart, getIdEnd).toUpperCase();
				String whisMsg = userInput.substring(getIdEnd + 1);
				whisper(whisMsg, rcvWhisId);
			}

		} else if ((userInput.startsWith("r ") || userInput.startsWith("R ") && current_user_locat.equals("013"))) {
			if (current_user_auth >= 3) {
				tempInt = Integer.parseInt(userInput.substring(2));
				if (tempInt != null) {
					bbs_erase(current_user_locat, tempInt);
				}
			} else {
				sendMessage("�մ��� ����� �� ���� ����Դϴ�.");
			}
		} else if ((userInput.startsWith("d ") || userInput.startsWith("D ") && (current_user_locat.equals("001")
				|| current_user_locat.equals("011") || current_user_locat.equals("012")))) {
			if (current_user_auth >= 3) {
				tempInt = Integer.parseInt(userInput.substring(2));
				if (tempInt != null) {
					bbs_delete(current_user_locat, tempInt);
				}
			} else {
				sendMessage("�մ��� ����� �� ���� ����Դϴ�.");
			}
		} else if ((current_user_locat.equals("001") || current_user_locat.equals("011")
				|| current_user_locat.equals("012") || current_user_locat.equals("013"))) {
			if (current_user_auth >= 3) {
				tempInt = Integer.parseInt(userInput);
				if (tempInt != null) {
					bbs_read(current_user_locat, tempInt);
				}
			} else {
				sendMessage("�մ��� ����� �� ���� ����Դϴ�.");
			}

			// �� �� �Է³��뿡 ���ؼ�
		} else {
			sendMessage("��ɾ Ȯ���� �� �����ϴ�. �ٽ� �Է����ּ���.");
			userInput = null;
		}
	}

	//
	//
	// Ŀ�ؼ� ���� �� ���� ���������
	private void terminateConn() throws IOException { // ���� ����.
		sendMessage("������ �����մϴ�.");
		socket.close();
	}

	private void terminateServer() throws IOException { // ���� ���� ���
		removeConnection(this);
		closeAll();
		System.exit(0);
	}

	//
	//
	// ���� ���� ȭ�� ���� �� ��� �޼ҵ�
	private void help() { // ��ɾ� ���� �����ִ� ȭ��
		sendMessage("SCITBBS ������������������������������������������������������������������������������������������������������������������������������������������");
		sendMessage(" HELP                        �� �� ��               http://www.itmasters.org ");
		sendMessage("����������������������������������������������������������������������������������������������������������������������������������������������������������");
		sendMessage("\n\t[   �⺻��ɾ�   ]\t\t[   �Խ��Ǹ�ɾ�   ]");
		sendMessage("\n\t (TOP) �ʱ� ȭ������ �̵� \t(��ȣ) �ش� ��ȣ�� ���� �н��ϴ�.");
		sendMessage("\n\t (C, HELP) ��ɾ� ��� ��ȸ\t(W) ���ο� ���� �ۼ�");
		sendMessage("\n\t (X, QUIT) ���� ���� \t\t\t(D) ���� ������ ��û");
		sendMessage("\n\t (P) ���� ȭ������ �̵� ");
		sendMessage("\n\t (TO ID ����) id���� �ӼӸ�");
		sendMessage("\n\t (MEMO ID ����) id���� �ӼӸ�");
		sendMessage("\n������������������������������������������������������������������������������������������������������������������������������������������������������");
	}

	private void showMenu999() { // login ����ȭ��
		sendMessage("           ____________________________________________________________");
		sendMessage("           ____________________________________________________________");
		sendMessage("           ________@@@@@@@@@@____@@@@@@@____@@@@@@@@@___@@@@@@@@@______");
		sendMessage("           ______@@____________@@______________@@__________@@__________");
		sendMessage("           _______@@@@@@@_____@@______________@@__________@@___________");
		sendMessage("           _____________@@___@@______________@@__________@@____________");
		sendMessage("           ____________@@___@@______________@@__________@@_____________");
		sendMessage("           ____@@@@@@@@@_____@@@@@@@@___@@@@@@@@_______@@______________");
		sendMessage("           ____________________________________________________________");
		sendMessage("           ___@@@@@@@@@@@@___@@@@@@@@@@@@___@@@@@@@@@@@________________");
		sendMessage("           ___________@@@____________@@@___@@@_____@@@_________________");
		sendMessage("           _________@@@___________@@@____@@@__@@___@@__________________");
		sendMessage("           ___@@__@@@__________@@@@_____@@_____@@@@___@@@@@@@@@________");
		sendMessage("           _____@@@_________@@@__@@@__________@@@______________________");
		sendMessage("           _______@@_____@@@________@@______@@_________________________");
		sendMessage("           _____________________________________________�� �誦����____");
		sendMessage("[�ȳ�] ó�� ���ź��� guest �� �Է��Ͽ� ȸ�� ������ �����ϼ���.");
	}

	private void showMenu000() { // TOP
		BoardVO rNotice = mgr.recentNotice();
		ArrayList<BoardVO> rBoard = mgr.recentBoard();

		sendMessage("SCITBBS ������������������������������������������������������������������������������������������������������������������������������������������");
		sendMessage(" TOP                         �ʱ�ȭ��               http://www.itmasters.org ");
		sendMessage("����������������������������������������������������������������������������������������������������������������������������������������������������������");

		if (current_user_auth == 1) {
			sendMessage("\n\t[  ��������  ]\t 1. �������� \t  2. ���Խ�û\t");
			sendMessage("\n\t[  Ŀ�´�Ƽ  ]\t11. �����Խ���\t 12. �͸�Խ���");
		} else if (current_user_auth >= 3 && current_user_auth < 5) {
			sendMessage("\n\t[  ��������  ]\t 1. �������� \t  2. ������������\t");
			sendMessage("\n\t[  Ŀ�´�Ƽ  ]\t11. �����Խ���\t 12. �͸�Խ���");
		} else if (current_user_auth >= 5) {
			sendMessage("\n\t[  ��������  ]\t 1. �������� \t  2. ���Խ�û/ȸ������\t");
			sendMessage("\n\t[  Ŀ�´�Ƽ  ]\t11. �����Խ���\t 12. �͸�Խ���\n\t\t\t13. ������û����");
		}
		sendMessage("\n\t[  �ΰ����  ]\t21. �� ȭ �� \t 22. ����(�̱���)\t");
		sendMessage("\n\t[  ��������  ]\t");
		if (rNotice == null) {
			sendMessage("\t\t\t�ֱ� 3�� �� ���������� �����ϴ�.");
		} else {
			sendMessage("\t�������� �Խ��� " + rNotice.getSeqNo() + "���� [" + rNotice.getSubj() + "]��(��) �ֽ��ϴ�.");
		}

		sendMessage("\n\t[  �� �� ��  ]\t");
		if (rBoard.size() == 0) {
			sendMessage("\t�����Խ��ǿ� �ֱ� 3�ϳ� �Խù��� �����ϴ�.");
			sendMessage("\t�͸�Խ��ǿ� �ֱ� 3�ϳ� �Խù��� �����ϴ�.");
		} else if (rBoard.size() == 1 && rBoard.get(0).getBoardName().equalsIgnoreCase("boardfree")) {
			sendMessage("\t�����Խ��� " + rBoard.get(0).getSeqNo() + "�� �� [" + rBoard.get(0).getSubj() + "] �� �ֽ��ϴ�.");
			sendMessage("\t�͸�Խ��ǿ� �ֱ� 3�ϳ� �Խù��� �����ϴ�.");
		} else if (rBoard.size() == 1 && rBoard.get(0).getBoardName().equalsIgnoreCase("boardanony")) {
			sendMessage("\t�����Խ��� " + rBoard.get(0).getSeqNo() + "�� �� [" + rBoard.get(0).getSubj() + "] �� �ֽ��ϴ�.");
			sendMessage("\t�͸�Խ��ǿ� �ֱ� 3�ϳ� �Խù��� �����ϴ�.");
		} else if (rBoard.size() == 2) {
			if (rBoard.get(0).getBoardName().equals("boardfree")) {
				sendMessage("\t�����Խ��� " + rBoard.get(0).getSeqNo() + "�� �� [" + rBoard.get(0).getSubj() + "] �� �ֽ��ϴ�.");
				sendMessage("\t�͸�Խ��� " + rBoard.get(1).getSeqNo() + "�� �� [" + rBoard.get(1).getSubj() + "] �� �ֽ��ϴ�.");
			} else {
				sendMessage("\t�����Խ��� " + rBoard.get(1).getSeqNo() + "�� �� [" + rBoard.get(1).getSubj() + "] �� �ֽ��ϴ�.");
				sendMessage("\t�͸�Խ��� " + rBoard.get(0).getSeqNo() + "�� �� [" + rBoard.get(0).getSubj() + "] �� �ֽ��ϴ�.");
			}
		} else {
			sendMessage("\t\t�����Խ����� �ֽ� �Խù� ������ �������µ� �����߽��ϴ�.");
			sendMessage("\t\t�͸�Խ����� �ֽ� �Խù� ������ �������µ� �����߽��ϴ�.");
		}

		sendMessage("\n\n������������������������������������������������������������������������������������������������������������������������������������������������������");
		sendMessage("��ɾ�ȳ�(C) �����̵�(GO) ����(X)\n����> ");
	}

	private void showMenu001() { // NOTICE
		sendMessage("SCITBBS ������������������������������������������������������������������������������������������������������������������������������������������");
		sendMessage(" NOTICE                      ��������               http://www.itmasters.org ");
		sendMessage("����������������������������������������������������������������������������������������������������������������������������������������������������������");
		sendMessage("��ȣ\t  �� �� ��\t��ȸ\t\t	��	��");
		sendMessage("����������������������������������������������������������������������������������������������������������������������������������������������������������");

		HashMap<String, Object> map1 = new HashMap<String, Object>();
		map1.put("boardName", "boardnotice");
		ArrayList<BoardVO> boardNotice = mgr.bbsList(map1);
		for (int i = 0; i < boardNotice.size(); i++) {
			sendMessage(boardNotice.get(i).getSeqNo() + "\t" + boardNotice.get(i).getWriteId() + "("
					+ mgr.idName(boardNotice.get(i).getWriteId()) + ")\t" + boardNotice.get(i).getCounter() + "\t"
					+ boardNotice.get(i).getSubj());
		}
		sendMessage("����������������������������������������������������������������������������������������������������������������������������������������������������������");
		sendMessage("��ɾ�ȳ�(C) �����̵�(GO) ����(X)\n����> ");
	}

	private void showMenu002() { // 2���޴�. ���ѿ� ���� JOIN/MYPAGE/USERS �޴��� ���δ�.
		sendMessage("SCITBBS ������������������������������������������������������������������������������������������������������������������������������������������");
		if (current_user_auth == 1) {
			sendMessage(" JOIN                        ���Խ�û               http://www.itmasters.org ");
			sendMessage("����������������������������������������������������������������������������������������������������������������������������������������������������������");
			sendMessage("\n\n\n\t\t 1. ���Խ�û\n\n\n\n");
		} else if (current_user_auth >= 3 && current_user_auth < 5) {
			sendMessage(" MYPAGE                    ������������             http://www.itmasters.org ");
			sendMessage("����������������������������������������������������������������������������������������������������������������������������������������������������������");
			sendMessage("\n\n\n\t\t  1. Ż���û \t  2. ������ ����\n\n\n\n");
		} else if (current_user_auth >= 5) {
			sendMessage(" USERS                   ���Խ�û/ȸ������          http://www.itmasters.org ");
			sendMessage("����������������������������������������������������������������������������������������������������������������������������������������������������������");
			sendMessage("\n\n\t\t  1. ���Խ�û���� \t  2. Ż���û����\n\n\t\t 11. ��üȸ����ȸ\t\n\n");
		}
		sendMessage("����������������������������������������������������������������������������������������������������������������������������������������������������������");
		sendMessage("��ɾ�ȳ�(C) �����̵�(GO) ����(X)\n����> ");
	}

	private void showMenu011() { // Free
		sendMessage("SCITBBS ������������������������������������������������������������������������������������������������������������������������������������������");
		sendMessage(" FREE                       �����Խ���              http://www.itmasters.org ");
		sendMessage("����������������������������������������������������������������������������������������������������������������������������������������������������������");
		sendMessage("��ȣ\t  �� �� ��\t��ȸ\t\t	��	��");

		HashMap<String, Object> map1 = new HashMap<String, Object>();
		map1.put("boardName", "boardfree");
		ArrayList<BoardVO> boardNotice = mgr.bbsList(map1);
		for (int i = 0; i < boardNotice.size(); i++) {
			sendMessage(boardNotice.get(i).getSeqNo() + "\t" + boardNotice.get(i).getWriteId() + "("
					+ mgr.idName(boardNotice.get(i).getWriteId()) + ")\t" + boardNotice.get(i).getCounter() + "\t"
					+ boardNotice.get(i).getSubj());
		}
		sendMessage("����������������������������������������������������������������������������������������������������������������������������������������������������������");
		sendMessage("��ɾ�ȳ�(C) �����̵�(GO) ����(X)\n����> ");
	}

	private void showMenu012() { // Anony
		sendMessage("SCITBBS ������������������������������������������������������������������������������������������������������������������������������������������");
		sendMessage(" ANONY                      �͸�Խ���               http://www.itmasters.org ");
		sendMessage("����������������������������������������������������������������������������������������������������������������������������������������������������������");
		sendMessage("��ȣ\t��ȸ\t\t	��	��");

		HashMap<String, Object> map1 = new HashMap<String, Object>();
		map1.put("boardName", "boardanony");
		ArrayList<BoardVO> boardNotice = mgr.bbsList(map1);
		for (int i = 0; i < boardNotice.size(); i++) {
			sendMessage(boardNotice.get(i).getSeqNo() + "\t" + boardNotice.get(i).getCounter() + "\t"
					+ boardNotice.get(i).getSubj());
		}
		sendMessage("����������������������������������������������������������������������������������������������������������������������������������������������������������");
		sendMessage("��ɾ�ȳ�(C) �����̵�(GO) ����(X)\n����> ");
	}

	private void showMenu013() { // trash
		sendMessage("SCITBBS ������������������������������������������������������������������������������������������������������������������������������������������");
		sendMessage(" TRASH                    �����Խù�����             http://www.itmasters.org ");
		sendMessage("����������������������������������������������������������������������������������������������������������������������������������������������������������");
		sendMessage("��ȣ\t��ȸ\t\t	��	��");

		HashMap<String, Object> map1 = new HashMap<String, Object>();
		map1.put("boardName", "boardtrash");
		ArrayList<BoardVO> boardNotice = mgr.bbsList(map1);
		for (int i = 0; i < boardNotice.size(); i++) {
			sendMessage(boardNotice.get(i).getSeqNo() + "\t" + boardNotice.get(i).getCounter() + "\t"
					+ boardNotice.get(i).getSubj());
		}

		sendMessage("����������������������������������������������������������������������������������������������������������������������������������������������������������");
		sendMessage("��ɾ�ȳ�(C) �����̵�(GO) ����(X)\n����> ");
	}

	private void showMenu021() throws IOException { // enter chatRoom
		current_user_chat_status = true;
		chatRoom(current_user_id);
		current_user_chat_status = false;
		current_user_locat = "000";
	}

	private void showMenu994() throws IOException { // 2���޴��� 2�� (�Ϲ�����) ������ ����
		HashMap<String, Object> map17 = new HashMap<String, Object>();
		map17.put("userId", current_user_id);

		sendMessage("������ �������� �Է��Ͻʽÿ�.");
		String i17 = br.readLine();
		map17.put("userPf", i17);

		int i17result = mgr.updPf(map17);
		if (i17result == 1) {
			sendMessage("����ó�� �Ǿ����ϴ�.");
		} else {
			sendMessage("����ó���� �����Ͽ����ϴ�.");
		}
		current_user_locat = "000";
	}

	private void showMenu995() { // 2���޴��� 11�� (����������) ��üȸ����ȸ
		sendMessage("SCITBBS ������������������������������������������������������������������������������������������������������������������������������������������");
		sendMessage(" -MGR-                    ��ü ȸ�� ��ȸ             http://www.itmasters.org ");
		sendMessage("����������������������������������������������������������������������������������������������������������������������������������������������������������");
		sendMessage("��ȣ\t  I D\t\t �̸�\t\t�� �� �� ��");
		sendMessage("����������������������������������������������������������������������������������������������������������������������������������������������������������");
		ArrayList<UsersVO> all = mgr.listAllUser();
		for (UsersVO u : all) {
			System.out.println(u.getUserId().getBytes().length);
			if (u.getUserId().getBytes().length <= 7 && u.getUserName().getBytes().length >= 8) {
				sendMessage(u.getUserIdNo() + "\t" + u.getUserId() + "\t\t" + u.getUserName() + " " + u.getUserPf());
			} else if (u.getUserId().getBytes().length <= 7) {
				sendMessage(u.getUserIdNo() + "\t" + u.getUserId() + "\t\t" + u.getUserName() + "\t " + u.getUserPf());
			} else {
				sendMessage(u.getUserIdNo() + "\t" + u.getUserId() + "\t" + u.getUserName() + "\t " + u.getUserPf());
			}
		}
		sendMessage("����������������������������������������������������������������������������������������������������������������������������������������������������������");
		sendMessage("��ɾ�ȳ�(C) �����̵�(GO) ����(X)\n����> ");
	}

	private void showMenu996() { // 2���޴��� 2�� (����������) Ż���û�ڰ���
		sendMessage("SCITBBS ������������������������������������������������������������������������������������������������������������������������������������������");
		sendMessage(" -MGR-                    Ż�� ��û ����             http://www.itmasters.org ");
		sendMessage("����������������������������������������������������������������������������������������������������������������������������������������������������������");
		sendMessage("��ȣ\tID\t�̸�\t����ó\t�ڱ�Ұ�");
		sendMessage("����������������������������������������������������������������������������������������������������������������������������������������������������������");
		ArrayList<UsersVO> apprLeave = mgr.listUser(0);
		for (UsersVO u : apprLeave) {
			sendMessage(u.getUserIdNo() + "\t" + u.getUserId() + "\t" + u.getUserIdNo() + "\t" + u.getUserPhone() + "\t"
					+ u.getUserPf());
		}
		sendMessage("����������������������������������������������������������������������������������������������������������������������������������������������������������");
		sendMessage("��ɾ�ȳ�(C) �����̵�(GO) ����(X)\n����> ");
	}

	private void showMenu997() { // 2���޴��� 1�� (����������) ���Կ�û�ڰ���
		current_user_locat = "997";
		sendMessage("SCITBBS ������������������������������������������������������������������������������������������������������������������������������������������");
		sendMessage(" -MGR-                    ���� ��û ����             http://www.itmasters.org ");
		sendMessage("����������������������������������������������������������������������������������������������������������������������������������������������������������");
		sendMessage("��ȣ\tID\t\t�̸�\t\t����ó\t\t�� �� �� ��");
		sendMessage("����������������������������������������������������������������������������������������������������������������������������������������������������������");
		ArrayList<UsersVO> apprJoin = mgr.listUser(2);
		for (UsersVO u : apprJoin) {
			sendMessage(u.getUserIdNo() + "\t" + u.getUserId() + "\t" + u.getUserName() + "\t" + u.getUserPhone() + "\t"
					+ u.getUserPf());
		}
		sendMessage("����������������������������������������������������������������������������������������������������������������������������������������������������������");
		sendMessage("��ɾ�ȳ�(C) �����̵�(GO) ����(X)\n����> ");
	}

	private void showMenu998() throws IOException { // 2���޴��� 1��, ����/Ż��/�����޴� ���Ժб�
		if (current_user_auth == 1) {
			// ���Խ�û
			users_reqJoin();
			current_user_locat = "002";
			showMenu002();

		} else if (current_user_auth >= 3 && current_user_auth < 5) {
			// Ż���û
			users_reqLeave();
			current_user_locat = "002";
			showMenu002();

		} else if (current_user_auth >= 5) {
			// ���Խ�û ����
			showMenu997();
		}
	}

	//
	// ��� �޼ҵ��
	private String[] rcvLogin() throws IOException { // ���Ŀ� member ��ü�� ���ϵ���
		String[] result = new String[2];
		String id = "";
		String pw = "";

		// id�Է�
		while (true) {
			sendMessage("ID�� �Է��ϼ��� : ");
			id = br.readLine().toUpperCase();

			if (id.equalsIgnoreCase("guest")) {
				pw = "1234";
			} else if (id.length() > 8) {
				sendMessage("�ٽ� �Է��ϼ���");
				continue;
			} else if (id.equals("")) {
				sendMessage("�ٽ� �Է��ϼ���");
				continue;
			}
			break;
		}

		// pw�Է�
		while (true) {
			if (pw.equals("")) {
				sendMessage("��й�ȣ�� �Է��ϼ��� : ");
				pw = br.readLine();
				if (pw.length() > 11) {
					sendMessage("�ٽ� �Է��ϼ���");
					pw = "";
					continue;
				} else {
					break;
				}
			} else {
				break;
			}
		}

		// ����ó��
		result[0] = id;
		result[1] = pw;
		return result;
	}

	private boolean requestLogIn(String[] reqLogIn) throws IOException { // �Է¹��� �������� �α��� ����
		boolean reqResult = false;
		reqResult = mgr.reqLogIn(reqLogIn);

		if (reqResult) {
			String rcvId = reqLogIn[0];
			current_user_id = rcvId;
			current_user_auth = mgr.chkAuth(current_user_id);
			current_user_locat = "000";
			current_user_chat_status = false;
			sendMessage(current_user_id + "��, ȯ���մϴ�. \n[Enter]�� �Է��ϼ���.");
		} else {
			sendMessage("�߸��Է��ϼ̽��ϴ�. Ȯ���Ͻð� �ٽ� �õ��Ͻʽÿ�.\n������ �����մϴ�.");
			terminateConn();
			removeConnection(this);
		}

		if (current_user_auth == 0) {
			sendMessage("Ż���û���� ȸ���Դϴ�.");
			reqResult = false;
			sendMessage("�߸��Է��ϼ̽��ϴ�. Ȯ���Ͻð� �ٽ� �õ��Ͻʽÿ�.\n������ �����մϴ�.");
			terminateConn();
			removeConnection(this);
		} else if (current_user_auth == 2) {
			sendMessage("���Խ�û���� ȸ���Դϴ�. ���� ���ε��� �ʾҽ��ϴ�.");
			reqResult = false;
			sendMessage("�߸��Է��ϼ̽��ϴ�. Ȯ���Ͻð� �ٽ� �õ��Ͻʽÿ�.\n������ �����մϴ�.");
			terminateConn();
			removeConnection(this);
		}
		return reqResult;
	}

	private void closeConn() throws IOException { // ���� Ȯ�� ����� ����.
		sendMessage(current_user_id + "��, �����Ͻðڽ��ϱ�? (Y/n)");
		String str = br.readLine();
		if (str.equals("N") || str.equals("n")) {
		} else {
			terminateConn();
		}
		return;
	}

	private void bbs_write(String current_user_locat) throws IOException { // �Խ��ǿ� ���� ����. @param �������ġ
		HashMap<String, Object> map = new HashMap<String, Object>();
		String boardName = "";
		String seqNo = "";
		while (true) {
			if (current_user_locat.equals("001") && current_user_auth >= 5) {
				boardName = "boardnotice";
				seqNo = "BOARDNOTICESEQ.nextval";
			} else if (current_user_locat.equals("011")) {
				boardName = "boardfree";
				seqNo = "boardFreeSeq.nextval";
			} else if (current_user_locat.equals("012")) {
				boardName = "boardanony";
				seqNo = "boardAnonySeq.nextval";
			} else {
				sendMessage("������ �߻��߽��ϴ�. ������ Ȯ���ϰ� �ٽ� �õ��ϼ���.");
				break;
			}
			boardName = boardName.toUpperCase();
			map.put("boardName", boardName); // �����ܿ��� �ڵ����� ���� ��
			map.put("seqNo", seqNo);
			map.put("case1", boardName.toUpperCase()); // ���� ���� �ι� ������� �ȵǴ� �� �Ͽ�
			map.put("writeId", current_user_id); // �����ܿ��� �ڵ����� ���� ��

			String subj = "";
			String content = "";

			sendMessage("������ �Է��ϼ��� : ");
			while (true) {
				String temp = br.readLine();
				subj = temp;
				if (!temp.equals("") && temp.length() < 80) {
					break;
				} else if (temp.length() >= 80) {
					sendMessage("���̰� �ʹ� ��ϴ�. �ٽ� �Է����ּ���.");
				} else if (temp.equals("")) {
					sendMessage("������ �����ϴ�. �ٽ� �Է����ּ���.");
				}
			}

			sendMessage("������ �Է��ϼ��� : (�ۼ��� ��ġ���� �� ó���� \"��\" �̶�� �Է����ּ���");
			while (true) {
				String temp = br.readLine();
				if (temp.equals("��") && content.length() < 1600) {
					break;
				} else if (temp.equals("")) {
					temp = "\n\n";
				}

				content += temp;

				if (content.length() >= 1600) {
					sendMessage("(system) ���̰� �ʹ� ��ϴ�. �ۼ��� �����մϴ�.");
					break;
				} else if (content.equals("")) {
					sendMessage("(system) ������ �����ϴ�. �ٽ� �Է����ּ���.");
				}
			}

			sendMessage("�ۼ��� ������ �����Ͻðڽ��ϱ�? (Y/n)");
			String str = br.readLine();
			if (str.equalsIgnoreCase("n")) {
			} else {
				map.put("subj", subj);
				map.put("content", content);
				int result = mgr.bbsWrite(map);
				map.clear();
				if (result == 1) {
					sendMessage("����߽��ϴ�.");
				} else {
					sendMessage("��Ͽ� �����߽��ϴ�.");
				}
			}
			break;
		}
	}

	private void bbs_read(String current_user_locat, int seqNo) { // Ư�� �Խ����� ���� �д´�. @param �������ġ, �۹�ȣ
		HashMap<String, Object> map = new HashMap<String, Object>();
		String boardName = "";
		if (current_user_locat.equals("001")) {
			boardName = "boardnotice";
		} else if (current_user_locat.equals("011")) {
			boardName = "boardfree";
		} else if (current_user_locat.equals("012")) {
			boardName = "boardanony";
		} else {
			sendMessage("������ �߻��߽��ϴ�. ������ Ȯ���ϰ� �ٽ� �õ��ϼ���.");
		}
		boardName = boardName.toUpperCase();
		map.put("boardName", boardName);
		map.put("seqNo", seqNo);

		BoardVO temp = mgr.bbsRead(map);
		if (temp != null) {
			int cnt = mgr.bbsReadCnt(temp);
			System.out.println("cnt status (0=fail, 1=cnt update done.) : " + cnt);
			System.out.println(temp);
			sendMessage("����������������������������������������������������������������������������������������������������������������������������������������������������������");

			if (boardName.equalsIgnoreCase("boardanony")) {
				sendMessage("    " + temp.getSeqNo() + ".\t" + temp.getSubj() + "\n  �ۼ��Ͻ�: " + temp.getWriteDate()
						+ "\t��ȸ��: " + temp.getCounter());
			} else {
				sendMessage("    " + temp.getSeqNo() + ".\t" + temp.getSubj() + "\n  �ۼ���: " + temp.getWriteId() + "("
						+ mgr.idName(temp.getWriteId()) + ")" + "\t�ۼ��Ͻ�: " + temp.getWriteDate() + "\t��ȸ��: "
						+ temp.getCounter());
			}

			sendMessage("����������������������������������������������������������������������������������������������������������������������������������������������������������");
			sendMessage(temp.getContent());
			sendMessage("����������������������������������������������������������������������������������������������������������������������������������������������������������");
		} else {
			sendMessage("(system) �Խù��� �ҷ����� �� �����߽��ϴ�.");
		}
	}

	private void bbs_delete(String current_user_locat, int tempInt) { // �� ������û�� �Ѵ�. @param �������ġ, �۹�ȣ
		HashMap<String, Object> map = new HashMap<String, Object>();
		String boardName = "";
		if (current_user_locat.equals("001") && current_user_auth >= 5) {
			boardName = "boardnotice";
		} else if (current_user_locat.equals("011")) {
			boardName = "boardfree";
		} else if (current_user_locat.equals("012")) {
			boardName = "boardanony";
		} else {
			sendMessage("(system) ������ �߻��߽��ϴ�. ������ Ȯ���ϰ� �ٽ� �õ��ϼ���.");
		}
		boardName = boardName.toUpperCase();
		map.put("boardName", boardName);
		map.put("seqNo", tempInt);
		int deleteReqResult = mgr.bbsDelete(map);
		if (deleteReqResult == 1) {
			int renameResult = mgr.bbsRenameTrash(map);
			if (renameResult == 1) {
				sendMessage("������û�Ͽ����ϴ�.");
			} else {
				sendMessage("������û�� �����Ͽ����ϴ�.");
			}
		} else {
			sendMessage("������û�� �����Ͽ����ϴ�.");
		}
	}

	private void bbs_erase(String current_user_locat, int tempInt) { // ������û�� ���� �����Ѵ�. @param �������ġ, �۹�ȣ
		HashMap<String, Object> mapRead = new HashMap<String, Object>();
		HashMap<String, Object> mapEraseOrign = new HashMap<String, Object>();
		HashMap<String, Object> mapEraseTrash = new HashMap<String, Object>();
		String boardName = "";
		if (current_user_locat.equals("013") && current_user_auth >= 5) {
			boardName = "boardtrash";
		} else {
			sendMessage("(system) ������ �߻��߽��ϴ�. ������ Ȯ���ϰ� �ٽ� �õ��ϼ���.");
		}
		boardName = boardName.toUpperCase();
		mapRead.put("boardName", boardName);
		mapRead.put("seqNo", tempInt);
		BoardVO temp = mgr.bbsRead(mapRead);
		mapEraseOrign.put("boardName", temp.getBoardName());
		mapEraseOrign.put("seqNo", tempInt);

		mapEraseTrash.put("boardName", boardName);
		mapEraseTrash.put("seqNo", tempInt);
		int eraseOrignResult = mgr.bbsEraseTrash(mapEraseOrign);
		if (eraseOrignResult == 1) {
			int eraseTrashResult = mgr.bbsEraseTrash(mapEraseTrash);
			if (eraseTrashResult == 1) {
				sendMessage("�����Ͽ����ϴ�.");
			} else {
				sendMessage("������ �����Ͽ����ϴ�.");
			}
		} else {
			sendMessage("������ �����Ͽ����ϴ�.");
		}
	}

	private void users_reqJoin() throws IOException { // ���Խ�û
		HashMap<String, Object> map13 = new HashMap<String, Object>();
		String temp = "";

		sendMessage("\n����Ͻ� ID�� �Է��ϼ���. 8�� �̳�. id�� ��ҹ��ڸ� �������� �ʽ��ϴ�.");
		while (true) {
			temp = br.readLine();
			if (temp.length() < 9 && temp.length() > 0) {
				String chkDupl = mgr.chkDupl(temp.toUpperCase());
				if (chkDupl == null) {
					chkDupl = mgr.chkDupl(temp.toLowerCase());
				}
				if (chkDupl == null) {
					String reqJoinId = temp.toUpperCase();
					map13.put("userId", reqJoinId.toUpperCase());
					temp = "";
				} else {
					sendMessage("�ߺ��� ID�� �ֽ��ϴ�. �ٽ� �Է��Ͻʽÿ�.");
					continue;
				}
				break;
			} else {
				sendMessage("���ǿ� �°� �ٽ� �Է��Ͻʽÿ�.");
			}
		}

		sendMessage("\n����Ͻ� ��й�ȣ�� �Է��ϼ���. 11�� �̳�.");
		while (true) {
			temp = br.readLine();
			if (temp.length() < 12 && temp.length() > 0) {
				String reqJoinPw = temp;
				map13.put("userPw", reqJoinPw);
				temp = "";
				break;
			} else {
				sendMessage("���ǿ� �°� �ٽ� �Է��Ͻʽÿ�.");
			}
		}

		sendMessage("\n����Ͻ� ��й�ȣ�� �ٽ� �Է��ϼ���.");
		while (true) {
			temp = br.readLine();
			if (map13.get("userPw").equals(temp)) {
				sendMessage("Ȯ�εǾ����ϴ�.");
				temp = "";
				break;
			} else {
				sendMessage("�Է��Ͻ� ��й�ȣ�� �������� �ʽ��ϴ�. �ٽ� �Է��Ͻʽÿ�.");
			}
		}

		sendMessage("\n����Ͻ� �̸��� �Է��ϼ���. 4�� �̳�.");
		while (true) {
			temp = br.readLine();
			if (temp.length() < 5 && temp.length() > 0) {
				String reqJoinName = temp.toUpperCase();
				map13.put("userName", reqJoinName.toUpperCase());
				temp = "";
				break;
			} else {
				sendMessage("���ǿ� �°� �ٽ� �Է��Ͻʽÿ�.");
			}
		}

		sendMessage("\n����ó�� �Է��ϼ���. 20�� �̳��� �Է��Ͻʽÿ�.");
		while (true) {
			temp = br.readLine();
			if (temp.length() < 21 && temp.length() > 0) {
				String reqJoinPhone = temp.toUpperCase();
				map13.put("userPhone", reqJoinPhone.toUpperCase());
				temp = "";
				break;
			} else {
				sendMessage("���ǿ� �°� �ٽ� �Է��Ͻʽÿ�.");
			}
		}

		sendMessage("\n�ڱ�Ұ��� �Է��ϼ���.");
		while (true) {
			temp = br.readLine();
			if (temp.length() < 21 && temp.length() > 0) {
				String reqJoinPf = temp;
				map13.put("userPf", reqJoinPf);
				temp = "";
				break;
			} else {
				sendMessage("���ǿ� �°� �ٽ� �Է��Ͻʽÿ�.");
			}
		}

		sendMessage("\n\n�ۼ��� ������ �����Ͻðڽ��ϱ�? (Y/n)");
		String str = br.readLine();
		if (str.equalsIgnoreCase("n")) {
		} else {
			int result = mgr.reqJoin(map13);
			if (result == 1) {
				sendMessage("���Խ�û�߽��ϴ�.");
			} else {
				sendMessage("���Խ�û�� �����߽��ϴ�.");
			}
		}
		current_user_locat = "999";
	}

	private void users_reqLeave() throws IOException { // Ż���û
		sendMessage("���� Ż���û �Ͻðڽ��ϱ�? (y/N)");
		String yn18 = br.readLine();
		if (yn18.equalsIgnoreCase("y")) {
			sendMessage("���� Ż���Ͻ÷��� \"����Ż��\" �� �Է��Ͻʽÿ�.");
			String leaveNow = br.readLine();
			if (leaveNow.equalsIgnoreCase("����Ż��")) {
				int leaveNowResult = mgr.reqLeave(current_user_id);
				if (leaveNowResult == 1) {
					sendMessage("Ż���û�� ���� ó���Ǿ����ϴ�. �̿��� �ּż� �����մϴ�.");
				} else {
					sendMessage("Ż���û ó���� ������ �߻��߽��ϴ�. �ٽ� �õ��Ͽ� �ֽʽÿ�.");
				}
			} else {
				sendMessage("Ż���û�� ����մϴ�.");
			}
		} else {
			sendMessage("Ż���û�� ����մϴ�.");
		}
	}

	private void users_adjAuth(String current_user_locat, int selectUserIdNo) throws IOException { // ���Խ�û ����
		HashMap<String, Object> map = new HashMap<String, Object>();
		int setUserAuth = 0;

		// 997���� ���� = ����2(���Խ�û��)�� 3(�Ϲ�ȸ��)���� �±�
		if (mgr.chkAuthByIdNo(selectUserIdNo) == 2) {
			setUserAuth = 3;

			map.put("userIdNo", selectUserIdNo);
			map.put("userAuth", setUserAuth);

			int result = mgr.adjAuthByIdNo(map);
			if (result == 1) {
				sendMessage("�±�ó���Ͽ����ϴ�.");
			} else {
				sendMessage("�±�ó���� �����߽��ϴ�.");
			}
		} else {
			sendMessage("�±��� �Ұ����� ȸ�������Դϴ�. Ȯ�� �� �ٽ� �õ��Ͻʽÿ�.");
		}
	}

	private void users_removeUser(int selectUserIdNo) { // Ż���û ����
		if (mgr.chkAuthByIdNo(selectUserIdNo) == 0) {
			int result = mgr.removeUser(selectUserIdNo);
			if (result == 1) {
				sendMessage("Ż��(����)ó���Ͽ����ϴ�.");
			} else {
				sendMessage("Ż��(����)ó���� �����߽��ϴ�.");
			}
		} else {
			sendMessage("Ż��ó���� �Ұ����� ȸ�������Դϴ�. Ȯ�� �� �ٽ� �õ��Ͻʽÿ�.");
		}
	}

	private void chatRoom(String current_user_id) throws IOException { // ä�ù� ���
		if (current_user_chat_status) {
			sendMessage("ä�ù濡 �����ϼ̽��ϴ�. �����Ƕ��� \'quit\' �Ǵ� \'exit\' �� �Է��ϼ���.");
			chatMsg(current_user_id + "�Բ��� ä�ù濡 �����ϼ̽��ϴ�.");
			while (true) {
				br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				String str = br.readLine();
				if (str.equalsIgnoreCase("quit") || str.equalsIgnoreCase("exit")) {
					str = null;
					chatMsg(current_user_id + "�Բ��� ä�ù濡�� �����̽��ϴ�.");
					sendMessage("ä�ù濡�� �����ϴ�. [Enter]Ű�� �Է��ϼ���.");
					break;
				} else {
					chatMsg(str);
					str = null;
				}
			}
		} else {
			sendMessage("ä�ù濡 ������ �� �����ϴ�.");
		}
		return;
	}
	// ���⿡�� ��
} // class
