package com.github.appreciated.vortex_crud.security.core.config;

import com.github.appreciated.vortex_crud.core.config.model.AccessControlled;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Component for checking Role-Based Access Control (RBAC) permissions.
 * Used by both route-level and field-level authorization checks.
 * Implements the core interface to avoid circular dependencies.
 */
@Component
public class RbacPermissionChecker implements com.github.appreciated.vortex_crud.core.security.RbacPermissionChecker {

    private VortexCrudConfigService<?, ?, ?> configService;

    public RbacPermissionChecker(VortexCrudConfigService<?, ?, ?> configService) {
        this.configService = configService;
    }

    /**
     * Determines the access level for the current user on an access-controlled resource.
     */
    public AccessLevel getAccessLevel(AccessControlled resource) {
        if (resource == null) {
            return AccessLevel.WRITE;
        }

        List<String> writeRoles = resource.writeRoles();
        List<String> readOnlyRoles = resource.readOnlyRoles();

        // If no roles specified, grant full access
        if ((writeRoles == null || writeRoles.isEmpty()) &&
            (readOnlyRoles == null || readOnlyRoles.isEmpty())) {
            return AccessLevel.WRITE;
        }

        Set<String> userRoles = getCurrentUserRoles();

        // Check write access first
        if (writeRoles != null && !writeRoles.isEmpty()) {
            if (writeRoles.stream().anyMatch(userRoles::contains)) {
                return AccessLevel.WRITE;
            }
        }

        // Check read-only access
        if (readOnlyRoles != null && !readOnlyRoles.isEmpty()) {
            if (readOnlyRoles.stream().anyMatch(userRoles::contains)) {
                return AccessLevel.READ_ONLY;
            }
        }

        return AccessLevel.NONE;
    }

    /**
     * Checks if the current user can write to the resource.
     */
    public boolean canWrite(AccessControlled resource) {
        return getAccessLevel(resource) == AccessLevel.WRITE;
    }

    /**
     * Checks if the current user can read the resource.
     */
    public boolean canRead(AccessControlled resource) {
        AccessLevel level = getAccessLevel(resource);
        return level == AccessLevel.READ_ONLY || level == AccessLevel.WRITE;
    }

    /**
     * Checks if the current user has any access to the resource.
     */
    public boolean hasAccess(AccessControlled resource) {
        return getAccessLevel(resource) != AccessLevel.NONE;
    }

    /**
     * Checks if the current user is authenticated.
     */
    public boolean isAuthenticated() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.isAuthenticated()
                && !"anonymousUser".equals(auth.getPrincipal());
    }

    /**
     * Gets the current authenticated user's roles.
     */
    public Set<String> getCurrentUserRoles() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return Set.of();
        }

        return auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(this::stripRolePrefix)
                .collect(Collectors.toSet());
    }

    /**
     * Removes the "ROLE_" prefix from role names if present.
     */
    private String stripRolePrefix(String role) {
        if (role != null && role.startsWith("ROLE_")) {
            return role.substring(5);
        }
        return role;
    }
}