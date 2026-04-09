package com.example.bookmanagement.service;

import com.example.bookmanagement.domain.Book;
import com.example.bookmanagement.dto.BookSearchDto;
import com.example.bookmanagement.dto.PageDto;
import com.example.bookmanagement.mapper.BookMapper;
import com.example.bookmanagement.mapper.CategoryMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

/**
 * BookServiceImpl 단위 테스트
 *
 * @ExtendWith(MockitoExtension.class): JUnit5 + Mockito 통합
 * @Mock: 가짜(Mock) 객체 생성 — DB 연결 없이 서비스 로직만 테스트
 * @InjectMocks: Mock 객체를 주입받은 실제 서비스 구현체 생성
 *
 * 면접 포인트:
 *  - 단위 테스트 vs 통합 테스트의 차이
 *  - Mockito given/when/then (BDD 스타일) 패턴
 *  - verify(): 특정 메서드가 실제로 호출되었는지 검증
 */
@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookMapper bookMapper;

    @Mock
    private CategoryMapper categoryMapper;

    // bookMapper, categoryMapper Mock 객체가 생성자로 주입됨
    @InjectMocks
    private BookServiceImpl bookService;

    private Book testBook;

    @BeforeEach
    void setUp() {
        // 각 테스트 실행 전 공통 테스트 데이터 초기화
        testBook = new Book();
        testBook.setBookId(1L);
        testBook.setTitle("테스트 도서");
        testBook.setAuthor("테스트 저자");
        testBook.setPublisher("테스트 출판사");
        testBook.setPrice(15000);
        testBook.setStock(10);
        testBook.setCategoryId(1L);
    }

    @Test
    @DisplayName("도서 목록 조회 - 페이지네이션 정상 동작")
    void getBookList_success() {
        // given: Mock 행동 정의
        BookSearchDto searchDto = new BookSearchDto();
        List<Book> books = Arrays.asList(testBook);
        given(bookMapper.findAll(searchDto)).willReturn(books);
        given(bookMapper.countAll(searchDto)).willReturn(1L);

        // when: 실제 서비스 메서드 호출
        PageDto<Book> result = bookService.getBookList(searchDto);

        // then: 결과 검증
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getTotalElements()).isEqualTo(1L);
        assertThat(result.getTotalPages()).isEqualTo(1);
        assertThat(result.getCurrentPage()).isEqualTo(1);

        // Mock 객체의 메서드가 실제로 호출되었는지 검증
        verify(bookMapper).findAll(searchDto);
        verify(bookMapper).countAll(searchDto);
    }

    @Test
    @DisplayName("도서 단건 조회 - 정상 케이스")
    void getBookById_found() {
        // given
        given(bookMapper.findById(1L)).willReturn(testBook);

        // when
        Book result = bookService.getBookById(1L);

        // then
        assertThat(result.getBookId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("테스트 도서");
        assertThat(result.getAuthor()).isEqualTo("테스트 저자");
    }

    @Test
    @DisplayName("도서 단건 조회 - 존재하지 않는 ID → IllegalArgumentException 발생")
    void getBookById_notFound_throwsException() {
        // given
        given(bookMapper.findById(999L)).willReturn(null);

        // when & then: 예외 발생 여부 + 메시지 검증
        assertThatThrownBy(() -> bookService.getBookById(999L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("존재하지 않는 도서");
    }

    @Test
    @DisplayName("도서 등록 - BookMapper.insert 호출 검증")
    void createBook_success() {
        // given
        given(bookMapper.insert(testBook)).willReturn(1);

        // when
        bookService.createBook(testBook);

        // then: insert 메서드가 정확히 1번 호출되었는지 검증
        verify(bookMapper, times(1)).insert(testBook);
    }

    @Test
    @DisplayName("도서 수정 - 존재하는 도서 정상 수정")
    void updateBook_success() {
        // given
        given(bookMapper.findById(1L)).willReturn(testBook);
        given(bookMapper.update(testBook)).willReturn(1);

        // when
        bookService.updateBook(testBook);

        // then
        verify(bookMapper).findById(1L);    // 존재 확인 쿼리 호출 검증
        verify(bookMapper).update(testBook); // 수정 쿼리 호출 검증
    }

    @Test
    @DisplayName("도서 수정 - 존재하지 않는 도서 → 예외 발생, update 미호출")
    void updateBook_notFound_throwsException() {
        // given
        testBook.setBookId(999L);
        given(bookMapper.findById(999L)).willReturn(null);

        // when & then
        assertThatThrownBy(() -> bookService.updateBook(testBook))
                .isInstanceOf(IllegalArgumentException.class);

        // update는 호출되지 않아야 함
        verify(bookMapper, never()).update(any());
    }

    @Test
    @DisplayName("도서 삭제 - 정상 동작")
    void deleteBook_success() {
        // given
        given(bookMapper.findById(1L)).willReturn(testBook);
        given(bookMapper.delete(1L)).willReturn(1);

        // when
        bookService.deleteBook(1L);

        // then
        verify(bookMapper).findById(1L);
        verify(bookMapper).delete(1L);
    }
}
