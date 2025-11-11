package com.github.appreciated.vortex_crud.core.config.model;

import java.io.Serializable;

/**
 * Functional interface for handling custom route actions.
 * Implementations receive a context object with access to the data store,
 * selected entities, and UI utilities.
 *
 * @param <ModelClass> The type of entity
 */
@FunctionalInterface
public interface CustomRouteActionHandler<ModelClass> extends Serializable {

    /**
     * Executes the custom action.
     *
     * @param context The action context containing data store, selected entities, and UI utilities
     */
    void execute(CustomRouteActionContext<ModelClass> context);
}
