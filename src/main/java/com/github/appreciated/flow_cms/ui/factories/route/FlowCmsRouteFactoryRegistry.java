package com.github.appreciated.flow_cms.ui.factories.route;

import com.github.appreciated.flow_cms.config.model.RouteConfig;
import com.github.appreciated.flow_cms.ui.factories.item.FlowCmsItemFactory;
import com.vaadin.flow.component.Component;

/**
 * Interface for factories that create view containers for different route configurations.
 * Implementations should provide methods for generating components based on RouteConfig.
 */

public interface FlowCmsRouteFactoryRegistry {
    FlowCmsRouteFactory getFactory(RouteConfig config);
    void addFactory(String key, FlowCmsRouteFactory factory);
}
