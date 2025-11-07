package com.github.appreciated.vortex_crud.security.core.config;

import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService;
import com.vaadin.flow.server.auth.AccessCheckResult;
import com.vaadin.flow.server.auth.NavigationAccessChecker;
import com.vaadin.flow.server.auth.NavigationContext;
import org.springframework.stereotype.Component;

/**
 * Custom access checker for Role-Based Access Control (RBAC) at the route level.
 */
@Component
class CustomAccessChecker implements NavigationAccessChecker {

    private final VortexCrudConfigService<?, ?, ?> configService;
    private final LocalStorageRbacPermissionChecker permissionChecker;

    public CustomAccessChecker(VortexCrudConfigService<?, ?, ?> configService,
                               LocalStorageRbacPermissionChecker permissionChecker) {
        this.configService = configService;
        this.permissionChecker = permissionChecker;
    }

    @Override
    public AccessCheckResult check(NavigationContext context) {
        String path = context.getLocation().getPath();

        // Allow public routes
        if (path.startsWith("login") || path.startsWith("sign-up") || path.startsWith("access-denied")) {
            return context.allow();
        }

        // Check authentication
        if (!permissionChecker.isAuthenticated()) {
            return context.deny("Not authenticated");
        }

        // Find route configuration
        RouteRenderer<?, ?, ?> route = findRoute(path);
        if (route == null) {
            return context.allow();
        }

        // Check if user has access to the route
        if (permissionChecker.hasAccess(route)) {
            return context.allow();
        }

        // Redirect to custom access denied view by throwing exception
        return context.deny("Insufficient permissions");
    }

    private RouteRenderer<?, ?, ?> findRoute(String path) {
        String firstSegment = path.contains("/") ? path.substring(0, path.indexOf("/")) : path;
        return configService.configuration().getRouteRenderers().get(firstSegment);
    }
}