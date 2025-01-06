package com.github.appreciated.vortex_crud.core.ui.factories.route;


import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;

/**
 * Interface for factories that create view containers for different route configurations.
 * Implementations should provide methods for generating components based on RouteConfig.
 */

public interface VortexCrudRouteFactoryRegistry<DataStoreId, FieldId> {
    VortexCrudRouteFactory<DataStoreId, FieldId> getFactory(Class<? extends VortexCrudRouteFactory<DataStoreId, FieldId>> factory);

    void addFactory(Class<? extends VortexCrudRouteFactory<DataStoreId, FieldId>> key, VortexCrudRouteFactory<DataStoreId, FieldId> factory);

    boolean isContainerRoute(RouteRenderer<DataStoreId, FieldId> currentRouteRenderer);
}
