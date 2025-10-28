package com.github.appreciated.vortex_crud.security.core.config;

import com.vaadin.flow.server.auth.AccessCheckResult;
import com.vaadin.flow.server.auth.NavigationAccessChecker;
import com.vaadin.flow.server.auth.NavigationContext;
import org.springframework.stereotype.Component;

@Component
class CustomAccessChecker implements NavigationAccessChecker {

    @Override
    public AccessCheckResult check(NavigationContext context) {
        return context.allow();
    }
}