package com.github.appreciated.vortex_crud.core.ui.factories.route;


import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;

/**
 * Interface for factories that create view containers for different route configurations.
 * Implementations should provide methods for generating components based on RouteConfig.
 */

public interface VortexCrudRouteFactoryRegistry<DataStoreId, FieldId, ModelClass> {
    VortexCrudRouteFactory<DataStoreId, FieldId, ModelClass> getFactory(Class<? extends VortexCrudRouteFactory<DataStoreId, FieldId, ModelClass>> factory);

    void addFactory(Class<? extends VortexCrudRouteFactory<DataStoreId, FieldId, ModelClass>> key, VortexCrudRouteFactory<DataStoreId, FieldId, ModelClass> factory);

    boolean isContainerRoute(RouteRenderer<DataStoreId, FieldId, ModelClass> currentRouteRenderer);
}
