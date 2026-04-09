package com.example.bookmanagement.mapper;

import com.example.bookmanagement.domain.Book;
import com.example.bookmanagement.dto.BookSearchDto;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

/**
 * 도서 MyBatis Mapper 인터페이스
 * 실제 SQL은 resources/mapper/BookMapper.xml에 정의
 * @Mapper: Spring 컨텍스트에 Mapper 구현체를 자동 생성하여 빈으로 등록
 */
@Mapper
public interface BookMapper {

    /** 도서 목록 조회 (검색 조건 + 페이지네이션) */
    List<Book> findAll(BookSearchDto searchDto);

    /** 도서 전체 건수 조회 (페이지 수 계산용) */
    long countAll(BookSearchDto searchDto);

    /** 도서 단건 조회 (category JOIN 포함) */
    Book findById(Long bookId);

    /** 도서 등록 (Oracle 시퀀스로 PK 채번) */
    int insert(Book book);

    /** 도서 수정 */
    int update(Book book);

    /** 도서 삭제 */
    int delete(Long bookId);
}
