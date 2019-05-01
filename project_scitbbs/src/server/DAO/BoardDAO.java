package server.DAO;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import server.VO.BoardVO;
import server.VO.UsersVO;

public class BoardDAO {
	// database ���� ������ �����´�.
	//
	// (������) user > java > mybatis(dbms) > db �� ������ ������ �������ڸ�,
	// java side : �� Ŭ�������� method �ۼ��س��� �޼ҵ带 mgr���� ��ü���� ��
	// mgr.method()�� ȣ���ϴ� �� ����ϰ� �� ���̴�.
	// �׷� �޼ҵ�� (read��� ��������) �� ����� xxMapper.java (=interface class) �� read()�޼ҵ��
	// ����Ǹ�,
	// java-db side : �̴� �ٽ� \src\�� mapper.xml ���� �ִ� ���� �̸��� ����
	// id="read"�� ����Ǿ� id="read" �Ʒ��� ����� sql�� �����ϰ� �ȴ�.
	// db side : DB�� ������ sql�� ���� insert, select���� ������ ó���Ͽ� ��ȯ�ϰ� �Ǹ�
	// \src\xxmapper.xml ���� ���ǵ� returnType �� ���� ��ȯ(�Ǵ� void return)�ϰ� �� ���̴�.
	// insert�� ���� paramtype�� �ް� add�Ŀ� �����ϹǷ� ���̻� �������� �ʴ´�.

	private SqlSessionFactory factory = MyBatisConfig.getSqlSessionFactory();

	public ArrayList<BoardVO> bbsList(HashMap<String, Object> map) {
		ArrayList<BoardVO> result = null;
		SqlSession session = null;

		try {
			session = factory.openSession();
			BbsMapper bMap = session.getMapper(BbsMapper.class);
			result = bMap.bbsList(map);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close(); // �ڿ� ��ȯ
			}
		}

