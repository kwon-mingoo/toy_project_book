-- ============================================================
-- 도서 관리 시스템 샘플 데이터
-- 카테고리 5건 + 도서 20건
-- ============================================================

-- ── 카테고리 데이터 (5건) ────────────────────────────────────
INSERT INTO category VALUES (seq_category.NEXTVAL, '소설',    SYSDATE);
INSERT INTO category VALUES (seq_category.NEXTVAL, 'IT',      SYSDATE);
INSERT INTO category VALUES (seq_category.NEXTVAL, '자기계발', SYSDATE);
INSERT INTO category VALUES (seq_category.NEXTVAL, '역사',    SYSDATE);
INSERT INTO category VALUES (seq_category.NEXTVAL, '과학',    SYSDATE);

-- ── 도서 데이터 (20건) ──────────────────────────────────────

-- 소설 (category_id = 1)
INSERT INTO book (BOOK_ID, TITLE, AUTHOR, PUBLISHER, PRICE, STOCK, CATEGORY_ID)
VALUES (seq_book.NEXTVAL, '채식주의자', '한강', '창비', 13500, 15, 1);

INSERT INTO book (BOOK_ID, TITLE, AUTHOR, PUBLISHER, PRICE, STOCK, CATEGORY_ID)
VALUES (seq_book.NEXTVAL, '82년생 김지영', '조남주', '민음사', 14000, 20, 1);

INSERT INTO book (BOOK_ID, TITLE, AUTHOR, PUBLISHER, PRICE, STOCK, CATEGORY_ID)
VALUES (seq_book.NEXTVAL, '아몬드', '손원평', '창비', 13500, 18, 1);

INSERT INTO book (BOOK_ID, TITLE, AUTHOR, PUBLISHER, PRICE, STOCK, CATEGORY_ID)
VALUES (seq_book.NEXTVAL, '파친코', '이민진', '문학사상', 23000, 12, 1);

-- IT (category_id = 2)
INSERT INTO book (BOOK_ID, TITLE, AUTHOR, PUBLISHER, PRICE, STOCK, CATEGORY_ID)
VALUES (seq_book.NEXTVAL, '클린 코드', '로버트 C. 마틴', '인사이트', 33000, 10, 2);

INSERT INTO book (BOOK_ID, TITLE, AUTHOR, PUBLISHER, PRICE, STOCK, CATEGORY_ID)
VALUES (seq_book.NEXTVAL, '자바 ORM 표준 JPA 프로그래밍', '김영한', '에이콘', 43000, 8, 2);

INSERT INTO book (BOOK_ID, TITLE, AUTHOR, PUBLISHER, PRICE, STOCK, CATEGORY_ID)
VALUES (seq_book.NEXTVAL, '토비의 스프링 3.1', '이일민', '에이콘', 60000, 5, 2);

INSERT INTO book (BOOK_ID, TITLE, AUTHOR, PUBLISHER, PRICE, STOCK, CATEGORY_ID)
VALUES (seq_book.NEXTVAL, '이펙티브 자바 3판', '조슈아 블로크', '인사이트', 36000, 7, 2);

INSERT INTO book (BOOK_ID, TITLE, AUTHOR, PUBLISHER, PRICE, STOCK, CATEGORY_ID)
VALUES (seq_book.NEXTVAL, '스프링 부트와 AWS로 혼자 구현하는 웹 서비스', '이동욱', '프리렉', 32000, 15, 2);

-- 자기계발 (category_id = 3)
INSERT INTO book (BOOK_ID, TITLE, AUTHOR, PUBLISHER, PRICE, STOCK, CATEGORY_ID)
VALUES (seq_book.NEXTVAL, '아주 작은 습관의 힘', '제임스 클리어', '비즈니스북스', 18000, 25, 3);

INSERT INTO book (BOOK_ID, TITLE, AUTHOR, PUBLISHER, PRICE, STOCK, CATEGORY_ID)
VALUES (seq_book.NEXTVAL, '미라클 모닝', '할 엘로드', '한빛비즈', 16000, 20, 3);

INSERT INTO book (BOOK_ID, TITLE, AUTHOR, PUBLISHER, PRICE, STOCK, CATEGORY_ID)
VALUES (seq_book.NEXTVAL, '부의 추월차선', '엠제이 드마코', '토트북', 19800, 30, 3);

INSERT INTO book (BOOK_ID, TITLE, AUTHOR, PUBLISHER, PRICE, STOCK, CATEGORY_ID)
VALUES (seq_book.NEXTVAL, '1만 시간의 법칙', '말콤 글래드웰', '김영사', 15000, 0, 3);

-- 역사 (category_id = 4)
INSERT INTO book (BOOK_ID, TITLE, AUTHOR, PUBLISHER, PRICE, STOCK, CATEGORY_ID)
VALUES (seq_book.NEXTVAL, '총, 균, 쇠', '재레드 다이아몬드', '문학사상', 28000, 10, 4);

INSERT INTO book (BOOK_ID, TITLE, AUTHOR, PUBLISHER, PRICE, STOCK, CATEGORY_ID)
VALUES (seq_book.NEXTVAL, '사피엔스', '유발 하라리', '김영사', 22000, 14, 4);

INSERT INTO book (BOOK_ID, TITLE, AUTHOR, PUBLISHER, PRICE, STOCK, CATEGORY_ID)
VALUES (seq_book.NEXTVAL, '조선왕조실록', '박영규', '웅진지식하우스', 18000, 9, 4);

INSERT INTO book (BOOK_ID, TITLE, AUTHOR, PUBLISHER, PRICE, STOCK, CATEGORY_ID)
VALUES (seq_book.NEXTVAL, '로마인 이야기', '시오노 나나미', '한길사', 15000, 6, 4);

-- 과학 (category_id = 5)
INSERT INTO book (BOOK_ID, TITLE, AUTHOR, PUBLISHER, PRICE, STOCK, CATEGORY_ID)
VALUES (seq_book.NEXTVAL, '코스모스', '칼 세이건', '사이언스북스', 29000, 11, 5);

INSERT INTO book (BOOK_ID, TITLE, AUTHOR, PUBLISHER, PRICE, STOCK, CATEGORY_ID)
VALUES (seq_book.NEXTVAL, '이기적 유전자', '리처드 도킨스', '을유문화사', 22000, 8, 5);

INSERT INTO book (BOOK_ID, TITLE, AUTHOR, PUBLISHER, PRICE, STOCK, CATEGORY_ID)
VALUES (seq_book.NEXTVAL, '파인만의 물리학 강의', '리처드 파인만', '승산', 45000, 4, 5);

COMMIT;
