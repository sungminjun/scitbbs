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
//	private String current_user_chat_room_no;	// 방번호, 채팅방을 여러개 만들 떄 쓰려고 했으나 폐기
//	private String current_user_chat_room_status;	// 방장권한, kick이나 채팅방 제목을 붙일 때 쓰려고 했으나 폐기

	BbsManager mgr = new BbsManager();	

	//
	//
	// 기본 서버 작동 관련
	public Bbs_work(Socket socket, ArrayList<Bbs_work> list) throws IOException { // 기본 client응대 서버 생성자
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

	public void work() throws Exception { // 쓰레드 내에서 일하는 메소드
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
				sendMessage("명령어를 확인할 수 없습니다.");
			}
		}

	}

	@Override
	public void run() { // 쓰레드 돌아감
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

	public void closeAll() throws IOException { // pw, br, socket을 닫고 접속을 종료함.
		if (pw != null)
			pw.close();
		if (br != null)
			br.close();
		if (socket != null)
			socket.close();
	}

	public void sendMessage(String message) { // client에 전송하고 싶은 String을 보냄 (sysout)
		pw.println(message);
		pw.flush();
	}

	public void broadcasting(String message) { // 전체 공지용 broadcasting 메소드, 모든 접속에 메세지를 보낸다.
		for (Bbs_work serverWorker : list) {
			serverWorker.sendMessage(message);
		}
	}

	public void chatMsg(String message) { // 채팅방용 chatMsg메소드, 채팅방에 접속한 인원에게만 메세지를 보낸다.
		for (Bbs_work chat_true : list) {
			if (chat_true.current_user_chat_status) {
				chat_true.sendMessage("[" + current_user_id + "(" + mgr.idName(current_user_id) + ") \t]  " + message);
			}
		}
	}

	public void whisper(String message, String rcvId) { // 쪽지(귓속말)용 메소드, 수신자ID와 일치하는 경우에만 메세지를 보낸다.
		boolean sentWhisperStatus = false;
		for (Bbs_work whisper : list) {
			if (whisper.current_user_id.equalsIgnoreCase(rcvId)) {
				whisper.sendMessage(
						"[" + current_user_id + "(" + mgr.idName(current_user_id) + ")(으)로부터 당신에게 귓속말 \t]  " + message);
				sentWhisperStatus = true;
			}
		}
		if (sentWhisperStatus) {
			for (Bbs_work whisper : list) {
				if (whisper.current_user_id.equalsIgnoreCase(current_user_id)) {
					whisper.sendMessage("[" + rcvId + "(" + mgr.idName(rcvId) + ")에게 귓속말 \t]  " + message);
				}
			}
		} else {
			sendMessage("대상이 없습니다.");
		}
	}

	public void removeConnection(Bbs_work worker) { // 연결 종료, 커넥션 회수 사용
		list.remove(worker);
//		broadcasting(socket.getInetAddress() + "disconnected.");
	}

	//
	//
	// 입력분기에 따른 ui 송출용
	private void userInterface(String current_user_locat) throws IOException { // 위치정보에 따른 페이지를 보여주도록 한다.
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
	// 입력분기처리용
	private void userInputResponse(String userInput) throws IOException { // 사용자로부터 입력 받은 내용으로 명령어 수행 판단
		Integer tempInt;
		System.out.println(current_user_id + " press : " + userInput + " from " + socket.getInetAddress());
		// user권한 체크 후 사용 명령어
		if (current_user_auth >= 5 && userInput.equals("TERMINATE_THIS_SERVER")) {
			terminateServer();

			// 전역, 전권한 공통 명령어 T, X, C, GO xxx, Memo <somebody> <contents>
		} else if (userInput.equals("")) { // 공백 (엔터) 입력 신호시 아무 행동도 하지 않는다.
		} else if (userInput.equalsIgnoreCase("T") || current_user_locat.equalsIgnoreCase("TOP")
				|| current_user_locat.equalsIgnoreCase("ㅅ")) { // 초기화면으로 이동
			current_user_locat_prev = current_user_locat;
			current_user_locat = "000";
		} else if (userInput.equalsIgnoreCase("X") || current_user_locat.equalsIgnoreCase("EXIT")
				|| current_user_locat.equalsIgnoreCase("QUIT") || userInput.equalsIgnoreCase("ㅌ")) { // 종료요청
			closeConn();
		} else if (userInput.equalsIgnoreCase("C") || userInput.equalsIgnoreCase("HELP")
				|| userInput.equalsIgnoreCase("ㅊ")) { // 명령어조회화면 이동
			help();
		} else if (userInput.equalsIgnoreCase("P") || userInput.equals("ㅔ")) { // 이전화면으로
			current_user_locat = current_user_locat_prev;
		} else if (userInput.equalsIgnoreCase("pf")) {
			sendMessage("권한 확인 [userId : " + current_user_id + ", 권한:" + current_user_auth + ", 위치:"
					+ current_user_locat + "] 입니다.");
			if (current_user_auth >= 5) {
				sendMessage("사용가능 운영자 명령어 : TERMINATE_THIS_SERVER");
			}
			// 미구현상황. 타인 id에 대한 정보를 get 할것인지 말건지 검토

			//
			// TOP화면
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

			// 회원가입 메뉴 화면
		} else if (current_user_locat.equals("002") && userInput.equalsIgnoreCase("1")) { // guest가입신청, 일반 탈퇴신청, 관리자
			current_user_locat_prev = current_user_locat;
			current_user_locat = "998";

		} else if (current_user_locat.equals("002") && userInput.equalsIgnoreCase("2") && current_user_auth >= 5) { // 관리자
			current_user_locat_prev = current_user_locat;
			current_user_locat = "996";

		} else if (current_user_locat.equals("002") && userInput.equalsIgnoreCase("2") && current_user_auth < 5
				&& current_user_auth >= 3) { // 관리자 탈퇴승인
			current_user_locat_prev = current_user_locat;
			current_user_locat = "994";

		} else if (current_user_locat.equals("002") && userInput.equalsIgnoreCase("11")) { // 관리자 전체회원조회
			current_user_locat_prev = current_user_locat;
			current_user_locat = "995";

			// 게시판 화면, 글쓰기 입력받기
		} else if ((current_user_locat.equals("001") || current_user_locat.equals("011")
				|| current_user_locat.equals("012")) && userInput.equalsIgnoreCase("W")) {
			if (current_user_auth >= 3) {
				bbs_write(current_user_locat);
			} else {
				sendMessage("손님은 사용할 수 없는 기능입니다.");
			}

			// 관리페이지, 회원승급(가입신청 승인) 또는 탈퇴 처리 시 시 parseInt
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

			// 게시판 메뉴에서만 적용, parseInt 처리가 문제
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
				sendMessage("손님은 사용할 수 없는 기능입니다.");
			}
		} else if ((userInput.startsWith("d ") || userInput.startsWith("D ") && (current_user_locat.equals("001")
				|| current_user_locat.equals("011") || current_user_locat.equals("012")))) {
			if (current_user_auth >= 3) {
				tempInt = Integer.parseInt(userInput.substring(2));
				if (tempInt != null) {
					bbs_delete(current_user_locat, tempInt);
				}
			} else {
				sendMessage("손님은 사용할 수 없는 기능입니다.");
			}
		} else if ((current_user_locat.equals("001") || current_user_locat.equals("011")
				|| current_user_locat.equals("012") || current_user_locat.equals("013"))) {
			if (current_user_auth >= 3) {
				tempInt = Integer.parseInt(userInput);
				if (tempInt != null) {
					bbs_read(current_user_locat, tempInt);
				}
			} else {
				sendMessage("손님은 사용할 수 없는 기능입니다.");
			}

			// 그 외 입력내용에 대해서
		} else {
			sendMessage("명령어를 확인할 수 없습니다. 다시 입력해주세요.");
			userInput = null;
		}
	}

	//
	//
	// 커넥션 종료 및 서버 원격종료용
	private void terminateConn() throws IOException { // 소켓 끈다.
		sendMessage("접속을 종료합니다.");
		socket.close();
	}

	private void terminateServer() throws IOException { // 서버 끄는 기능
		removeConnection(this);
		closeAll();
		System.exit(0);
	}

	//
	//
	// 이하 개별 화면 송출 및 기능 메소드
	private void help() { // 명령어 도움말 보여주는 화면
		sendMessage("SCITBBS ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
		sendMessage(" HELP                        도 움 말               http://www.itmasters.org ");
		sendMessage("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
		sendMessage("\n\t[   기본명령어   ]\t\t[   게시판명령어   ]");
		sendMessage("\n\t (TOP) 초기 화면으로 이동 \t(번호) 해당 번호의 글을 읽습니다.");
		sendMessage("\n\t (C, HELP) 명령어 목록 조회\t(W) 새로운 글을 작성");
		sendMessage("\n\t (X, QUIT) 접속 종료 \t\t\t(D) 글의 삭제를 요청");
		sendMessage("\n\t (P) 이전 화면으로 이동 ");
		sendMessage("\n\t (TO ID 내용) id에게 귓속말");
		sendMessage("\n\t (MEMO ID 내용) id에게 귓속말");
		sendMessage("\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
	}

	private void showMenu999() { // login 마중화면
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
		sendMessage("           _____________________________________________へ ようこそ____");
		sendMessage("[안내] 처음 오신분은 guest 를 입력하여 회원 가입을 진행하세요.");
	}

	private void showMenu000() { // TOP
		BoardVO rNotice = mgr.recentNotice();
		ArrayList<BoardVO> rBoard = mgr.recentBoard();

		sendMessage("SCITBBS ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
		sendMessage(" TOP                         초기화면               http://www.itmasters.org ");
		sendMessage("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

		if (current_user_auth == 1) {
			sendMessage("\n\t[  정보관리  ]\t 1. 공지사항 \t  2. 가입신청\t");
			sendMessage("\n\t[  커뮤니티  ]\t11. 자유게시판\t 12. 익명게시판");
		} else if (current_user_auth >= 3 && current_user_auth < 5) {
			sendMessage("\n\t[  정보관리  ]\t 1. 공지사항 \t  2. 개인정보관리\t");
			sendMessage("\n\t[  커뮤니티  ]\t11. 자유게시판\t 12. 익명게시판");
		} else if (current_user_auth >= 5) {
			sendMessage("\n\t[  정보관리  ]\t 1. 공지사항 \t  2. 가입신청/회원관리\t");
			sendMessage("\n\t[  커뮤니티  ]\t11. 자유게시판\t 12. 익명게시판\n\t\t\t13. 삭제요청관리");
		}
		sendMessage("\n\t[  부가기능  ]\t21. 대 화 방 \t 22. 게임(미구현)\t");
		sendMessage("\n\t[  공지사항  ]\t");
		if (rNotice == null) {
			sendMessage("\t\t\t최근 3주 내 공지사항이 없습니다.");
		} else {
			sendMessage("\t공지사항 게시판 " + rNotice.getSeqNo() + "번글 [" + rNotice.getSubj() + "]이(가) 있습니다.");
		}

		sendMessage("\n\t[  최 신 글  ]\t");
		if (rBoard.size() == 0) {
			sendMessage("\t자유게시판에 최근 3일내 게시물이 없습니다.");
			sendMessage("\t익명게시판에 최근 3일내 게시물이 없습니다.");
		} else if (rBoard.size() == 1 && rBoard.get(0).getBoardName().equalsIgnoreCase("boardfree")) {
			sendMessage("\t자유게시판 " + rBoard.get(0).getSeqNo() + "번 글 [" + rBoard.get(0).getSubj() + "] 이 있습니다.");
			sendMessage("\t익명게시판에 최근 3일내 게시물이 없습니다.");
		} else if (rBoard.size() == 1 && rBoard.get(0).getBoardName().equalsIgnoreCase("boardanony")) {
			sendMessage("\t자유게시판 " + rBoard.get(0).getSeqNo() + "번 글 [" + rBoard.get(0).getSubj() + "] 이 있습니다.");
			sendMessage("\t익명게시판에 최근 3일내 게시물이 없습니다.");
		} else if (rBoard.size() == 2) {
			if (rBoard.get(0).getBoardName().equals("boardfree")) {
				sendMessage("\t자유게시판 " + rBoard.get(0).getSeqNo() + "번 글 [" + rBoard.get(0).getSubj() + "] 이 있습니다.");
				sendMessage("\t익명게시판 " + rBoard.get(1).getSeqNo() + "번 글 [" + rBoard.get(1).getSubj() + "] 이 있습니다.");
			} else {
				sendMessage("\t자유게시판 " + rBoard.get(1).getSeqNo() + "번 글 [" + rBoard.get(1).getSubj() + "] 이 있습니다.");
				sendMessage("\t익명게시판 " + rBoard.get(0).getSeqNo() + "번 글 [" + rBoard.get(0).getSubj() + "] 이 있습니다.");
			}
		} else {
			sendMessage("\t\t자유게시판의 최신 게시물 정보를 가져오는데 실패했습니다.");
			sendMessage("\t\t익명게시판의 최신 게시물 정보를 가져오는데 실패했습니다.");
		}

		sendMessage("\n\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
		sendMessage("명령어안내(C) 직접이동(GO) 종료(X)\n선택> ");
	}

	private void showMenu001() { // NOTICE
		sendMessage("SCITBBS ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
		sendMessage(" NOTICE                      공지사항               http://www.itmasters.org ");
		sendMessage("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
		sendMessage("번호\t  작 성 자\t조회\t\t	제	목");
		sendMessage("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

		HashMap<String, Object> map1 = new HashMap<String, Object>();
		map1.put("boardName", "boardnotice");
		ArrayList<BoardVO> boardNotice = mgr.bbsList(map1);
		for (int i = 0; i < boardNotice.size(); i++) {
			sendMessage(boardNotice.get(i).getSeqNo() + "\t" + boardNotice.get(i).getWriteId() + "("
					+ mgr.idName(boardNotice.get(i).getWriteId()) + ")\t" + boardNotice.get(i).getCounter() + "\t"
					+ boardNotice.get(i).getSubj());
		}
		sendMessage("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
		sendMessage("명령어안내(C) 직접이동(GO) 종료(X)\n선택> ");
	}

	private void showMenu002() { // 2번메뉴. 권한에 따라 JOIN/MYPAGE/USERS 메뉴로 보인다.
		sendMessage("SCITBBS ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
		if (current_user_auth == 1) {
			sendMessage(" JOIN                        가입신청               http://www.itmasters.org ");
			sendMessage("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
			sendMessage("\n\n\n\t\t 1. 가입신청\n\n\n\n");
		} else if (current_user_auth >= 3 && current_user_auth < 5) {
			sendMessage(" MYPAGE                    개인정보관리             http://www.itmasters.org ");
			sendMessage("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
			sendMessage("\n\n\n\t\t  1. 탈퇴신청 \t  2. 프로필 수정\n\n\n\n");
		} else if (current_user_auth >= 5) {
			sendMessage(" USERS                   가입신청/회원관리          http://www.itmasters.org ");
			sendMessage("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
			sendMessage("\n\n\t\t  1. 가입신청관리 \t  2. 탈퇴신청관리\n\n\t\t 11. 전체회원조회\t\n\n");
		}
		sendMessage("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
		sendMessage("명령어안내(C) 직접이동(GO) 종료(X)\n선택> ");
	}

	private void showMenu011() { // Free
		sendMessage("SCITBBS ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
		sendMessage(" FREE                       자유게시판              http://www.itmasters.org ");
		sendMessage("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
		sendMessage("번호\t  작 성 자\t조회\t\t	제	목");

		HashMap<String, Object> map1 = new HashMap<String, Object>();
		map1.put("boardName", "boardfree");
		ArrayList<BoardVO> boardNotice = mgr.bbsList(map1);
		for (int i = 0; i < boardNotice.size(); i++) {
			sendMessage(boardNotice.get(i).getSeqNo() + "\t" + boardNotice.get(i).getWriteId() + "("
					+ mgr.idName(boardNotice.get(i).getWriteId()) + ")\t" + boardNotice.get(i).getCounter() + "\t"
					+ boardNotice.get(i).getSubj());
		}
		sendMessage("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
		sendMessage("명령어안내(C) 직접이동(GO) 종료(X)\n선택> ");
	}

	private void showMenu012() { // Anony
		sendMessage("SCITBBS ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
		sendMessage(" ANONY                      익명게시판               http://www.itmasters.org ");
		sendMessage("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
		sendMessage("번호\t조회\t\t	제	목");

		HashMap<String, Object> map1 = new HashMap<String, Object>();
		map1.put("boardName", "boardanony");
		ArrayList<BoardVO> boardNotice = mgr.bbsList(map1);
		for (int i = 0; i < boardNotice.size(); i++) {
			sendMessage(boardNotice.get(i).getSeqNo() + "\t" + boardNotice.get(i).getCounter() + "\t"
					+ boardNotice.get(i).getSubj());
		}
		sendMessage("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
		sendMessage("명령어안내(C) 직접이동(GO) 종료(X)\n선택> ");
	}

	private void showMenu013() { // trash
		sendMessage("SCITBBS ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
		sendMessage(" TRASH                    삭제게시물관리             http://www.itmasters.org ");
		sendMessage("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
		sendMessage("번호\t조회\t\t	제	목");

		HashMap<String, Object> map1 = new HashMap<String, Object>();
		map1.put("boardName", "boardtrash");
		ArrayList<BoardVO> boardNotice = mgr.bbsList(map1);
		for (int i = 0; i < boardNotice.size(); i++) {
			sendMessage(boardNotice.get(i).getSeqNo() + "\t" + boardNotice.get(i).getCounter() + "\t"
					+ boardNotice.get(i).getSubj());
		}

		sendMessage("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
		sendMessage("명령어안내(C) 직접이동(GO) 종료(X)\n선택> ");
	}

	private void showMenu021() throws IOException { // enter chatRoom
		current_user_chat_status = true;
		chatRoom(current_user_id);
		current_user_chat_status = false;
		current_user_locat = "000";
	}

	private void showMenu994() throws IOException { // 2번메뉴의 2번 (일반유저) 프로필 수정
		HashMap<String, Object> map17 = new HashMap<String, Object>();
		map17.put("userId", current_user_id);

		sendMessage("수정할 프로필을 입력하십시오.");
		String i17 = br.readLine();
		map17.put("userPf", i17);

		int i17result = mgr.updPf(map17);
		if (i17result == 1) {
			sendMessage("수정처리 되었습니다.");
		} else {
			sendMessage("수정처리에 실패하였습니다.");
		}
		current_user_locat = "000";
	}

	private void showMenu995() { // 2번메뉴의 11번 (관리자전용) 전체회원조회
		sendMessage("SCITBBS ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
		sendMessage(" -MGR-                    전체 회원 조회             http://www.itmasters.org ");
		sendMessage("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
		sendMessage("번호\t  I D\t\t 이름\t\t자 기 소 개");
		sendMessage("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
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
		sendMessage("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
		sendMessage("명령어안내(C) 직접이동(GO) 종료(X)\n선택> ");
	}

	private void showMenu996() { // 2번메뉴의 2번 (관리자전용) 탈퇴요청자관리
		sendMessage("SCITBBS ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
		sendMessage(" -MGR-                    탈퇴 요청 관리             http://www.itmasters.org ");
		sendMessage("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
		sendMessage("번호\tID\t이름\t연락처\t자기소개");
		sendMessage("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
		ArrayList<UsersVO> apprLeave = mgr.listUser(0);
		for (UsersVO u : apprLeave) {
			sendMessage(u.getUserIdNo() + "\t" + u.getUserId() + "\t" + u.getUserIdNo() + "\t" + u.getUserPhone() + "\t"
					+ u.getUserPf());
		}
		sendMessage("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
		sendMessage("명령어안내(C) 직접이동(GO) 종료(X)\n선택> ");
	}

	private void showMenu997() { // 2번메뉴의 1번 (관리자전용) 가입요청자관리
		current_user_locat = "997";
		sendMessage("SCITBBS ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
		sendMessage(" -MGR-                    가입 요청 관리             http://www.itmasters.org ");
		sendMessage("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
		sendMessage("번호\tID\t\t이름\t\t연락처\t\t자 기 소 개");
		sendMessage("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
		ArrayList<UsersVO> apprJoin = mgr.listUser(2);
		for (UsersVO u : apprJoin) {
			sendMessage(u.getUserIdNo() + "\t" + u.getUserId() + "\t" + u.getUserName() + "\t" + u.getUserPhone() + "\t"
					+ u.getUserPf());
		}
		sendMessage("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
		sendMessage("명령어안내(C) 직접이동(GO) 종료(X)\n선택> ");
	}

	private void showMenu998() throws IOException { // 2번메뉴의 1번, 가입/탈퇴/관리메뉴 진입분기
		if (current_user_auth == 1) {
			// 가입신청
			users_reqJoin();
			current_user_locat = "002";
			showMenu002();

		} else if (current_user_auth >= 3 && current_user_auth < 5) {
			// 탈퇴신청
			users_reqLeave();
			current_user_locat = "002";
			showMenu002();

		} else if (current_user_auth >= 5) {
			// 가입신청 관리
			showMenu997();
		}
	}

	//
	// 기능 메소드들
	private String[] rcvLogin() throws IOException { // 추후에 member 객체와 비교하도록
		String[] result = new String[2];
		String id = "";
		String pw = "";

		// id입력
		while (true) {
			sendMessage("ID를 입력하세요 : ");
			id = br.readLine().toUpperCase();

			if (id.equalsIgnoreCase("guest")) {
				pw = "1234";
			} else if (id.length() > 8) {
				sendMessage("다시 입력하세요");
				continue;
			} else if (id.equals("")) {
				sendMessage("다시 입력하세요");
				continue;
			}
			break;
		}

		// pw입력
		while (true) {
			if (pw.equals("")) {
				sendMessage("비밀번호를 입력하세요 : ");
				pw = br.readLine();
				if (pw.length() > 11) {
					sendMessage("다시 입력하세요");
					pw = "";
					continue;
				} else {
					break;
				}
			} else {
				break;
			}
		}

		// 리턴처리
		result[0] = id;
		result[1] = pw;
		return result;
	}

	private boolean requestLogIn(String[] reqLogIn) throws IOException { // 입력받은 내용으로 로그인 점검
		boolean reqResult = false;
		reqResult = mgr.reqLogIn(reqLogIn);

		if (reqResult) {
			String rcvId = reqLogIn[0];
			current_user_id = rcvId;
			current_user_auth = mgr.chkAuth(current_user_id);
			current_user_locat = "000";
			current_user_chat_status = false;
			sendMessage(current_user_id + "님, 환영합니다. \n[Enter]를 입력하세요.");
		} else {
			sendMessage("잘못입력하셨습니다. 확인하시고 다시 시도하십시오.\n연결을 종료합니다.");
			terminateConn();
			removeConnection(this);
		}

		if (current_user_auth == 0) {
			sendMessage("탈퇴신청중인 회원입니다.");
			reqResult = false;
			sendMessage("잘못입력하셨습니다. 확인하시고 다시 시도하십시오.\n연결을 종료합니다.");
			terminateConn();
			removeConnection(this);
		} else if (current_user_auth == 2) {
			sendMessage("가입신청중인 회원입니다. 아직 승인되지 않았습니다.");
			reqResult = false;
			sendMessage("잘못입력하셨습니다. 확인하시고 다시 시도하십시오.\n연결을 종료합니다.");
			terminateConn();
			removeConnection(this);
		}
		return reqResult;
	}

	private void closeConn() throws IOException { // 종료 확인 물어보고 끈다.
		sendMessage(current_user_id + "님, 종료하시겠습니까? (Y/n)");
		String str = br.readLine();
		if (str.equals("N") || str.equals("n")) {
		} else {
			terminateConn();
		}
		return;
	}

	private void bbs_write(String current_user_locat) throws IOException { // 게시판에 글을 쓴다. @param 사용자위치
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
				sendMessage("에러가 발생했습니다. 권한을 확인하고 다시 시도하세요.");
				break;
			}
			boardName = boardName.toUpperCase();
			map.put("boardName", boardName); // 서버단에서 자동으로 받을 것
			map.put("seqNo", seqNo);
			map.put("case1", boardName.toUpperCase()); // 같은 변수 두번 끌어오기 안되는 듯 하여
			map.put("writeId", current_user_id); // 서버단에서 자동으로 받을 것

			String subj = "";
			String content = "";

			sendMessage("제목을 입력하세요 : ");
			while (true) {
				String temp = br.readLine();
				subj = temp;
				if (!temp.equals("") && temp.length() < 80) {
					break;
				} else if (temp.length() >= 80) {
					sendMessage("길이가 너무 깁니다. 다시 입력해주세요.");
				} else if (temp.equals("")) {
					sendMessage("내용이 없습니다. 다시 입력해주세요.");
				}
			}

			sendMessage("내용을 입력하세요 : (작성을 마치려면 줄 처음에 \"끝\" 이라고 입력해주세요");
			while (true) {
				String temp = br.readLine();
				if (temp.equals("끝") && content.length() < 1600) {
					break;
				} else if (temp.equals("")) {
					temp = "\n\n";
				}

				content += temp;

				if (content.length() >= 1600) {
					sendMessage("(system) 길이가 너무 깁니다. 작성을 종료합니다.");
					break;
				} else if (content.equals("")) {
					sendMessage("(system) 내용이 없습니다. 다시 입력해주세요.");
				}
			}

			sendMessage("작성한 내용을 전송하시겠습니까? (Y/n)");
			String str = br.readLine();
			if (str.equalsIgnoreCase("n")) {
			} else {
				map.put("subj", subj);
				map.put("content", content);
				int result = mgr.bbsWrite(map);
				map.clear();
				if (result == 1) {
					sendMessage("등록했습니다.");
				} else {
					sendMessage("등록에 실패했습니다.");
				}
			}
			break;
		}
	}

	private void bbs_read(String current_user_locat, int seqNo) { // 특정 게시판의 글을 읽는다. @param 사용자위치, 글번호
		HashMap<String, Object> map = new HashMap<String, Object>();
		String boardName = "";
		if (current_user_locat.equals("001")) {
			boardName = "boardnotice";
		} else if (current_user_locat.equals("011")) {
			boardName = "boardfree";
		} else if (current_user_locat.equals("012")) {
			boardName = "boardanony";
		} else {
			sendMessage("에러가 발생했습니다. 권한을 확인하고 다시 시도하세요.");
		}
		boardName = boardName.toUpperCase();
		map.put("boardName", boardName);
		map.put("seqNo", seqNo);

		BoardVO temp = mgr.bbsRead(map);
		if (temp != null) {
			int cnt = mgr.bbsReadCnt(temp);
			System.out.println("cnt status (0=fail, 1=cnt update done.) : " + cnt);
			System.out.println(temp);
			sendMessage("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

			if (boardName.equalsIgnoreCase("boardanony")) {
				sendMessage("    " + temp.getSeqNo() + ".\t" + temp.getSubj() + "\n  작성일시: " + temp.getWriteDate()
						+ "\t조회수: " + temp.getCounter());
			} else {
				sendMessage("    " + temp.getSeqNo() + ".\t" + temp.getSubj() + "\n  작성자: " + temp.getWriteId() + "("
						+ mgr.idName(temp.getWriteId()) + ")" + "\t작성일시: " + temp.getWriteDate() + "\t조회수: "
						+ temp.getCounter());
			}

			sendMessage("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
			sendMessage(temp.getContent());
			sendMessage("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
		} else {
			sendMessage("(system) 게시물을 불러오는 데 실패했습니다.");
		}
	}

	private void bbs_delete(String current_user_locat, int tempInt) { // 글 삭제요청을 한다. @param 사용자위치, 글번호
		HashMap<String, Object> map = new HashMap<String, Object>();
		String boardName = "";
		if (current_user_locat.equals("001") && current_user_auth >= 5) {
			boardName = "boardnotice";
		} else if (current_user_locat.equals("011")) {
			boardName = "boardfree";
		} else if (current_user_locat.equals("012")) {
			boardName = "boardanony";
		} else {
			sendMessage("(system) 에러가 발생했습니다. 권한을 확인하고 다시 시도하세요.");
		}
		boardName = boardName.toUpperCase();
		map.put("boardName", boardName);
		map.put("seqNo", tempInt);
		int deleteReqResult = mgr.bbsDelete(map);
		if (deleteReqResult == 1) {
			int renameResult = mgr.bbsRenameTrash(map);
			if (renameResult == 1) {
				sendMessage("삭제요청하였습니다.");
			} else {
				sendMessage("삭제요청에 실패하였습니다.");
			}
		} else {
			sendMessage("삭제요청에 실패하였습니다.");
		}
	}

	private void bbs_erase(String current_user_locat, int tempInt) { // 삭제요청된 글을 삭제한다. @param 사용자위치, 글번호
		HashMap<String, Object> mapRead = new HashMap<String, Object>();
		HashMap<String, Object> mapEraseOrign = new HashMap<String, Object>();
		HashMap<String, Object> mapEraseTrash = new HashMap<String, Object>();
		String boardName = "";
		if (current_user_locat.equals("013") && current_user_auth >= 5) {
			boardName = "boardtrash";
		} else {
			sendMessage("(system) 에러가 발생했습니다. 권한을 확인하고 다시 시도하세요.");
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
				sendMessage("삭제하였습니다.");
			} else {
				sendMessage("삭제에 실패하였습니다.");
			}
		} else {
			sendMessage("삭제에 실패하였습니다.");
		}
	}

	private void users_reqJoin() throws IOException { // 가입신청
		HashMap<String, Object> map13 = new HashMap<String, Object>();
		String temp = "";

		sendMessage("\n사용하실 ID를 입력하세요. 8자 이내. id는 대소문자를 구분하지 않습니다.");
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
					sendMessage("중복된 ID가 있습니다. 다시 입력하십시오.");
					continue;
				}
				break;
			} else {
				sendMessage("조건에 맞게 다시 입력하십시오.");
			}
		}

		sendMessage("\n사용하실 비밀번호를 입력하세요. 11자 이내.");
		while (true) {
			temp = br.readLine();
			if (temp.length() < 12 && temp.length() > 0) {
				String reqJoinPw = temp;
				map13.put("userPw", reqJoinPw);
				temp = "";
				break;
			} else {
				sendMessage("조건에 맞게 다시 입력하십시오.");
			}
		}

		sendMessage("\n사용하실 비밀번호를 다시 입력하세요.");
		while (true) {
			temp = br.readLine();
			if (map13.get("userPw").equals(temp)) {
				sendMessage("확인되었습니다.");
				temp = "";
				break;
			} else {
				sendMessage("입력하실 비밀번호가 동일하지 않습니다. 다시 입력하십시오.");
			}
		}

		sendMessage("\n사용하실 이름을 입력하세요. 4자 이내.");
		while (true) {
			temp = br.readLine();
			if (temp.length() < 5 && temp.length() > 0) {
				String reqJoinName = temp.toUpperCase();
				map13.put("userName", reqJoinName.toUpperCase());
				temp = "";
				break;
			} else {
				sendMessage("조건에 맞게 다시 입력하십시오.");
			}
		}

		sendMessage("\n연락처를 입력하세요. 20자 이내로 입력하십시오.");
		while (true) {
			temp = br.readLine();
			if (temp.length() < 21 && temp.length() > 0) {
				String reqJoinPhone = temp.toUpperCase();
				map13.put("userPhone", reqJoinPhone.toUpperCase());
				temp = "";
				break;
			} else {
				sendMessage("조건에 맞게 다시 입력하십시오.");
			}
		}

		sendMessage("\n자기소개를 입력하세요.");
		while (true) {
			temp = br.readLine();
			if (temp.length() < 21 && temp.length() > 0) {
				String reqJoinPf = temp;
				map13.put("userPf", reqJoinPf);
				temp = "";
				break;
			} else {
				sendMessage("조건에 맞게 다시 입력하십시오.");
			}
		}

		sendMessage("\n\n작성한 내용을 전송하시겠습니까? (Y/n)");
		String str = br.readLine();
		if (str.equalsIgnoreCase("n")) {
		} else {
			int result = mgr.reqJoin(map13);
			if (result == 1) {
				sendMessage("가입신청했습니다.");
			} else {
				sendMessage("가입신청에 실패했습니다.");
			}
		}
		current_user_locat = "999";
	}

	private void users_reqLeave() throws IOException { // 탈퇴신청
		sendMessage("정말 탈퇴요청 하시겠습니까? (y/N)");
		String yn18 = br.readLine();
		if (yn18.equalsIgnoreCase("y")) {
			sendMessage("정말 탈퇴하시려면 \"지금탈퇴\" 를 입력하십시오.");
			String leaveNow = br.readLine();
			if (leaveNow.equalsIgnoreCase("지금탈퇴")) {
				int leaveNowResult = mgr.reqLeave(current_user_id);
				if (leaveNowResult == 1) {
					sendMessage("탈퇴요청이 정상 처리되었습니다. 이용해 주셔서 감사합니다.");
				} else {
					sendMessage("탈퇴요청 처리에 문제가 발생했습니다. 다시 시도하여 주십시오.");
				}
			} else {
				sendMessage("탈퇴요청을 취소합니다.");
			}
		} else {
			sendMessage("탈퇴요청을 취소합니다.");
		}
	}

	private void users_adjAuth(String current_user_locat, int selectUserIdNo) throws IOException { // 가입신청 승인
		HashMap<String, Object> map = new HashMap<String, Object>();
		int setUserAuth = 0;

		// 997에서 진입 = 레벨2(가입신청중)을 3(일반회원)으로 승급
		if (mgr.chkAuthByIdNo(selectUserIdNo) == 2) {
			setUserAuth = 3;

			map.put("userIdNo", selectUserIdNo);
			map.put("userAuth", setUserAuth);

			int result = mgr.adjAuthByIdNo(map);
			if (result == 1) {
				sendMessage("승급처리하였습니다.");
			} else {
				sendMessage("승급처리에 실패했습니다.");
			}
		} else {
			sendMessage("승급이 불가능한 회원레벨입니다. 확인 후 다시 시도하십시오.");
		}
	}

	private void users_removeUser(int selectUserIdNo) { // 탈퇴신청 승인
		if (mgr.chkAuthByIdNo(selectUserIdNo) == 0) {
			int result = mgr.removeUser(selectUserIdNo);
			if (result == 1) {
				sendMessage("탈퇴(삭제)처리하였습니다.");
			} else {
				sendMessage("탈퇴(삭제)처리에 실패했습니다.");
			}
		} else {
			sendMessage("탈퇴처리가 불가능한 회원레벨입니다. 확인 후 다시 시도하십시오.");
		}
	}

	private void chatRoom(String current_user_id) throws IOException { // 채팅방 기능
		if (current_user_chat_status) {
			sendMessage("채팅방에 입장하셨습니다. 나가실때는 \'quit\' 또는 \'exit\' 를 입력하세요.");
			chatMsg(current_user_id + "님께서 채팅방에 입장하셨습니다.");
			while (true) {
				br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				String str = br.readLine();
				if (str.equalsIgnoreCase("quit") || str.equalsIgnoreCase("exit")) {
					str = null;
					chatMsg(current_user_id + "님께서 채팅방에서 나가셨습니다.");
					sendMessage("채팅방에서 나갑니다. [Enter]키를 입력하세요.");
					break;
				} else {
					chatMsg(str);
					str = null;
				}
			}
		} else {
			sendMessage("채팅방에 입장할 수 없습니다.");
		}
		return;
	}
	// 여기에서 끝
} // class
