package com.github.appreciated.vortex_crud.core.security;

import com.github.appreciated.vortex_crud.core.config.model.AccessControlled;

import java.util.Set;

/**
 * Interface for checking Role-Based Access Control (RBAC) permissions.
 * Implementations should check user roles against resource permissions.
 * This interface is in core to avoid circular dependencies.
 */
public interface RbacPermissionChecker {

    /**
     * Access level for a resource.
     */
    enum AccessLevel {
        NONE,       // No access
        READ_ONLY,  // Can view but not modify
        WRITE       // Can view and modify
    }

    /**
     * Determines the access level for the current user on an access-controlled resource.
     *
     * @param resource The access-controlled resource (route, field, etc.)
     * @return The access level (NONE, READ_ONLY, or WRITE)
     */
    AccessLevel getAccessLevel(AccessControlled resource);

    /**
     * Checks if the current user can write to (modify) the resource.
     *
     * @param resource The access-controlled resource
     * @return true if user has write access, false otherwise
     */
    boolean canWrite(AccessControlled resource);

    /**
     * Checks if the current user can read the resource.
     *
     * @param resource The access-controlled resource
     * @return true if user has at least read-only access, false otherwise
     */
    boolean canRead(AccessControlled resource);

    /**
     * Checks if the current user has any access to the resource.
     *
     * @param resource The access-controlled resource
     * @return true if user has any access (read or write), false otherwise
     */
    boolean hasAccess(AccessControlled resource);

    /**
     * Checks if the current user is authenticated.
     *
     * @return true if user is authenticated, false otherwise
     */
    boolean isAuthenticated();

    /**
     * Gets the current authenticated user's roles.
     *
     * @return Set of role names
     */
    Set<String> getCurrentUserRoles();
}