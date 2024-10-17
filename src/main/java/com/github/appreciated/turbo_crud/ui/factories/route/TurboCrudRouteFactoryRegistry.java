package com.github.appreciated.turbo_crud.ui.factories.route;

/**
 * Interface for factories that create view containers for different route configurations.
 * Implementations should provide methods for generating components based on RouteConfig.
 */

public interface TurboCrudRouteFactoryRegistry {
    TurboCrudRouteFactory getFactory(String factory);
    void addFactory(String key, TurboCrudRouteFactory factory);
}
