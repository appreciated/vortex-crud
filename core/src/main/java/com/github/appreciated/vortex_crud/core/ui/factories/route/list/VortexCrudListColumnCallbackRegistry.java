package com.github.appreciated.vortex_crud.core.ui.factories.route.list;

import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;

/**
 * Interface for factories that create view containers for different route configurations.
 * Implementations should provide methods for generating components based on RouteConfig.
 */

public interface VortexCrudListColumnCallbackRegistry<ModelClass, FieldType, RepositoryType> {
    VortexCrudListColumnCallback<ModelClass, FieldType, RepositoryType> getCallback(RouteRenderer<ModelClass, FieldType, RepositoryType> config);

    void addCallback(String key, VortexCrudListColumnCallback<ModelClass, FieldType, RepositoryType> factory);
}
