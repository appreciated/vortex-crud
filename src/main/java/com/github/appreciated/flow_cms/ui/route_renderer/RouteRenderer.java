package com.github.appreciated.flow_cms.ui.route_renderer;

import com.github.appreciated.flow_cms.service.DynamicEntityManagerService;
import com.typesafe.config.ConfigObject;
import com.vaadin.flow.component.Component;


public interface RouteRenderer {
    Component renderRoute(int i, ConfigObject config, DynamicEntityManagerService entityManagerService);
}
