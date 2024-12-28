package com.github.appreciated.turbo_crud.core.ui.factories.route;


import com.github.appreciated.turbo_crud.core.config.model.Route;

/**
 * Interface for factories that create view containers for different route configurations.
 * Implementations should provide methods for generating components based on RouteConfig.
 */

public interface TurboCrudRouteFactoryRegistry {
    TurboCrudRouteFactory getFactory(Class<? extends TurboCrudRouteFactory> factory);

    void addFactory(Class<? extends TurboCrudRouteFactory> key, TurboCrudRouteFactory factory);

    boolean isContainerRoute(Route currentRoute);
}
