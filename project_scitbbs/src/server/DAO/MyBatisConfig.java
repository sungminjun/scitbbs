package server.DAO;

import java.io.Reader;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class MyBatisConfig {
	private static SqlSessionFactory sqlSessionFactory;

	// static �ʱ�ȭ���� �̿��ϸ� Ŭ������ �ε� �� �� ���� �ѹ� �����ϰ� �ȴ�.
	// Ư���� �ʱ�ȭ���� �̿��ϸ� logic�� ���� �� �ֱ� ������ ������ �ʱ⺯�� �����̳� ���� ���� ����ó���� ���� ������ ���� �� �ִ�.

	static {
		String resource = "./mybatis-config.xml"; // Mybatis ȯ�漳�� ���� �б�. src ��ο� ����.

		try {
			// Reader ��ü�� ���ؼ� mybatis-config.xml�� ������ ���� ���پ� �о�鿩��,
			// �����ͺ��̽� ���� ����(����Ŭ SID, acc id/pw)�� SqlSessionFactory ��ü�� build�Ѵ�.
			
			Reader reader = Resources.getResourceAsReader(resource);
			sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static SqlSessionFactory getSqlSessionFactory() {
		return sqlSessionFactory;
	}
}
