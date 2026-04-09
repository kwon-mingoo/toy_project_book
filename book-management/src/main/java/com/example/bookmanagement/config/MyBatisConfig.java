package com.example.bookmanagement.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis 설정 클래스
 *
 * @MapperScan: 지정된 패키지 아래의 인터페이스를 자동으로 Spring Bean(Mapper)으로 등록
 *              application.yml의 mybatis.mapper-locations 설정과 함께 동작
 *
 * 추가 커스터마이징이 필요한 경우 SqlSessionFactory 빈을 이 클래스에서 직접 정의할 수 있음
 */
@Configuration
@MapperScan("com.example.bookmanagement.mapper")
public class MyBatisConfig {
    // 기본 MyBatis 설정은 application.yml에서 관리
}
