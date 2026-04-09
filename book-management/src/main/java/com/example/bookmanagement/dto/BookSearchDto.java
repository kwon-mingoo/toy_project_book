package com.example.bookmanagement.dto;

import lombok.Data;

/**
 * 도서 검색 조건 DTO
 * Controller에서 검색 파라미터를 받아 MyBatis 쿼리에 전달
 */
@Data
public class BookSearchDto {

    private String title;       // 제목 검색어 (LIKE 검색)
    private String author;      // 저자 검색어 (LIKE 검색)
    private Long categoryId;    // 카테고리 필터 (NULL이면 전체)

    private int page = 1;   // 현재 페이지 번호 (기본값: 1)
    private int size = 10;  // 페이지당 건수 (기본값: 10)

    /**
     * Oracle OFFSET 계산
     * MyBatis XML에서 #{offset} 으로 사용
     * 예) page=2, size=10 → offset=10 (11번째 행부터 조회)
     */
    public int getOffset() {
        return (page - 1) * size;
    }
}
