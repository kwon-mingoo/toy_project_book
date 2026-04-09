package com.example.bookmanagement.domain;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.util.Date;

/**
 * 도서 도메인 클래스
 * Bean Validation 어노테이션을 함께 선언하여 등록/수정 시 유효성 검사에 활용
 */
@Data
public class Book {

    private Long bookId;    // 도서 ID (PK, Oracle 시퀀스 사용)

    @NotBlank(message = "제목은 필수입니다.")
    @Size(max = 200, message = "제목은 200자 이내여야 합니다.")
    private String title;   // 제목

    @NotBlank(message = "저자는 필수입니다.")
    @Size(max = 100, message = "저자는 100자 이내여야 합니다.")
    private String author;  // 저자

    @Size(max = 100, message = "출판사는 100자 이내여야 합니다.")
    private String publisher;   // 출판사

    @Min(value = 0, message = "가격은 0 이상이어야 합니다.")
    private Integer price;      // 가격

    @Min(value = 0, message = "재고는 0 이상이어야 합니다.")
    private Integer stock;      // 재고

    private Long categoryId;    // 카테고리 ID (FK)

    // MyBatis JOIN 결과로 채워지는 필드 (category 테이블 JOIN)
    private String categoryName;

    private Date createdAt;     // 등록일시
    private Date updatedAt;     // 수정일시
}
