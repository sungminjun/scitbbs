package server.VO;

public class UsersVO {
	// member�� seqNo(primary key), id, name, pw, pf, phone, userAuth, joinDate
	// id varchar2(8), -- �ѱ�4��, ����8�� �̳�
	// name varchar2(8), -- �ѱ�4��, ����8�� �̳�
	// pw varchar2(11), -- ��й�ȣ, 11�� �̳�
	// pf varchar2(237), -- �ڱ�Ұ�, 79byte*3��, �� 80����Ʈ �ƴ��� �𸣰���
	// phone Number(20), -- ��ȭ��ȣ
	// userAuth number(1), -- ���� 0=guest, 1=user 2=sysop �� ���� ������
	// joinDate date -- ��������, pf�� ǥ��

	private int userIdNo;
	private String userId;
	private String userPw;
	private String userName;
	private String userPhone;
	private String userPf;
	private int userAuth;
	private String userJoinDate;

	public UsersVO() {
	}

	public UsersVO(int userIdNo, String userId, String userPw, String userName, String userPhone, String userPf,
			int userAuth, String userJoinDate) {
		this.userIdNo = userIdNo;
		this.userId = userId;
		this.userPw = userPw;
		this.userName = userName;
		this.userPhone = userPhone;
		this.userPf = userPf;
		this.userAuth = userAuth;
		this.userJoinDate = userJoinDate;
	}

	public int getUserIdNo() {
		return userIdNo;
	}

	public void setUserIdNo(int userIdNo) {
		this.userIdNo = userIdNo;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserPw() {
		return userPw;
	}

	public void setUserPw(String userPw) {
		this.userPw = userPw;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public String getUserPf() {
		return userPf;
	}

	public void setUserPf(String userPf) {
		this.userPf = userPf;
	}

	public int getUserAuth() {
		return userAuth;
	}

	public void setUserAuth(int userAuth) {
		this.userAuth = userAuth;
	}

	public String getUserJoinDate() {
		return userJoinDate;
	}

	public void setUserJoinDate(String userJoinDate) {
		this.userJoinDate = userJoinDate;
	}

	@Override
	public String toString() {
		return "UsersVO [userIdNo=" + userIdNo + ", userId=" + userId + ", userPw=" + userPw + ", userName=" + userName
				+ ", userPhone=" + userPhone + ", userPf=" + userPf + ", userAuth=" + userAuth + ", userJoinDate="
				+ userJoinDate + "]";
	}

}
