package server.DAO;

import java.util.ArrayList;
import java.util.HashMap;

import server.VO.BoardVO;
import server.VO.UsersVO;

public interface BbsMapper {
	// 해당vo mapper.xml 에 있는 sql에 연결된 id명과 같아야 한다.
	// 즉, 메소드 명과 같은 id명을 호출해 그 내용을 sql로 쏜다.

	// board, 즉 게시판 관련 method 우선

	/*
	 * 게시글 쓰기 기능 (insert)
	 * 
	 * @param UI단에서 입력받은 내용을 BoardVO객체 전송
	 * 
	 * @return 성공시 true, 실패시 false
	 */
	public int bbsWrite(HashMap<String, Object> map);

	/*
	 * 게시판 목록 불러오기 (select)
	 * 
	 * @param 게시판명을 db에 요청
	 * 
	 * @return BoardVO객체의 집합(arrayList)을 반환
	 */
	public ArrayList<BoardVO> bbsList(HashMap<String, Object> map);

	/*
	 * 게시판 글 읽기 (select(글읽기) 와 update(조회수++) 복합)
	 * 
	 * @param 게시판명, 글번호를 db에 요청
	 * 
	 * @return 글내용이 들어있는 boardVO obj
	 */
	public BoardVO bbsRead(HashMap<String, Object> map);

	/*
	 * 읽은 글의 조회수를 업데이트한다.
	 */
	public int bbsReadCnt(BoardVO cnt);

	/*
	 * 구현하지 않을 계획 console수준에서 수정하기 너무 어렵다. 대신, 게시판 글 읽기에 update기능 있으므로 ok라고 생각함.
	 * (수정이란, 이전 글 내용을 불러온 상태에서 다시 입력하는 것을 말함)
	 */
	public BoardVO bbsEdit(BoardVO edit);

	/*
	 * 게시판 글 삭제 (update, insert)
	 *
	 * @param 게시판명, 글번호를 db에 요청
	 * 
	 * @return 글내용이 들어있는 boardVO obj를 쓰레기통으로 이동시키고 글 제목과 글 내용은 삭제된 글입니다. 로 수정하도록 한다.
	 */
	public int bbsDelete(HashMap<String, Object> map);

	/*
	 * bbsDelete()에서 이동시키고, 쓰레기통으로 갔음을 알리도록 rename하는 메소드
	 */
	public int bbsRenameTrash(HashMap<String, Object> map);

	/*
	 * 게시판 글 완전삭제 (관리자권한, delete)
	 * 
	 * @param 게시판명, 글번호를 db에 요청 (단, 게시판명은 원본 게시판이 아닌, 쓰레기통 게시판)
	 * 
	 * @return 글내용이 들어있는 boardVO를 삭제한다.
	 */
	public int bbsEraseTrash(HashMap<String, Object> map);

	// UsersVO 관련 field // UsersVO 관련 field // UsersVO 관련 field // UsersVO 관련 field
	// UsersVO 관련 field // UsersVO 관련 field // UsersVO 관련 field // UsersVO 관련 field
	// UsersVO 관련 field // UsersVO 관련 field // UsersVO 관련 field // UsersVO 관련 field
	// UsersVO 관련 field // UsersVO 관련 field // UsersVO 관련 field // UsersVO 관련 field
	// UsersVO 관련 field // UsersVO 관련 field // UsersVO 관련 field // UsersVO 관련 field

	/*
	 * 회원가입 (insert)
	 * 
	 * @param UI에서 입력받은 UsersVO 객체
	 * 
	 * @return 성공시 true, 실패시 false
	 */
	public int reqJoin(HashMap<String, Object> map);

	/*
	 * id중복확인 (select)
	 * 
	 * @param UI에서 입력받은 id명
	 * 
	 * @return 중복시 false, 중복 내용 없을시 true; -- 단, String을 보내서 SELECT id FROM users
	 * WHERE id=chkId했을때 t/f판단 가능한지 모르겠음. 스트링으로 받아서 mgr단에서 중복확인처리 하는것으로.
	 */
	public String chkDupl(String chkId);

	/*
	 * id-이름 매칭 (select)
	 * 
	 * @param 게시판에서 글 쓰거나 할 때 이름표시하기 위해 id를 입력(전송)받는다.
	 * 
	 * @return userName
	 */
	public String idName(String userId);

	/*
	 * userAuth 권한조정 (update) 승급과 강등을 관리한다.
	 * 
	 * @param UI에서 입력받은 id명과 권한레벨
	 * 
	 * @return 성공시 true, 실패시 false
	 */
	public int adjAuth(HashMap<String, Object> map);

	/*
	 * usersVO의 PF수정 (update)
	 * 
	 * @param UI에서 입력받은 id명과 프로필내용, id는 본인한정으로 한다.
	 * 
	 * @return 성공시 true, 실패시 false
	 */
	public int updPf(HashMap<String, Object> map);

	/*
	 * 탈퇴요청 (update)
	 * 
	 * @param 본인한정, pw입력 후 auth를 0으로 내리도록 한다
	 * 
	 * @return 성공시 true, 실패시 false
	 */
	public int reqLeave(String leaveId);

	/*
	 * 삭제 (delete)
	 * 
	 * @param 관리자권한, id를 입력받아 auth가 0일때만(reqLeave했을때만) 삭제하도록 한다.
	 * 
	 * @return 성공시 true, 실패시 false
	 */
	public int removeUser(int selectUserIdNo);

	/*
	 * 회원조회 (관리자권한, select)
	 * 
	 * @param userAuth로 0 탈퇴요청회원 1 준회원(승급대기) 2 정회원 까지에 대해
	 * 
	 * @return 해당 회원 목록을 반환
	 */
	public ArrayList<UsersVO> listUser(int userAuth);

	/*
	 * 회원조회 (관리자권한, select)
	 * 
	 * @return 전체회원 목록을 반환
	 */
	public ArrayList<UsersVO> listAllUser();

	/*
	 * 로그인
	 * 
	 * @param id/pw 맵
	 * 
	 * @return 결과
	 */
	public int login(HashMap<String, Object> map);

	/*
	 * 접속 유저 권한 확인용 
	 */
	public int chkAuth(String s23);

	/* 
	 * 승급시 대상자 권한 재확인용 (대상 외 강제 등급조정 방지)
	 */
	public int chkAuthByIdNo(int rcvIdNo);

	/* 
	 * 승급시 권한부여용 (by IdNo)로
	 */
	public int adjAuthByIdNo(HashMap<String, Object> map);

	/*
	 * 3주 이내의 최신 공지사항 가져오기
	 */
	public BoardVO recentNotice();

	/*
	 * 3일 이내의 최신 글 가져오기
	 */
	public ArrayList<BoardVO> recentBoard();
}
