package com.github.appreciated.vortex_crud.security.core.config;

import com.github.appreciated.vortex_crud.core.config.model.AccessControlled;
import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.github.appreciated.vortex_crud.core.config.model.IdentityAndAccessManagement;
import com.github.appreciated.vortex_crud.core.security.RbacPermissionChecker;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService;
import org.springframework.stereotype.Component;

/**
 * Component for checking Role-Based Access Control (RBAC) permissions.
 * Used by both route-level and field-level authorization checks.
 * Implements the core interface to avoid circular dependencies.
 */
@Component
public class LocalStorageRbacPermissionChecker implements RbacPermissionChecker {

    private VortexCrudConfigService<?, ?, ?> configService;

    public LocalStorageRbacPermissionChecker(VortexCrudConfigService<?, ?, ?> configService) {
        this.configService = configService;
    }

    @Override
    public boolean isAuthenticated() {
        return false;
    }

    /**
     * Gets the current authenticated user entity from the data store.
     * Delegates to IdentityAndAccessManagement if configured.
     */
    @Override
    public Object getCurrentUserEntity() {
        if (configService == null) {
            return null;
        }

        IdentityAndAccessManagement<?, ?, ?> iam = configService.configuration().identityAndAccessManagement();
        if (iam == null) {
            return null;
        }

        return iam.getCurrentUserEntity();
    }

    @Override
    public boolean hasUserWriteAccessToRoute(AccessControlled resource) {
        return false;
    }

    @Override
    public boolean hasUserReadAccessToRoute(AccessControlled resource) {
        return false;
    }

    @Override
    public FieldAccessLevel getUserFieldAccess(AccessControlled resource, Field field) {
        return null;
    }
}