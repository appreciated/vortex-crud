package com.github.appreciated.vortex_crud.core.ui.factories.route;

import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;

/**
 * Interface for factories that create view containers for different route configurations.
 * Implementations should provide methods for generating components based on RouteConfig.
 */

public interface VortexCrudRouteFactoryRegistry<ModelClass, FieldType, RepositoryType> {
    VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType> getFactory(Class<? extends VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType>> factory);

    void addFactory(Class<? extends VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType>> key, VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType> factory);

    boolean isContainerRoute(RouteRenderer<ModelClass, FieldType, RepositoryType> currentRouteRenderer);
}
