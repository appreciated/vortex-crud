package com.github.appreciated.flow_cms.ui.route_renderer;

import com.github.appreciated.flow_cms.config.model.RouteConfig;
import com.vaadin.flow.component.Component;

interface FlowCmsRouteRendererFactory {
    Component createViewContainer(RouteConfig config);
}
