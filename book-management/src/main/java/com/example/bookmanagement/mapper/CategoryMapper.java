package com.example.bookmanagement.mapper;

import com.example.bookmanagement.domain.Category;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

/**
 * 카테고리 MyBatis Mapper 인터페이스
 * 실제 SQL은 resources/mapper/CategoryMapper.xml에 정의
 */
@Mapper
public interface CategoryMapper {

    /** 카테고리 전체 조회 (등록/수정 폼 SELECT BOX 용) */
    List<Category> findAll();

    /** 카테고리 단건 조회 */
    Category findById(Long categoryId);
}
