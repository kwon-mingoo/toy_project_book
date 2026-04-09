-- ============================================================
-- 도서 관리 시스템 DDL (Oracle XE 21c)
-- 실행 계정: bookadmin
-- ============================================================

-- [초기화 시] 기존 객체 제거 (주석 해제 후 사용)
-- DROP TABLE book CASCADE CONSTRAINTS PURGE;
-- DROP TABLE category CASCADE CONSTRAINTS PURGE;
-- DROP SEQUENCE seq_book;
-- DROP SEQUENCE seq_category;

-- ── 카테고리 테이블 ──────────────────────────────────────────
CREATE TABLE category (
    category_id   NUMBER          PRIMARY KEY,
    category_name VARCHAR2(100)   NOT NULL,
    created_at    DATE            DEFAULT SYSDATE
);

-- 카테고리 PK 채번용 시퀀스
CREATE SEQUENCE seq_category
    START WITH 1
    INCREMENT BY 1
    NOCACHE;

-- ── 도서 테이블 ─────────────────────────────────────────────
CREATE TABLE book (
    book_id     NUMBER          PRIMARY KEY,
    title       VARCHAR2(200)   NOT NULL,
    author      VARCHAR2(100)   NOT NULL,
    publisher   VARCHAR2(100),
    price       NUMBER(10),
    stock       NUMBER(5)       DEFAULT 0,
    category_id NUMBER,
    created_at  DATE            DEFAULT SYSDATE,
    updated_at  DATE            DEFAULT SYSDATE,
    CONSTRAINT fk_book_category
        FOREIGN KEY (category_id) REFERENCES category(category_id)
);

-- 도서 PK 채번용 시퀀스
CREATE SEQUENCE seq_book
    START WITH 1
    INCREMENT BY 1
    NOCACHE;

COMMIT;
