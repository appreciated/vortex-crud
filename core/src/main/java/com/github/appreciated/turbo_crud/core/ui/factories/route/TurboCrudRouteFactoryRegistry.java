package com.github.appreciated.turbo_crud.core.ui.factories.route;


import com.github.appreciated.turbo_crud.core.config.model.Route;

/**
 * Interface for factories that create view containers for different route configurations.
 * Implementations should provide methods for generating components based on RouteConfig.
 */

public interface TurboCrudRouteFactoryRegistry<DataStoreId, FieldId> {
    TurboCrudRouteFactory<DataStoreId, FieldId> getFactory(Class<? extends TurboCrudRouteFactory<DataStoreId, FieldId>> factory);

    void addFactory(Class<? extends TurboCrudRouteFactory<DataStoreId, FieldId>> key, TurboCrudRouteFactory<DataStoreId, FieldId> factory);

    boolean isContainerRoute(Route<DataStoreId, FieldId> currentRoute);
}
