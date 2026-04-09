package com.example.bookmanagement.dto;

import lombok.Getter;
import java.util.List;

/**
 * 페이지네이션 결과 DTO
 * 제네릭 타입으로 다양한 도메인에 재사용 가능
 */
@Getter
public class PageDto<T> {

    private final List<T> content;        // 현재 페이지의 데이터 목록
    private final int currentPage;        // 현재 페이지 번호
    private final int totalPages;         // 전체 페이지 수
    private final long totalElements;     // 전체 데이터 건수
    private final int size;               // 페이지당 건수

    public PageDto(List<T> content, int currentPage, long totalElements, int size) {
        this.content = content;
        this.currentPage = currentPage;
        this.totalElements = totalElements;
        this.size = size;
        // 전체 페이지 수 계산 (올림 처리)
        this.totalPages = (int) Math.ceil((double) totalElements / size);
    }

    /** 이전 페이지 존재 여부 */
    public boolean hasPrevious() {
        return currentPage > 1;
    }

    /** 다음 페이지 존재 여부 */
    public boolean hasNext() {
        return currentPage < totalPages;
    }

    /**
     * 페이지 그룹의 시작 번호
     * 예) currentPage=7 → startPage=6 (6~10 그룹)
     */
    public int getStartPage() {
        return ((currentPage - 1) / 5) * 5 + 1;
    }

    /**
     * 페이지 그룹의 끝 번호
     * 예) totalPages=8, startPage=6 → endPage=8
     */
    public int getEndPage() {
        if (totalPages == 0) return 1;
        return Math.min(getStartPage() + 4, totalPages);
    }
}
