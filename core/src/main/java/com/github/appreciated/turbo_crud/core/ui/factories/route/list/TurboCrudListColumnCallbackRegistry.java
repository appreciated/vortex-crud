package com.github.appreciated.turbo_crud.core.ui.factories.route.list;


import com.github.appreciated.turbo_crud.core.config.model.RouteRenderer;

/**
 * Interface for factories that create view containers for different route configurations.
 * Implementations should provide methods for generating components based on RouteConfig.
 */

public interface TurboCrudListColumnCallbackRegistry<DataStoreId, FieldId> {
    TurboCrudListColumnCallback<DataStoreId, FieldId> getCallback(RouteRenderer<DataStoreId, FieldId> config);

    void addCallback(String key, TurboCrudListColumnCallback<DataStoreId, FieldId> factory);
}
