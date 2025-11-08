package com.github.appreciated.vortex_crud.core.config.model;

/**
 * Unified hook interface for all DataStore lifecycle events.
 * Hooks can intercept create, read, update, and delete operations.
 *
 * @param <ModelClass> The entity type
 */
@FunctionalInterface
public interface DataStoreHook<ModelClass> {
    /**
     * Executes the hook logic.
     *
     * @param context The hook context containing the entity and operation control
     */
    void execute(ModelClass context);
}
