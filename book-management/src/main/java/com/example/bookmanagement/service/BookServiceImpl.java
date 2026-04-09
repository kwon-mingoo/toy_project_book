package com.example.bookmanagement.service;

import com.example.bookmanagement.domain.Book;
import com.example.bookmanagement.domain.Category;
import com.example.bookmanagement.dto.BookSearchDto;
import com.example.bookmanagement.dto.PageDto;
import com.example.bookmanagement.mapper.BookMapper;
import com.example.bookmanagement.mapper.CategoryMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * 도서 서비스 구현체
 *
 * @Service: Spring이 이 클래스를 서비스 레이어 빈으로 등록
 * @RequiredArgsConstructor: final 필드를 파라미터로 받는 생성자 자동 생성 → 생성자 주입 (Spring DI)
 * @Slf4j: log 변수 자동 생성 (Lombok)
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    // 생성자 주입 — @RequiredArgsConstructor가 자동 생성
    private final BookMapper bookMapper;
    private final CategoryMapper categoryMapper;

    /**
     * 도서 목록 조회
     * @Transactional(readOnly=true): 읽기 전용 트랜잭션 — DB 최적화 힌트 제공, 실수로 데이터 변경 방지
     */
    @Override
    @Transactional(readOnly = true)
    public PageDto<Book> getBookList(BookSearchDto searchDto) {
        log.debug("도서 목록 조회 - 검색조건: {}", searchDto);
        List<Book> books = bookMapper.findAll(searchDto);
        long totalCount = bookMapper.countAll(searchDto);
        return new PageDto<>(books, searchDto.getPage(), totalCount, searchDto.getSize());
    }

    /**
     * 도서 단건 조회
     * 존재하지 않으면 GlobalExceptionHandler가 처리하는 예외를 던짐
     */
    @Override
    @Transactional(readOnly = true)
    public Book getBookById(Long bookId) {
        log.debug("도서 상세 조회 - bookId: {}", bookId);
        Book book = bookMapper.findById(bookId);
        if (book == null) {
            throw new IllegalArgumentException("존재하지 않는 도서입니다. bookId: " + bookId);
        }
        return book;
    }

    /**
     * 도서 등록
     * @Transactional: 예외 발생 시 롤백, 정상 완료 시 커밋
     */
    @Override
    @Transactional
    public void createBook(Book book) {
        log.info("도서 등록 - title: {}", book.getTitle());
        bookMapper.insert(book);
    }

    /** 도서 수정 */
    @Override
    @Transactional
    public void updateBook(Book book) {
        log.info("도서 수정 - bookId: {}", book.getBookId());
        // 존재 여부 선확인 (없으면 예외 발생)
        getBookById(book.getBookId());
        bookMapper.update(book);
    }

    /** 도서 삭제 */
    @Override
    @Transactional
    public void deleteBook(Long bookId) {
        log.info("도서 삭제 - bookId: {}", bookId);
        // 존재 여부 선확인 (없으면 예외 발생)
        getBookById(bookId);
        bookMapper.delete(bookId);
    }

    /** 카테고리 전체 조회 */
    @Override
    @Transactional(readOnly = true)
    public List<Category> getCategoryList() {
        return categoryMapper.findAll();
    }
}
