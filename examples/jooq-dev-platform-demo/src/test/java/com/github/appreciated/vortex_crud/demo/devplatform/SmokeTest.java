package com.github.appreciated.vortex_crud.demo.devplatform;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
    "spring.datasource.url=jdbc:sqlite::memory:",
    "app.ssh.port=0"
})
class SmokeTest {

    @Test
    void contextLoads() {
    }
}
