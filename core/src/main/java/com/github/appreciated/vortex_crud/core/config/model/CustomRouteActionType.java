package com.github.appreciated.vortex_crud.core.config.model;

/**
 * Defines the type of custom route action based on required entity selection.
 */
public enum CustomRouteActionType {
    /**
     * Global action that doesn't require any entity selection.
     * Examples: Import Data, Generate Report, Export All
     */
    GLOBAL,

    /**
     * Action that operates on a single selected entity.
     * The action button will be disabled if no entity or multiple entities are selected.
     * Examples: Approve, Archive, Send Email
     */
    SINGLE_ENTITY,

    /**
     * Action that operates on one or more selected entities.
     * The action button will be disabled if no entities are selected.
     * Examples: Bulk Delete, Export Selected, Bulk Update
     */
    MULTI_ENTITY
}
