package com.github.appreciated.vortex_crud.core.security;

import com.github.appreciated.vortex_crud.core.config.model.AccessControlled;
import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValue;

/**
 * Interface for checking Role-Based Access Control (RBAC) permissions.
 * Implementations should check user roles against resource permissions.
 * This interface is in core to avoid circular dependencies.
 */
public interface VortexCrudRbacPermissionChecker<ModelClass, FieldType, RepositoryType> {


    enum FieldAccessLevel {
        NONE,       // No access
        READ_ONLY,  // Can view but not modify
        WRITE       // Can view and modify
    }

    /**
     * Checks if the current user is authenticated.
     *
     * @return true if user is authenticated, false otherwise
     */
    boolean isAuthenticated();

    /**
     * Gets the current authenticated user entity from the data store.
     * This provides access to the full user object, not just roles.
     *
     * @return The current user entity, or null if not authenticated or IAM not configured
     */
    Object currentUserEntity();

    /**
     * Checks if the current user can write to the resource.
     *
     * @param resource The access-controlled resource to check
     * @return true if the user has WRITE access, false otherwise
     */
    boolean hasUserWriteAccessToRoute(AccessControlled resource);

    /**
     * Checks if the current user can write to the resource in a specific context.
     *
     * @param resource The access-controlled resource to check
     * @param context The context entity
     * @return true if the user has WRITE access, false otherwise
     */
    default boolean hasUserWriteAccessToRoute(AccessControlled resource, Object context) {
        return hasUserWriteAccessToRoute(resource);
    }

    boolean hasUserReadAccessToRoute(AccessControlled resource);

    FieldAccessLevel getUserFieldAccess(AccessControlled resource, Field<? extends ModelClass, FieldType, RepositoryType> field);

    /**
     * Applies security settings to a UI component based on the access level.
     *
     * @param component   The component to secure
     * @param accessLevel The access level to apply
     */
    default void applySecurityToComponent(Component component, FieldAccessLevel accessLevel) {
        if (accessLevel == FieldAccessLevel.READ_ONLY) {
            if (component instanceof HasValue) {
                ((HasValue<?, ?>) component).setReadOnly(true);
            }
        }
    }
}
