package com.github.appreciated.vortex_crud.core.config;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Hierarchical context that tracks entity IDs as you navigate through nested routes.
 *
 * For example, given URL: /projects/123/tasks/124/subtasks
 * - At /projects: context = {}
 * - At /projects/123: context = {projects: 123}
 * - At /projects/123/tasks: context = {projects: 123}
 * - At /projects/123/tasks/124: context = {projects: 123, tasks: 124}
 * - At /projects/123/tasks/124/subtasks: context = {projects: 123, tasks: 124}
 */
public class RouteIdContext {

    private final Map<String, Object> routeIdMap;

    public RouteIdContext() {
        this.routeIdMap = new LinkedHashMap<>();
    }

    private RouteIdContext(Map<String, Object> routeIdMap) {
        this.routeIdMap = new LinkedHashMap<>(routeIdMap);
    }

    /**
     * Get the entity ID for a specific route name.
     *
     * @param routeName the route name (e.g., "projects", "tasks")
     * @return the entity ID, or null if not found
     */
    public Object getIdForRoute(String routeName) {
        return routeIdMap.get(routeName);
    }

    /**
     * Check if the context has an ID for a specific route.
     */
    public boolean hasIdForRoute(String routeName) {
        return routeIdMap.containsKey(routeName);
    }

    /**
     * Create a new context with an additional route ID mapping.
     * This creates a new instance without modifying the original.
     *
     * @param routeName the route name
     * @param id the entity ID
     * @return a new RouteIdContext with the added mapping
     */
    public RouteIdContext withRouteId(String routeName, Object id) {
        RouteIdContext newContext = new RouteIdContext(this.routeIdMap);
        newContext.routeIdMap.put(routeName, id);
        return newContext;
    }

    /**
     * Get all route IDs in the context as an unmodifiable map.
     */
    public Map<String, Object> getAllRouteIds() {
        return Collections.unmodifiableMap(routeIdMap);
    }

    /**
     * Check if the context is empty (no route IDs).
     */
    public boolean isEmpty() {
        return routeIdMap.isEmpty();
    }

    /**
     * Get the immediate parent entity ID from the context.
     * Returns the last (most recently added) non-null entity ID, which represents
     * the immediate parent in the route hierarchy.
     *
     * For example, in context {projects: 123, tasks: 456}, returns 456 (the task ID).
     *
     * @return the last non-null entity ID found, or null if none exist
     */
    public Object getImmediateParentId() {
        Object lastId = null;
        for (Map.Entry<String, Object> entry : routeIdMap.entrySet()) {
            if (entry.getValue() != null) {
                lastId = entry.getValue();
            }
        }
        return lastId;
    }

    @Override
    public String toString() {
        return "RouteIdContext" + routeIdMap;
    }
}
