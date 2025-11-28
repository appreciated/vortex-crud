package com.github.appreciated.vortex_crud.demo.projectmanagement;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "spring.datasource.url=jdbc:sqlite::memory:")
class SmokeTest {

    @Test
    void contextLoads() {
    }
}
