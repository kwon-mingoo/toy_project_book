package com.example.bookmanagement.controller;

import com.example.bookmanagement.domain.Book;
import com.example.bookmanagement.dto.BookSearchDto;
import com.example.bookmanagement.dto.PageDto;
import com.example.bookmanagement.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 도서 REST API 컨트롤러
 *
 * @RestController = @Controller + @ResponseBody
 * 반환 객체를 Jackson이 자동으로 JSON으로 직렬화하여 응답
 *
 * 일반 MVC 컨트롤러와의 차이:
 *  - BookController: 뷰 이름(String) 반환 → Thymeleaf가 HTML 렌더링
 *  - BookApiController: 객체 반환 → Jackson이 JSON 직렬화
 *
 * 주요 용도: jQuery Ajax 호출, 외부 시스템 연동, SPA 프론트엔드 연동
 */
@Slf4j
@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookApiController {

    private final BookService bookService;

    /**
     * 도서 목록 JSON 조회
     * GET /api/books?title=&author=&categoryId=&page=1
     * 응답 예시: { "content": [...], "totalElements": 20, "totalPages": 2, ... }
     */
    @GetMapping
    public ResponseEntity<PageDto<Book>> getBooks(BookSearchDto searchDto) {
        log.debug("API 도서 목록 요청 - searchDto: {}", searchDto);
        PageDto<Book> page = bookService.getBookList(searchDto);
        return ResponseEntity.ok(page);
    }

    /**
     * 도서 단건 JSON 조회
     * GET /api/books/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBook(@PathVariable Long id) {
        log.debug("API 도서 상세 요청 - id: {}", id);
        return ResponseEntity.ok(bookService.getBookById(id));
    }
}
