package com.github.appreciated.flow_cms.ui.route_renderer;

import com.github.appreciated.flow_cms.config.model.RouteConfig;
import com.vaadin.flow.component.Component;

/**
 * Interface for factories that create view containers for different route configurations.
 * Implementations should provide methods for generating components based on RouteConfig.
 */

public interface FlowCmsRouteRendererFactory {
    Component createViewContainer(RouteConfig config);
}
