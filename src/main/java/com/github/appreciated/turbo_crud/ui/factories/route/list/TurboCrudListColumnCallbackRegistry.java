package com.github.appreciated.turbo_crud.ui.factories.route.list;

import com.github.appreciated.turbo_crud.config.model.Route;

/**
 * Interface for factories that create view containers for different route configurations.
 * Implementations should provide methods for generating components based on RouteConfig.
 */

public interface TurboCrudListColumnCallbackRegistry {
    TurboCrudListColumnCallback getCallback(Route config);
    void addCallback(String key, TurboCrudListColumnCallback factory);
}
