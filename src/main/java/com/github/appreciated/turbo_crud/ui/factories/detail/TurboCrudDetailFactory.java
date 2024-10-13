package com.github.appreciated.turbo_crud.ui.factories.detail;

import com.github.appreciated.turbo_crud.config.model.DetailFactory;
import com.github.appreciated.turbo_crud.service.GenericEntity;
import com.vaadin.flow.component.Component;

/**
 * Interface for rendering the details of an entity.
 * Classes implementing this interface should provide a method for rendering a detail component
 * based on a given route configuration and entity data.
 */

public interface TurboCrudDetailFactory {
    Component renderDetail(String table, String title, DetailFactory detailFactory, GenericEntity entity, boolean isWrapped, boolean hideHeader, TurboCrudDetailFactoryRegistry detailFactoryRegistry);
}
