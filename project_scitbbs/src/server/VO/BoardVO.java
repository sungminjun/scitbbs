package server.VO;


public class BoardVO {
	// board는 boardName, seqNo, writeId, subj, content, postDate
	// boardName varchar2(8), -- 게시판 여러개 있을 때 구분하기 위해 사용, go 메뉴이름과 같도록 설정
	// seqNo boardId_blah_seq.nextval , -- 게시판 내 게시물 번호
	// writeId varchar2(8) -- 작성자id, 등록 시 memberInfo에서 가져온다
	// subj varchar2(60) -- 제목, 사용자가 지정
	// content varchar2(1600) -- 내용, 80*20 으로 일단 지정
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
