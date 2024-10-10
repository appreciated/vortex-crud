package com.github.appreciated.flow_cms.ui.factories.route.list;

import com.github.appreciated.flow_cms.config.model.RouteConfig;
import com.github.appreciated.flow_cms.ui.factories.item.FlowCmsItemFactory;

/**
 * Interface for factories that create view containers for different route configurations.
 * Implementations should provide methods for generating components based on RouteConfig.
 */

public interface FlowCmsListColumnCallbackRegistry {
    FlowCmdListColumnCallback getCallback(RouteConfig config);
    void addCallback(String key, FlowCmdListColumnCallback factory);
}
