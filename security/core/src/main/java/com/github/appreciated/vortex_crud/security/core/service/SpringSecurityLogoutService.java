package com.github.appreciated.vortex_crud.security.core.service;

import com.github.appreciated.vortex_crud.core.security.VortexCrudLogoutService;
import org.springframework.security.core.context.SecurityContextHolder;

public class SpringSecurityLogoutService implements VortexCrudLogoutService {
    @Override
    public void logout() {
        SecurityContextHolder.clearContext();
    }
}
