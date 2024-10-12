package com.github.appreciated.flow_cms.ui.factories.detail;

import com.github.appreciated.flow_cms.config.model.DetailFactory;
import com.github.appreciated.flow_cms.service.GenericEntity;
import com.vaadin.flow.component.Component;

/**
 * Interface for rendering the details of an entity.
 * Classes implementing this interface should provide a method for rendering a detail component
 * based on a given route configuration and entity data.
 */

public interface FlowCmsDetailFactory {
    Component renderDetail(String table, String title, DetailFactory detailFactory, GenericEntity entity, boolean isWrapped, boolean hideHeader, FlowCmsDetailFactoryRegistry detailFactoryRegistry);
}
