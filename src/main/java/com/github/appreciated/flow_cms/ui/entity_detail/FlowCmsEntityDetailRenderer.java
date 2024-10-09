package com.github.appreciated.flow_cms.ui.entity_detail;

import com.github.appreciated.flow_cms.config.model.RouteConfig;
import com.github.appreciated.flow_cms.service.GenericEntity;
import com.vaadin.flow.component.Component;

/**
 * Interface for rendering the details of an entity.
 * Classes implementing this interface should provide a method for rendering a detail component
 * based on a given route configuration and entity data.
 */

public interface FlowCmsEntityDetailRenderer {
    Component renderDetail(RouteConfig routeConfig, GenericEntity entity);
}
