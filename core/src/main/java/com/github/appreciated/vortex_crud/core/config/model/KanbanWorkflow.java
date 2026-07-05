package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.service.VortexCrudContext;

/**
 * Jira-like workflow for a {@link KanbanRoute}: restricts which column transitions are
 * allowed when dragging cards. A card may always stay in its current column. If a route
 * has no workflow configured, all transitions are allowed.
 * <p>
 * Use {@link StaticKanbanWorkflow} to define the transitions in the application
 * configuration, or {@link DynamicKanbanWorkflow} to resolve them from a database table
 * at runtime so the flow can be changed without a redeploy.
 */
@FunctionalInterface
public interface KanbanWorkflow<ModelClass, FieldType, RepositoryType> {

    /**
     * @param from    the column value the dragged card currently has
     * @param to      the column value of the drop target
     * @param context the current context, e.g. for resolving definition rows
     * @return whether the transition is allowed
     */
    boolean isTransitionAllowed(Object from, Object to, VortexCrudContext<ModelClass, FieldType, RepositoryType> context);
}
