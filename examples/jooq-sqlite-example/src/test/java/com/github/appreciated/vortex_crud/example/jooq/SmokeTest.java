package com.github.appreciated.vortex_crud.example.jooq;

import org.jooq.DSLContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.github.appreciated.vortex_crud.jooq.models.tables.Users.USERS;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(properties = "spring.datasource.url=jdbc:sqlite::memory:")
class SmokeTest {

    @Autowired
    private DSLContext dsl;

    @Test
    void contextLoads() {
        int count = dsl.fetchCount(dsl.selectFrom(USERS).where(USERS.USERNAME.eq("admin@example.com")));
        assertTrue(count > 0, "Admin user should exist");
    }
}
