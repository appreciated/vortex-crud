package com.github.appreciated.turbo_crud.ui.factories.route;

import com.github.appreciated.turbo_crud.config.model.Route;

/**
 * Interface for factories that create view containers for different route configurations.
 * Implementations should provide methods for generating components based on RouteConfig.
 */

public interface TurboCrudRouteFactoryRegistry {
    TurboCrudRouteFactory getFactory(String factory);
    void addFactory(String key, TurboCrudRouteFactory factory);

    boolean isContainerRoute(Route currentRoute);
}
