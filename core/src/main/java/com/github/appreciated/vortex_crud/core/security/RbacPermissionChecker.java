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

    /**
     * Checks if the current user has a specific role.
     *
     * @param role The role name to check
     * @return true if the user has the role, false otherwise
     */
    boolean currentUserHasRequiredRoles(String role);

    /**
     * Gets the current authenticated user entity from the data store.
     * This provides access to the full user object, not just roles.
     *
     * @return The current user entity, or null if not authenticated or IAM not configured
     */
    Object getCurrentUserEntity();

    /**
     * Determines the access level for the current user on an access-controlled resource.
     *
     * @param resource The access-controlled resource to check
     * @return The access level (NONE, READ_ONLY, or WRITE)
     */
    AccessLevel getAccessLevel(AccessControlled resource);

    /**
     * Checks if the current user can write to the resource.
     *
     * @param resource The access-controlled resource to check
     * @return true if the user has WRITE access, false otherwise
     */
    boolean canWrite(AccessControlled resource);

    /**
     * Checks if the current user can read the resource.
     *
     * @param resource The access-controlled resource to check
     * @return true if the user has READ_ONLY or WRITE access, false otherwise
     */
    boolean canRead(AccessControlled resource);

    /**
     * Checks if the current user has any access to the resource.
     *
     * @param resource The access-controlled resource to check
     * @return true if the user has any access level other than NONE, false otherwise
     */
    boolean hasAccess(AccessControlled resource);
}