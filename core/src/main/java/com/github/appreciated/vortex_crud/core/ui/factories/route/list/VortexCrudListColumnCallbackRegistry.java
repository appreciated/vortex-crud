package com.github.appreciated.vortex_crud.core.ui.factories.route.list;


import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;

/**
 * Interface for factories that create view containers for different route configurations.
 * Implementations should provide methods for generating components based on RouteConfig.
 */

public interface VortexCrudListColumnCallbackRegistry<DataStoreId, FieldId> {
    VortexCrudListColumnCallback<DataStoreId, FieldId> getCallback(RouteRenderer<DataStoreId, FieldId> config);

    void addCallback(String key, VortexCrudListColumnCallback<DataStoreId, FieldId> factory);
}
