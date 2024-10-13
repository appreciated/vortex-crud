package com.github.appreciated.flow_cms.ui.factories.route;

import com.github.appreciated.flow_cms.config.model.RouteConfig;

/**
 * Interface for factories that create view containers for different route configurations.
 * Implementations should provide methods for generating components based on RouteConfig.
 */

public interface TurboCrudRouteFactoryRegistry {
    TurboCrudRouteFactory getFactory(RouteConfig config);
    void addFactory(String key, TurboCrudRouteFactory factory);
}
