package com.github.appreciated.vortex_crud.core.service;

import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;

/**
 * Service for resolving navigation paths to route configurations.
 * Uses VortexCrudPathToRouteResolver internally for proper nested route handling.
 */
public interface VortexCrudPermissionResolutionService<ModelClass, FieldType, RepositoryType> {

    /**
     * Resolves a navigation path to the corresponding route renderer.
     * Handles nested routes and wildcards properly using VortexCrudPathToRouteResolver.
     *
     * @param path The navigation path (e.g., "projects-cards/123")
     * @return The resolved route renderer, or null if the route cannot be resolved
     */
    RouteRenderer<?, ?, ?> resolveRouteForPath(String path);
}