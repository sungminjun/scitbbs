package server.MGR;

import java.util.ArrayList;
import java.util.HashMap;

import server.DAO.BoardDAO;
import server.VO.BoardVO;
import server.VO.UsersVO;

public class BbsManager {

	BoardDAO dao = new BoardDAO();

	public boolean reqLogIn(String[] reqLogIn) {
		boolean result = false;
		String rcvId = reqLogIn[0];
		String rcvPw = reqLogIn[1];

		HashMap<String, Object> reqLogInMap = new HashMap<String, Object>();
		reqLogInMap.put("userId", rcvId.toUpperCase());
		reqLogInMap.put("userPw", rcvPw);
		result = dao.login(reqLogInMap);
		return result;
	}

	public int chkAuth(String rcvId) {
		int result = 0;
		result = dao.chkAuth(rcvId);
		return result;
	}

	public int chkAuthByIdNo(int rcvIdNo) {
		int result = 0;
		result = dao.chkAuth(rcvIdNo);
		return result;
	}

	public ArrayList<BoardVO> bbsList(HashMap<String, Object> map1) {
		ArrayList<BoardVO> result = new ArrayList<BoardVO>();
		result = dao.bbsList(map1);
		return result;
	}

	public String idName(String writeId) {
		String result = "";
		result = dao.idName(writeId);
		return result;
	}

	public int bbsWrite(HashMap<String, Object> map) {
		int result = 0;
		result = dao.bbsWrite(map);
		return result;
	}

	public BoardVO bbsRead(HashMap<String, Object> map) {
		BoardVO result = null;
		result = dao.bbsRead(map);
		return result;
	}

	public int bbsReadCnt(BoardVO temp) {
		int result = 0;
		result = dao.bbsReadCnt(temp);
		return result;
	}

	public int bbsDelete(HashMap<String, Object> map) {
		int result = 0;
		result = dao.bbsDelete(map);
		return result;
	}

	public int bbsRenameTrash(HashMap<String, Object> map) {
		int result = 0;
		result = dao.bbsRenameTrash(map);
		return result;
	}

	public int bbsEraseTrash(HashMap<String, Object> map) {
		int result = 0;
		result = dao.bbsEraseTrash(map);
		return result;
	}

	public BoardVO recentNotice() {
		BoardVO result = null;
		result = dao.recentNotice();
		return result;
	}
	

	public ArrayList<BoardVO> recentBoard() {
		ArrayList<BoardVO> result = null;
		result = dao.recentBoard();
		return result;
	}
	

	public String chkDupl(String chkDupl) {
		String result = "";
		result = dao.chkDupl(chkDupl);
		return result;
	}

	public int reqJoin(HashMap<String, Object> map) {
		int result = 0;
		result = dao.reqJoin(map);
		return result;
	}

	public int reqLeave(String current_user_id) {
		int result = 0;
		result = dao.reqLeave(current_user_id);
		return result;
	}

	public ArrayList<UsersVO> listUser(int i) {
		ArrayList<UsersVO> result = new ArrayList<UsersVO>();
		result = dao.listUser(i);
		return result;
	}

	public ArrayList<UsersVO> listAllUser() {
		ArrayList<UsersVO> result = new ArrayList<UsersVO>();
		result = dao.listAllUser();
		return result;
	}

	public int adjAuth(HashMap<String, Object> map) {
		int result = 0;
		result = dao.adjAuth(map);
		return result;
	}

	public int adjAuthByIdNo(HashMap<String, Object> map) {
		int result = 0;
		result = dao.adjAuthByIdNo(map);
		return result;
	}

	public int removeUser(int selectUserIdNo) {
		int result = 0;
		result = dao.removeUser(selectUserIdNo);
		return result;
	}

	public int updPf(HashMap<String, Object> map17) {
		int result = 0;
		result = dao.updPf(map17);
		return result;
	}


}
