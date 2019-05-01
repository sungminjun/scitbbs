package server.DAO;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import server.VO.BoardVO;
import server.VO.UsersVO;

public class BoardDAO {
	// database 에서 정보를 가져온다.
	//
	// (내생각) user > java > mybatis(dbms) > db 의 순으로 진행을 설명하자면,
	// java side : 현 클래스에서 method 작성해놓은 메소드를 mgr에서 객체생성 후
	// mgr.method()를 호출하는 식 사용하게 될 것이다.
	// 그럼 메소드명 (read라고 가정하자) 에 연결된 xxMapper.java (=interface class) 의 read()메소드와
	// 연결되며,
	// java-db side : 이는 다시 \src\의 mapper.xml 내에 있는 같은 이름을 쓰는
	// id="read"에 연결되어 id="read" 아래에 선언된 sql을 전송하게 된다.
	// db side : DB는 수신한 sql에 따라 insert, select등의 내용을 처리하여 반환하게 되며
	// \src\xxmapper.xml 에서 정의된 returnType 등등에 의해 반환(또는 void return)하게 될 것이다.
	// insert의 경우는 paramtype만 받고 add후에 종결하므로 더이상 정리하지 않는다.

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
				session.close(); // 자원 반환
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
				session.close(); // 자원 반환
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
				session.close(); // 자원 반환
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
				session.close(); // 자원 반환
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
				session.close(); // 자원 반환
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
				session.close(); // 자원 반환
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
				session.close(); // 자원 반환
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
				session.close(); // 자원 반환
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
				session.close(); // 자원 반환
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
				session.close(); // 자원 반환
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
				session.close(); // 자원 반환
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
				session.close(); // 자원 반환
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
				session.close(); // 자원 반환
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
				session.close(); // 자원 반환
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
				session.close(); // 자원 반환
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
				session.close(); // 자원 반환
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
				session.close(); // 자원 반환
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
				session.close(); // 자원 반환
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
				session.close(); // 자원 반환
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
				session.close(); // 자원 반환
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
				session.close(); // 자원 반환
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
				session.close(); // 자원 반환
			}
		}
		return result;
	}

}
