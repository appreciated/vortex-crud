package com.github.appreciated.vortex_crud.security.core.config;

import com.github.appreciated.vortex_crud.core.config.VortexCrudDefaultRouteRedirectConfiguration;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.security.VortexCrudRbacPermissionChecker;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService;
import com.github.appreciated.vortex_crud.core.service.VortexCrudPermissionResolutionService;
import com.vaadin.flow.server.auth.AccessCheckResult;
import com.vaadin.flow.server.auth.NavigationAccessChecker;
import com.vaadin.flow.server.auth.NavigationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

/**
 * Custom access checker for Role-Based Access Control (RBAC) at the route level.
 * Uses VortexCrudPermissionResolutionService for proper nested route resolution.
 */
@Component
class VortexCrudNavigationAccessChecker<ModelClass, FieldType, RepositoryType> implements NavigationAccessChecker {

    private final VortexCrudConfigService<ModelClass, FieldType, RepositoryType> configService;
    private final VortexCrudRbacPermissionChecker<ModelClass, FieldType, RepositoryType> permissionChecker;
    private final VortexCrudPermissionResolutionService<ModelClass, FieldType, RepositoryType> resolutionService;
    private final VortexCrudDefaultRouteRedirectConfiguration<ModelClass, FieldType, RepositoryType> defaultRouteConfig;

    // Cache for default route to avoid repeated lookups
    private volatile RouteRenderer<ModelClass, FieldType, RepositoryType> cachedDefaultRoute;

    public VortexCrudNavigationAccessChecker(
            VortexCrudConfigService<ModelClass, FieldType, RepositoryType> configService,
            VortexCrudRbacPermissionChecker<ModelClass, FieldType, RepositoryType> permissionChecker,
            @Autowired(required = false) VortexCrudPermissionResolutionService<ModelClass, FieldType, RepositoryType> resolutionService,
            VortexCrudDefaultRouteRedirectConfiguration<ModelClass, FieldType, RepositoryType> defaultRouteConfig) {
        this.configService = configService;
        this.permissionChecker = permissionChecker;
        this.resolutionService = resolutionService;
        this.defaultRouteConfig = defaultRouteConfig;
    }

    @Override
    public AccessCheckResult check(NavigationContext context) {
        try {
            String path = context.getLocation().getPath();
            // System.err.println("Checking access for path: " + path);

            // Allow public routes (login, sign-up, access-denied)
            if (isPublicRoute(path)) {
                return context.allow();
            }

            // Check if IAM is enabled
            if (!isIAMEnabled()) {
                // If IAM is not enabled, allow all routes
                return context.allow();
            }

            // Check if user is authenticated
            if (!permissionChecker.isAuthenticated()) {
                return context.deny("Not authenticated");
            }

            // If resolution service is not available, allow access
            if (resolutionService == null) {
                return context.allow();
            }

            // Resolve path to route using the resolution service
            RouteRenderer<ModelClass, FieldType, RepositoryType> route;

            // Handle empty path (default route) with caching
            if (Objects.equals(path, "")) {
                Map.Entry<String, RouteRenderer<ModelClass, FieldType, RepositoryType>> defaultRouteEntry = defaultRouteConfig.getDefaultRouteEntry();
                if (defaultRouteEntry != null) {
                    route = defaultRouteEntry.getValue();
                } else {
                    return context.allow();
                }
            } else {
                route = resolutionService.resolveRouteForPath(path);
            }

            // If route is null, it's not managed by vortex-crud, allow it
            if (route == null) {
                // System.err.println("Route not resolved for path: " + path);
                return context.allow();
            }

            // Check if user has read access to the route
            if (permissionChecker.hasUserReadAccessToRoute(route)) {
                return context.allow();
            }

            // System.err.println("Access denied for path: " + path);
            // User does not have permission to access this route
            return context.deny("Insufficient permissions");
        } catch (Exception e) {
            // In case of any error, allow navigation to avoid breaking the application
            // Log the error in production
            System.err.println("Error in CustomAccessChecker: " + e.getMessage());
            e.printStackTrace();
            return context.allow();
        }
    }

    /**
     * Checks if the given path is a public route that should always be accessible.
     *
     * @param path The navigation path
     * @return true if the path is a public route, false otherwise
     */
    private boolean isPublicRoute(String path) {
        return path.startsWith("login")
                || path.startsWith("sign-up")
                || path.startsWith("access-denied");
    }

    /**
     * Checks if Identity and Access Management is enabled in the configuration.
     *
     * @return true if IAM is enabled, false otherwise
     */
    private boolean isIAMEnabled() {
        if (configService == null || configService.configuration() == null) {
            return false;
        }
        return configService.configuration().identityAndAccessManagement() != null;
    }
}