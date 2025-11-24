package com.github.appreciated.vortex_crud.security.core.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.assertNull;

class SpringSecurityLogoutServiceTest {

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void testLogout() {
        // Set up a context
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("user", "pass"));

        SpringSecurityLogoutService service = new SpringSecurityLogoutService();
        service.logout();

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}
