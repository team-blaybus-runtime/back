package com.init.global.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class QueryDslConfig {

    @Primary
    @Bean
    public JPAQueryFactory mysqlQueryFactory(@Qualifier("mysqlEntityManagerFactory") EntityManager entityManager) {
        return new JPAQueryFactory(entityManager);
    }

    @Bean
    public JPAQueryFactory postgresQueryFactory(@Qualifier("postgresEntityManagerFactory") EntityManager entityManager) {
        return new JPAQueryFactory(entityManager);
    }
}
