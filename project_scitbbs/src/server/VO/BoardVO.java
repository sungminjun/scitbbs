package server.VO;


public class BoardVO {
	// board�� boardName, seqNo, writeId, subj, content, postDate
	// boardName varchar2(8), -- �Խ��� ������ ���� �� �����ϱ� ���� ���, go �޴��̸��� ������ ����
	// seqNo boardId_blah_seq.nextval , -- �Խ��� �� �Խù� ��ȣ
	// writeId varchar2(8) -- �ۼ���id, ��� �� memberInfo���� �����´�
	// subj varchar2(60) -- ����, ����ڰ� ����
	// content varchar2(1600) -- ����, 80*20 ���� �ϴ� ����
	private String boardName;
	private int seqNo;
	private String writeId;
	private String subj;
	private String content;
	private String writeDate;
	private int counter;

	public BoardVO() {
	}

	
	public BoardVO(String boardName) {
		this.boardName = boardName;
	}


	public BoardVO(String boardName, int seqNo, String writeId, String subj, String content, String writeDate,
			int counter) {
		this.boardName = boardName;
		this.seqNo = seqNo;
		this.writeId = writeId;
		this.subj = subj;
		this.content = content;
		this.writeDate = writeDate;
		this.counter = counter;
	}

	public String getBoardName() {
		return boardName;
	}

	public void setBoardName(String boardName) {
		this.boardName = boardName;
	}

	public int getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(int seqNo) {
		this.seqNo = seqNo;
	}

	public String getWriteId() {
		return writeId;
	}

	public void setWriteId(String writeId) {
		this.writeId = writeId;
	}

	public String getSubj() {
		return subj;
	}

	public void setSubj(String subj) {
		this.subj = subj;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getWriteDate() {
		return writeDate;
	}

	public void setWriteDate(String writeDate) {
		this.writeDate = writeDate;
	}

	public int getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}

	@Override
	public String toString() {
		return "BoardVO [boardName=" + boardName + ", seqNo=" + seqNo + ", writeId=" + writeId + ", subj=" + subj
				+ ", content=" + content + ", writeDate=" + writeDate + ", counter=" + counter + "]";
	}

}
