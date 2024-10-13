package com.github.appreciated.flow_cms.ui.factories.route.list;

import com.github.appreciated.flow_cms.config.model.RouteConfig;

/**
 * Interface for factories that create view containers for different route configurations.
 * Implementations should provide methods for generating components based on RouteConfig.
 */

public interface TurboCrudListColumnCallbackRegistry {
    TurboCrudListColumnCallback getCallback(RouteConfig config);
    void addCallback(String key, TurboCrudListColumnCallback factory);
}
