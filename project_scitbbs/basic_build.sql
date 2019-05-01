DROP TABLE USERS;
DROP TABLE BOARDS;
DROP TABLE BOARDNOTICE;
DROP TABLE BOARDFREE;
DROP TABLE BOARDANONY;
DROP TABLE BOARDTRASH;

DROP SEQUENCE userIdNoSeq;
DROP SEQUENCE BoardSeq;
DROP SEQUENCE BOARDNOTICESEQ;
DROP SEQUENCE BoardFreeSeq;
DROP SEQUENCE BoardAnonySeq;
DROP SEQUENCE BoardTrashSeq;

CREATE SEQUENCE userIdNoSeq 
    START WITH 1 INCREMENT BY 1 MAXVALUE 99999 NOMINVALUE NOCYCLE NOCACHE;
CREATE SEQUENCE boardSeq 
    START WITH 1 INCREMENT BY 1 MAXVALUE 99999 NOMINVALUE NOCYCLE NOCACHE;
CREATE SEQUENCE BOARDNOTICESEQ
    START WITH 1 INCREMENT BY 1 MAXVALUE 99999 NOMINVALUE NOCYCLE NOCACHE;
CREATE SEQUENCE boardAnonySeq 
    START WITH 1 INCREMENT BY 1 MAXVALUE 99999 NOMINVALUE NOCYCLE NOCACHE;
CREATE SEQUENCE boardFreeSeq 
    START WITH 1 INCREMENT BY 1 MAXVALUE 99999 NOMINVALUE NOCYCLE NOCACHE;
CREATE SEQUENCE boardTrashSeq 
    START WITH 1 INCREMENT BY 1 MAXVALUE 99999 NOMINVALUE NOCYCLE NOCACHE;

CREATE TABLE USERS(
    userIdNo number(5) constraint useridno_pk primary key-- 고유id 번호, Seq 처리
    , userId nvarchar2(8) CONSTRAINT id_capital CHECK (userId = upper(userId)) unique -- id 8글자
    , userPw nvarchar2(11) not null -- pw 11글자까지
    , userName nvarchar2(4) not null -- 이름
    , userPhone nvarchar2(20)
    , userPf varchar2(236) -- 자기소개
    , userAuth number(1) default 2 -- 권한
    , userJoinDate date default sysdate
);

CREATE TABLE BOARDS(
    seqNo number(3) primary key,
    name varchar(20) not null
);

CREATE TABLE BOARDNOTICE(
    boardName varchar2(20) default 'BOARDNOTICE'
    , seqNo number(5) unique
    , writeId nvarchar2(8) not null
    , subj nvarchar2(70) not null
    , content nvarchar2(1600) not null
    , writeDate date default sysdate
    , counter number(5) default 0
);

CREATE TABLE BOARDFREE(
    boardName varchar2(20) default 'BOARDFREE'
    , seqNo number(5) unique
    , writeId nvarchar2(8) not null
    , subj nvarchar2(70) not null
    , content nvarchar2(1600) not null
    , writeDate date default sysdate
    , counter number(5) default 0
);

CREATE TABLE BOARDANONY(
    boardName varchar2(20) default 'BOARDANONY'
    , seqNo number(5) unique
    , writeId nvarchar2(8) not null
    , subj nvarchar2(70) not null
    , content nvarchar2(1600) not null
    , writeDate date default sysdate
    , counter number(5) default 0
);

CREATE TABLE BOARDTRASH(
    boardName varchar2(20) default 'BOARDTRASH'
    , seqNo number(5)
    , writeId varchar2(8) not null
    , subj varchar2(70) not null
    , content varchar2(1600) not null
    , writeDate date default sysdate
    , counter number(5) default 0
);



-- create basic boards tree table
INSERT INTO boards VALUES(boardSeq.nextval, 'BOARDNOTICE');
INSERT INTO boards VALUES(boardSeq.nextval, 'BOARDFREE');
INSERT INTO boards VALUES(boardSeq.nextval, 'BOARDANONY');
INSERT INTO boards VALUES(boardSeq.nextval, 'BOARDTRASH');

-- create sample id 
INSERT INTO USERS VALUES(userIdNoSeq.nextval, 'SYSOP', 'oper', '운영자', '010-6545-5825', '안녕하세요.', 5, sysdate);
INSERT INTO USERS VALUES(userIdNoSeq.nextval, 'SCIT', 'kita', '김무역', '02-6000-6260','반갑습니다.', 2, sysdate);
INSERT INTO USERS VALUES(userIdNoSeq.nextval, 'GUEST', '1234', '손님', '00-0000-0000','손님입니다.', 1, sysdate);

-- create sample board contents
INSERT INTO BOARDNOTICE(seqNo, writeId, subj, content) VALUES(boardNoticeSeq.nextval, 'SYSOP', '공지게시판 샘플 제목 1', '공지내용1');
INSERT INTO BOARDNOTICE(seqNo, writeId, subj, content) VALUES(boardNoticeSeq.nextval, 'SYSOP', '공지게시판 샘플 제목 1', '공지내용2');
INSERT INTO BOARDNOTICE(seqNo, writeId, subj, content) VALUES(boardNoticeSeq.nextval, 'SYSOP', '공지게시판 샘플 제목 1', '공지내용3');

INSERT INTO BOARDFREE(seqNo, writeId, subj, content) VALUES(boardFreeSeq.nextval, 'SCIT', '자유게시판 샘플 제목 1', '자유내용1');
INSERT INTO BOARDFREE(seqNo, writeId, subj, content) VALUES(boardFreeSeq.nextval, 'SCIT', '자유게시판 샘플 제목 2', '자유내용2');
INSERT INTO BOARDFREE(seqNo, writeId, subj, content) VALUES(boardFreeSeq.nextval, 'SCIT', '자유게시판 샘플 제목 3', '자유내용3');

INSERT INTO BOARDANONY(seqNo, writeId, subj, content) VALUES(boardAnonySeq.nextval, 'SCIT', '익명게시판 샘플 제목 1', '익명내용1');
INSERT INTO BOARDANONY(seqNo, writeId, subj, content) VALUES(boardAnonySeq.nextval, 'SCIT', '익명게시판 샘플 제목 2', '익명내용2');
INSERT INTO BOARDANONY(seqNo, writeId, subj, content) VALUES(boardAnonySeq.nextval, 'SCIT', '익명게시판 샘플 제목 3', '익명내용3');

COMMIT;

SELECT * FROM BOARDTRASH;   