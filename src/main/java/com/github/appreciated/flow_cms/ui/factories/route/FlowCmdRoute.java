package com.github.appreciated.flow_cms.ui.factories.route;

import com.github.appreciated.flow_cms.config.model.RouteConfig;
import com.github.appreciated.flow_cms.service.DynamicEntityManagerService;
import com.vaadin.flow.component.Component;

/**
 * Interface for rendering a route based on the provided configuration and entity management service.
 * Implementations should return a component representing the rendered view for the specified route.
 */

public interface FlowCmdRoute {
    Component renderRoute(int currentEntityId, RouteConfig config, DynamicEntityManagerService entityManagerService);
}
