package com.example.bookmanagement.domain;

import lombok.Data;
import java.util.Date;

/**
 * 카테고리 도메인 클래스
 * @Data: @Getter + @Setter + @ToString + @EqualsAndHashCode + @RequiredArgsConstructor 통합
 */
@Data
public class Category {

    private Long categoryId;        // 카테고리 ID (PK)
    private String categoryName;    // 카테고리명
    private Date createdAt;         // 등록일시
}
