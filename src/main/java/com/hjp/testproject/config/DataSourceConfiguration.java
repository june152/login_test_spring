package com.hjp.testproject.config;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zaxxer.hikari.HikariDataSource;

/**
 * application.yml 설정 사용
 * 
 * @ConfigurationProperties 어노테이션 사용 경로 = ("spring.datasource")
 * @Configuration -> 이 클래스가 Bean 구성 클래스라고 선언
 *                Bean 등록 시 @Bean -> 직접 제어하지 않는 외부 라이브러리 등을 Bean으로 등록
 * @Component -> 직접 작성한 Class를 Bean으로 등록
 */
@Configuration(proxyBeanMethods = false)
public class DataSourceConfiguration {

    @Bean
    @ConfigurationProperties("spring.datasource")
    DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    /**
     * Datasource : 커넥션 풀 생성
     * HikariCP : 커넥션 풀
     */

    @Bean
    @ConfigurationProperties("spring.datasource.hikari")
    HikariDataSource dataSource(DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

}
