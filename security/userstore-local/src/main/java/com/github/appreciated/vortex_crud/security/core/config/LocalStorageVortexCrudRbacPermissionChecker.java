package com.github.appreciated.vortex_crud.security.core.config;

import com.github.appreciated.vortex_crud.core.config.model.AccessControlled;
import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.github.appreciated.vortex_crud.core.config.model.IdentityAndAccessManagement;
import com.github.appreciated.vortex_crud.core.security.VortexCrudRbacPermissionChecker;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService;
import com.github.appreciated.vortex_crud.security.core.service.LocalStorageUserContextService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * Component for checking Role-Based Access Control (RBAC) permissions.
 * Used by both route-level and field-level authorization checks.
 * Implements the core interface to avoid circular dependencies.
 */
@Component
public class LocalStorageVortexCrudRbacPermissionChecker<ModelClass, FieldType, RepositoryType>
        implements VortexCrudRbacPermissionChecker<ModelClass, FieldType, RepositoryType> {

    private final LocalStorageUserContextService<ModelClass, FieldType, RepositoryType> userContextService;
    private final VortexCrudConfigService<ModelClass, FieldType, RepositoryType> configService;

    public LocalStorageVortexCrudRbacPermissionChecker(
            LocalStorageUserContextService<ModelClass, FieldType, RepositoryType> userContextService,
            VortexCrudConfigService<ModelClass, FieldType, RepositoryType> configService) {
        this.userContextService = userContextService;
        this.configService = configService;
    }

    @Override
    public boolean isAuthenticated() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return false;
        }

        // Check if the user is authenticated and not anonymous
        return auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal());
    }

    /**
     * Gets the current authenticated user entity from the data store.
     * Delegates to LocalUserContextService.
     */
    @Override
    public Object currentUserEntity() {
        return userContextService.currentUserEntity();
    }

    /**
     * Gets the current user's roles from LocalUserContextService.
     *
     * @return Set of role names for the current user, or empty set if not authenticated
     */
    private Set<String> getCurrentUserRoles() {
        return userContextService.currentUserRoles();
    }

    @Override
    public boolean hasUserWriteAccessToRoute(AccessControlled resource) {
        if (resource == null) {
            return false;
        }

        // Get write roles from resource, or use defaults if not specified
        List<String> writeRoles = resource.writeRoles();
        if (writeRoles == null || writeRoles.isEmpty()) {
            // Use default write roles from IdentityAndAccessManagement
            IdentityAndAccessManagement<ModelClass, FieldType, RepositoryType> iam =
                configService.configuration().identityAndAccessManagement();
            if (iam != null) {
                writeRoles = iam.defaultWriteRoles();
            }
        }

        // If still no write roles are defined, deny access by default
        if (writeRoles == null || writeRoles.isEmpty()) {
            return false;
        }

        // Get current user's roles
        Set<String> userRoles = getCurrentUserRoles();
        if (userRoles.isEmpty()) {
            return false;
        }

        // Check if user has any of the required write roles
        return userRoles.stream().anyMatch(writeRoles::contains);
    }

    @Override
    public boolean hasUserReadAccessToRoute(AccessControlled resource) {
        if (resource == null) {
            return false;
        }

        // Get write roles and read-only roles from resource, or use defaults if not specified
        List<String> writeRoles = resource.writeRoles();
        List<String> readOnlyRoles = resource.readOnlyRoles();

        IdentityAndAccessManagement<ModelClass, FieldType, RepositoryType> iam = null;
        if (configService.configuration() != null) {
            iam = configService.configuration().identityAndAccessManagement();
        }

        // Use default roles if not specified
        if ((writeRoles == null || writeRoles.isEmpty()) && iam != null) {
            writeRoles = iam.defaultWriteRoles();
        }
        if ((readOnlyRoles == null || readOnlyRoles.isEmpty()) && iam != null) {
            readOnlyRoles = iam.defaultReadRoles();
        }

        // If neither write nor read roles are defined, deny access by default
        boolean hasWriteRoles = writeRoles != null && !writeRoles.isEmpty();
        boolean hasReadOnlyRoles = readOnlyRoles != null && !readOnlyRoles.isEmpty();

        if (!hasWriteRoles && !hasReadOnlyRoles) {
            return false;
        }

        // Get current user's roles
        Set<String> userRoles = getCurrentUserRoles();
        if (userRoles.isEmpty()) {
            return false;
        }

        // User has read access if they have either write access OR read-only access
        boolean hasWriteAccess = hasWriteRoles && userRoles.stream().anyMatch(writeRoles::contains);
        boolean hasReadAccess = hasReadOnlyRoles && userRoles.stream().anyMatch(readOnlyRoles::contains);

        return hasWriteAccess || hasReadAccess;
    }

    @Override
    public FieldAccessLevel getUserFieldAccess(AccessControlled route, Field<ModelClass, FieldType, RepositoryType> field) {
        if (route == null || field == null) {
            return FieldAccessLevel.NONE;
        }

        // First check if user has access to the route at all
        if (!hasUserReadAccessToRoute(route)) {
            return FieldAccessLevel.NONE;
        }

        // Get current user's roles
        Set<String> userRoles = getCurrentUserRoles();
        if (userRoles.isEmpty()) {
            return FieldAccessLevel.NONE;
        }

        // Check field-level permissions
        List<String> fieldWriteRoles = field.writeRoles();
        List<String> fieldReadOnlyRoles = field.readOnlyRoles();

        // If no field-level permissions are defined, inherit from route
        boolean hasFieldWriteRoles = fieldWriteRoles != null && !fieldWriteRoles.isEmpty();
        boolean hasFieldReadOnlyRoles = fieldReadOnlyRoles != null && !fieldReadOnlyRoles.isEmpty();

        if (!hasFieldWriteRoles && !hasFieldReadOnlyRoles) {
            // No field-level permissions, check route permissions
            return hasUserWriteAccessToRoute(route) ? FieldAccessLevel.WRITE : FieldAccessLevel.READ_ONLY;
        }

        // Check if user has write access to the field
        boolean hasFieldWriteAccess = hasFieldWriteRoles && userRoles.stream().anyMatch(fieldWriteRoles::contains);
        if (hasFieldWriteAccess) {
            // Also verify user has write access to the route
            return hasUserWriteAccessToRoute(route) ? FieldAccessLevel.WRITE : FieldAccessLevel.READ_ONLY;
        }

        // Check if user has read-only access to the field
        boolean hasFieldReadAccess = hasFieldReadOnlyRoles && userRoles.stream().anyMatch(fieldReadOnlyRoles::contains);
        if (hasFieldReadAccess) {
            return FieldAccessLevel.READ_ONLY;
        }

        // User has access to route but not to this specific field
        return FieldAccessLevel.NONE;
    }
}