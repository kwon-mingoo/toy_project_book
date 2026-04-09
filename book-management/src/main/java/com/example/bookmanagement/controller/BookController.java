package com.example.bookmanagement.controller;

import com.example.bookmanagement.domain.Book;
import com.example.bookmanagement.dto.BookSearchDto;
import com.example.bookmanagement.dto.PageDto;
import com.example.bookmanagement.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 도서 View 컨트롤러
 * Thymeleaf 템플릿을 렌더링하여 HTML 페이지를 응답
 *
 * 일반 MVC 컨트롤러: @Controller → 반환값이 뷰 이름 (문자열)
 * REST 컨트롤러와의 차이: @RestController는 반환값을 JSON으로 직렬화
 */
@Slf4j
@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;  // 생성자 주입 (DI)

    /**
     * 도서 목록 조회
     * GET /books?title=&author=&categoryId=&page=1
     */
    @GetMapping
    public String list(BookSearchDto searchDto, Model model) {
        log.debug("도서 목록 페이지 요청 - searchDto: {}", searchDto);
        PageDto<Book> page = bookService.getBookList(searchDto);
        model.addAttribute("page", page);
        model.addAttribute("searchDto", searchDto);
        model.addAttribute("categories", bookService.getCategoryList());
        return "books/list";    // templates/books/list.html 렌더링
    }

    /**
     * 도서 상세 조회
     * GET /books/{id}
     */
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        log.debug("도서 상세 페이지 요청 - id: {}", id);
        model.addAttribute("book", bookService.getBookById(id));
        return "books/detail";
    }

    /**
     * 도서 등록 폼 화면
     * GET /books/new
     */
    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("book", new Book());
        model.addAttribute("categories", bookService.getCategoryList());
        model.addAttribute("mode", "create");
        return "books/form";
    }

    /**
     * 도서 등록 처리
     * POST /books
     * @Valid: Book 객체의 Bean Validation 어노테이션 실행
     * BindingResult: 유효성 검사 오류를 담는 객체 — @Valid 바로 뒤에 선언해야 함
     */
    @PostMapping
    public String create(@Valid @ModelAttribute Book book,
                         BindingResult bindingResult,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        // 유효성 검사 실패 시 폼으로 복귀
        if (bindingResult.hasErrors()) {
            log.warn("도서 등록 유효성 검사 실패: {}", bindingResult.getAllErrors());
            model.addAttribute("categories", bookService.getCategoryList());
            model.addAttribute("mode", "create");
            return "books/form";
        }
        bookService.createBook(book);
        // RedirectAttributes: PRG(Post-Redirect-Get) 패턴 — 새로고침 시 중복 등록 방지
        redirectAttributes.addFlashAttribute("successMessage", "도서가 등록되었습니다.");
        return "redirect:/books";
    }

    /**
     * 도서 수정 폼 화면
     * GET /books/{id}/edit
     */
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("book", bookService.getBookById(id));
        model.addAttribute("categories", bookService.getCategoryList());
        model.addAttribute("mode", "edit");
        return "books/form";
    }

    /**
     * 도서 수정 처리
     * POST /books/{id}/edit
     */
    @PostMapping("/{id}/edit")
    public String edit(@PathVariable Long id,
                       @Valid @ModelAttribute Book book,
                       BindingResult bindingResult,
                       Model model,
                       RedirectAttributes redirectAttributes) {
        // 경로 변수 ID를 Book 객체에 명시적으로 세팅
        book.setBookId(id);
        if (bindingResult.hasErrors()) {
            log.warn("도서 수정 유효성 검사 실패: {}", bindingResult.getAllErrors());
            model.addAttribute("categories", bookService.getCategoryList());
            model.addAttribute("mode", "edit");
            return "books/form";
        }
        bookService.updateBook(book);
        redirectAttributes.addFlashAttribute("successMessage", "도서가 수정되었습니다.");
        return "redirect:/books/" + id;
    }

    /**
     * 도서 삭제 처리
     * POST /books/{id}/delete
     * (HTML form은 GET/POST만 지원하므로 DELETE 대신 POST 사용)
     */
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        bookService.deleteBook(id);
        redirectAttributes.addFlashAttribute("successMessage", "도서가 삭제되었습니다.");
        return "redirect:/books";
    }
}