		return result;
	}

	public BoardVO bbsRead(HashMap<String, Object> map) {
		BoardVO result = null;
		SqlSession session = null;

		try {
			session = factory.openSession();
			BbsMapper bMap = session.getMapper(BbsMapper.class);
			result = bMap.bbsRead(map);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close(); // �ڿ� ��ȯ
			}
		}

		return result;
	}

	public int bbsReadCnt(BoardVO read) {
		int result = 0;
		SqlSession session = null;

		try {
			session = factory.openSession();
			BbsMapper bMap = session.getMapper(BbsMapper.class);
			result = bMap.bbsReadCnt(read);
			session.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close(); // �ڿ� ��ȯ
			}
		}
		return result;
	}

	public int bbsWrite(HashMap<String, Object> map) {
		int result = 0;
		SqlSession session = null;

		try {
			session = factory.openSession();
			BbsMapper bMap = session.getMapper(BbsMapper.class);
			result = bMap.bbsWrite(map);
			session.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close(); // �ڿ� ��ȯ
			}
		}

		return result;
	}

	public int bbsDelete(HashMap<String, Object> map) {
		int result = 0;
		SqlSession session = null;

		try {
			session = factory.openSession();
			BbsMapper bMap = session.getMapper(BbsMapper.class);
			result = bMap.bbsDelete(map);
			session.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close(); // �ڿ� ��ȯ
			}
		}
		return result;
	}

	public int bbsRenameTrash(HashMap<String, Object> map) {
		int result = 0;
		SqlSession session = null;

		try {
			session = factory.openSession();
			BbsMapper bMap = session.getMapper(BbsMapper.class);
			result = bMap.bbsRenameTrash(map);
			session.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close(); // �ڿ� ��ȯ
			}
		}
		return result;
	}

	public int bbsEraseTrash(HashMap<String, Object> map) {
		int result = 0;
		SqlSession session = null;

		try {
			session = factory.openSession();
			BbsMapper bMap = session.getMapper(BbsMapper.class);
			result = bMap.bbsEraseTrash(map);
			session.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close(); // �ڿ� ��ȯ
			}
		}
		return result;
	}

	public BoardVO recentNotice() {
		BoardVO result = null;
		SqlSession session = null;

		try {
			session = factory.openSession();
			BbsMapper bMap = session.getMapper(BbsMapper.class);
			result = bMap.recentNotice();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close(); // �ڿ� ��ȯ
			}
		}

		return result;
	}

	public ArrayList<BoardVO> recentBoard() {
		ArrayList<BoardVO> result = null;
		SqlSession session = null;

		try {
			session = factory.openSession();
			BbsMapper bMap = session.getMapper(BbsMapper.class);
			result = bMap.recentBoard();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close(); // �ڿ� ��ȯ
			}
		}

		return result;
	}

	public int reqJoin(HashMap<String, Object> map) {
		int result = 0;
		SqlSession session = null;

		try {
			session = factory.openSession();
			BbsMapper bMap = session.getMapper(BbsMapper.class);
			result = bMap.reqJoin(map);
			session.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close(); // �ڿ� ��ȯ
			}
		}
		return result;
	}

	public String chkDupl(String chkId) {
		String result = "";
		SqlSession session = null;

		try {
			session = factory.openSession();
			BbsMapper bMap = session.getMapper(BbsMapper.class);
			result = bMap.chkDupl(chkId);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close(); // �ڿ� ��ȯ
			}
		}
		return result;
	}

	public String idName(String userId) {
		String result = "";
		SqlSession session = null;

		try {
			session = factory.openSession();
			BbsMapper bMap = session.getMapper(BbsMapper.class);
			result = bMap.idName(userId);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close(); // �ڿ� ��ȯ
			}
		}
		return result;
	}

	public int adjAuth(HashMap<String, Object> map) {
		int result = 0;
		SqlSession session = null;

		try {
			session = factory.openSession();
			BbsMapper bMap = session.getMapper(BbsMapper.class);
			result = bMap.adjAuth(map);
			session.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close(); // �ڿ� ��ȯ
			}
		}
		return result;
	}

	public int adjAuthByIdNo(HashMap<String, Object> map) {
		int result = 0;
		SqlSession session = null;

		try {
			session = factory.openSession();
			BbsMapper bMap = session.getMapper(BbsMapper.class);
			result = bMap.adjAuthByIdNo(map);
			session.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close(); // �ڿ� ��ȯ
			}
		}
		return result;
	}

	public int updPf(HashMap<String, Object> map) {
		int result = 0;
		SqlSession session = null;

		try {
			session = factory.openSession();
			BbsMapper bMap = session.getMapper(BbsMapper.class);
			result = bMap.updPf(map);
			session.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close(); // �ڿ� ��ȯ
			}
		}
		return result;
	}

	public int reqLeave(String leaveId) {
		int result = 0;
		SqlSession session = null;

		try {
			session = factory.openSession();
			BbsMapper bMap = session.getMapper(BbsMapper.class);
			result = bMap.reqLeave(leaveId);
			session.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close(); // �ڿ� ��ȯ
			}
		}
		return result;
	}

	public int removeUser(int selectUserIdNo) {
		int result = 0;
		SqlSession session = null;

		try {
			session = factory.openSession();
			BbsMapper bMap = session.getMapper(BbsMapper.class);
			result = bMap.removeUser(selectUserIdNo);
			session.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close(); // �ڿ� ��ȯ
			}
		}
		return result;
	}

	public ArrayList<UsersVO> listUser(int userAuth) {
		ArrayList<UsersVO> result = new ArrayList<UsersVO>();
		SqlSession session = null;

		try {
			session = factory.openSession();
			BbsMapper bMap = session.getMapper(BbsMapper.class);
			result = bMap.listUser(userAuth);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close(); // �ڿ� ��ȯ
			}
		}
		return result;
	}

	public ArrayList<UsersVO> listAllUser() {
		ArrayList<UsersVO> result = new ArrayList<UsersVO>();
		SqlSession session = null;

		try {
			session = factory.openSession();
			BbsMapper bMap = session.getMapper(BbsMapper.class);
			result = bMap.listAllUser();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close(); // �ڿ� ��ȯ
			}
		}
		return result;
	}

	public boolean login(HashMap<String, Object> reqLogInMap) {
		boolean result = false;
		SqlSession session = null;
		int tempResult = 0;
		try {
			session = factory.openSession();
			BbsMapper bMap = session.getMapper(BbsMapper.class);
			tempResult = bMap.login(reqLogInMap);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close(); // �ڿ� ��ȯ
			}
		}
		if (tempResult == 1) {
			result = true;
		}
		return result;
	}

	public int chkAuth(String s23) {
		int result = 0;
		SqlSession session = null;
		try {
			session = factory.openSession();
			BbsMapper bMap = session.getMapper(BbsMapper.class);
			result = bMap.chkAuth(s23);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close(); // �ڿ� ��ȯ
			}
		}
		return result;
	}

	public int chkAuth(int rcvIdNo) {
		int result = 0;
		SqlSession session = null;
		try {
			session = factory.openSession();
			BbsMapper bMap = session.getMapper(BbsMapper.class);
			result = bMap.chkAuthByIdNo(rcvIdNo);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close(); // �ڿ� ��ȯ
			}
		}
		return result;
	}

}
