package com.github.appreciated.flow_cms.ui.entity_detail;

import com.github.appreciated.flow_cms.config.model.RouteConfig;
import com.github.appreciated.flow_cms.service.GenericEntity;
import com.vaadin.flow.component.Component;

public interface EntityDetailRenderer {
    Component renderDetail(RouteConfig routeConfig, GenericEntity entity);
}
