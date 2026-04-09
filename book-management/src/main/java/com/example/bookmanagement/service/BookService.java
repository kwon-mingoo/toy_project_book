package com.example.bookmanagement.service;

import com.example.bookmanagement.domain.Book;
import com.example.bookmanagement.domain.Category;
import com.example.bookmanagement.dto.BookSearchDto;
import com.example.bookmanagement.dto.PageDto;
import java.util.List;

/**
 * 도서 서비스 인터페이스
 *
 * 한국 SI 관습: 인터페이스와 구현체(Impl)를 분리
 * 장점:
 *  - 구현체 교체 시 호출부 코드 변경 불필요 (DIP 원칙)
 *  - 테스트 시 Mock 객체 주입 용이
 *  - Spring AOP(@Transactional)가 인터페이스 기반 프록시로 동작
 */
public interface BookService {

    /** 도서 목록 조회 (검색 + 페이지네이션) */
    PageDto<Book> getBookList(BookSearchDto searchDto);

    /** 도서 단건 조회 */
    Book getBookById(Long bookId);

    /** 도서 등록 */
    void createBook(Book book);

    /** 도서 수정 */
    void updateBook(Book book);

    /** 도서 삭제 */
    void deleteBook(Long bookId);

    /** 카테고리 전체 조회 */
    List<Category> getCategoryList();
}
