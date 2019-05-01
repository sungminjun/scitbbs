package server.DAO;

import java.util.ArrayList;
import java.util.HashMap;

import server.VO.BoardVO;
import server.VO.UsersVO;

public interface BbsMapper {
	// �ش�vo mapper.xml �� �ִ� sql�� ����� id��� ���ƾ� �Ѵ�.
	// ��, �޼ҵ� ��� ���� id���� ȣ���� �� ������ sql�� ���.

	// board, �� �Խ��� ���� method �켱

	/*
	 * �Խñ� ���� ��� (insert)
	 * 
	 * @param UI�ܿ��� �Է¹��� ������ BoardVO��ü ����
	 * 
	 * @return ������ true, ���н� false
	 */
	public int bbsWrite(HashMap<String, Object> map);

	/*
	 * �Խ��� ��� �ҷ����� (select)
	 * 
	 * @param �Խ��Ǹ��� db�� ��û
	 * 
	 * @return BoardVO��ü�� ����(arrayList)�� ��ȯ
	 */
	public ArrayList<BoardVO> bbsList(HashMap<String, Object> map);

	/*
	 * �Խ��� �� �б� (select(���б�) �� update(��ȸ��++) ����)
	 * 
	 * @param �Խ��Ǹ�, �۹�ȣ�� db�� ��û
	 * 
	 * @return �۳����� ����ִ� boardVO obj
	 */
	public BoardVO bbsRead(HashMap<String, Object> map);

	/*
	 * ���� ���� ��ȸ���� ������Ʈ�Ѵ�.
	 */
	public int bbsReadCnt(BoardVO cnt);

	/*
	 * �������� ���� ��ȹ console���ؿ��� �����ϱ� �ʹ� ��ƴ�. ���, �Խ��� �� �б⿡ update��� �����Ƿ� ok��� ������.
	 * (�����̶�, ���� �� ������ �ҷ��� ���¿��� �ٽ� �Է��ϴ� ���� ����)
	 */
	public BoardVO bbsEdit(BoardVO edit);

	/*
	 * �Խ��� �� ���� (update, insert)
	 *
	 * @param �Խ��Ǹ�, �۹�ȣ�� db�� ��û
	 * 
	 * @return �۳����� ����ִ� boardVO obj�� ������������ �̵���Ű�� �� ����� �� ������ ������ ���Դϴ�. �� �����ϵ��� �Ѵ�.
	 */
	public int bbsDelete(HashMap<String, Object> map);

	/*
	 * bbsDelete()���� �̵���Ű��, ������������ ������ �˸����� rename�ϴ� �޼ҵ�
	 */
	public int bbsRenameTrash(HashMap<String, Object> map);

	/*
	 * �Խ��� �� �������� (�����ڱ���, delete)
	 * 
	 * @param �Խ��Ǹ�, �۹�ȣ�� db�� ��û (��, �Խ��Ǹ��� ���� �Խ����� �ƴ�, �������� �Խ���)
	 * 
	 * @return �۳����� ����ִ� boardVO�� �����Ѵ�.
	 */
	public int bbsEraseTrash(HashMap<String, Object> map);

	// UsersVO ���� field // UsersVO ���� field // UsersVO ���� field // UsersVO ���� field
	// UsersVO ���� field // UsersVO ���� field // UsersVO ���� field // UsersVO ���� field
	// UsersVO ���� field // UsersVO ���� field // UsersVO ���� field // UsersVO ���� field
	// UsersVO ���� field // UsersVO ���� field // UsersVO ���� field // UsersVO ���� field
	// UsersVO ���� field // UsersVO ���� field // UsersVO ���� field // UsersVO ���� field

	/*
	 * ȸ������ (insert)
	 * 
	 * @param UI���� �Է¹��� UsersVO ��ü
	 * 
	 * @return ������ true, ���н� false
	 */
	public int reqJoin(HashMap<String, Object> map);

	/*
	 * id�ߺ�Ȯ�� (select)
	 * 
	 * @param UI���� �Է¹��� id��
	 * 
	 * @return �ߺ��� false, �ߺ� ���� ������ true; -- ��, String�� ������ SELECT id FROM users
	 * WHERE id=chkId������ t/f�Ǵ� �������� �𸣰���. ��Ʈ������ �޾Ƽ� mgr�ܿ��� �ߺ�Ȯ��ó�� �ϴ°�����.
	 */
	public String chkDupl(String chkId);

	/*
	 * id-�̸� ��Ī (select)
	 * 
	 * @param �Խ��ǿ��� �� ���ų� �� �� �̸�ǥ���ϱ� ���� id�� �Է�(����)�޴´�.
	 * 
	 * @return userName
	 */
	public String idName(String userId);

	/*
	 * userAuth �������� (update) �±ް� ������ �����Ѵ�.
	 * 
	 * @param UI���� �Է¹��� id��� ���ѷ���
	 * 
	 * @return ������ true, ���н� false
	 */
	public int adjAuth(HashMap<String, Object> map);

	/*
	 * usersVO�� PF���� (update)
	 * 
	 * @param UI���� �Է¹��� id��� �����ʳ���, id�� ������������ �Ѵ�.
	 * 
	 * @return ������ true, ���н� false
	 */
	public int updPf(HashMap<String, Object> map);

	/*
	 * Ż���û (update)
	 * 
	 * @param ��������, pw�Է� �� auth�� 0���� �������� �Ѵ�
	 * 
	 * @return ������ true, ���н� false
	 */
	public int reqLeave(String leaveId);

	/*
	 * ���� (delete)
	 * 
	 * @param �����ڱ���, id�� �Է¹޾� auth�� 0�϶���(reqLeave��������) �����ϵ��� �Ѵ�.
	 * 
	 * @return ������ true, ���н� false
	 */
	public int removeUser(int selectUserIdNo);

	/*
	 * ȸ����ȸ (�����ڱ���, select)
	 * 
	 * @param userAuth�� 0 Ż���ûȸ�� 1 ��ȸ��(�±޴��) 2 ��ȸ�� ������ ����
	 * 
	 * @return �ش� ȸ�� ����� ��ȯ
	 */
	public ArrayList<UsersVO> listUser(int userAuth);

	/*
	 * ȸ����ȸ (�����ڱ���, select)
	 * 
	 * @return ��üȸ�� ����� ��ȯ
	 */
	public ArrayList<UsersVO> listAllUser();

	/*
	 * �α���
	 * 
	 * @param id/pw ��
	 * 
	 * @return ���
	 */
	public int login(HashMap<String, Object> map);

	/*
	 * ���� ���� ���� Ȯ�ο� 
	 */
	public int chkAuth(String s23);

	/* 
	 * �±޽� ����� ���� ��Ȯ�ο� (��� �� ���� ������� ����)
	 */
	public int chkAuthByIdNo(int rcvIdNo);

	/* 
	 * �±޽� ���Ѻο��� (by IdNo)��
	 */
	public int adjAuthByIdNo(HashMap<String, Object> map);

	/*
	 * 3�� �̳��� �ֽ� �������� ��������
	 */
	public BoardVO recentNotice();

	/*
	 * 3�� �̳��� �ֽ� �� ��������
	 */
	public ArrayList<BoardVO> recentBoard();
}
