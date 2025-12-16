package com.github.appreciated.vortex_crud.example.jpa;

import com.github.appreciated.vortex_crud.example.jpa.entity.User;
import com.github.appreciated.vortex_crud.example.jpa.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class SmokeTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void contextLoads() {
        User probe = new User();
        probe.setUsername("admin@example.com");
        assertTrue(userRepository.exists(Example.of(probe)), "Admin user should exist");
    }
}
