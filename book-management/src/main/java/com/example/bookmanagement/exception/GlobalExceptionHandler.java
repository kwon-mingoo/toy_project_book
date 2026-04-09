package com.example.bookmanagement.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 전역 예외 처리 핸들러
 *
 * @ControllerAdvice: 모든 @Controller에서 발생한 예외를 한 곳에서 처리 (AOP 기반)
 * 장점:
 *  - 컨트롤러마다 try-catch를 작성할 필요 없음
 *  - 예외 유형별로 일관된 에러 페이지 제공
 *  - REST API용 @RestControllerAdvice와 구분 (여기서는 HTML 에러 페이지 반환)
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 잘못된 인수 예외 처리
     * 예: 존재하지 않는 도서 ID로 조회 시 발생
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgument(IllegalArgumentException e, Model model) {
        log.error("잘못된 요청: {}", e.getMessage());
        model.addAttribute("errorMessage", e.getMessage());
        return "error/404";     // templates/error/404.html
    }

    /**
     * 기타 예상치 못한 예외 처리
     * 운영 환경에서는 상세 오류 내용을 사용자에게 노출하지 않도록 주의
     */
    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, Model model) {
        log.error("서버 오류 발생: ", e);
        model.addAttribute("errorMessage", "서버 내부 오류가 발생했습니다. 잠시 후 다시 시도해 주세요.");
        return "error/500";     // templates/error/500.html
    }
}
