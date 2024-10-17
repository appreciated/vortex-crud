package com.github.appreciated.turbo_crud.ui.factories.item;

import com.github.appreciated.turbo_crud.service.GenericEntity;
import com.typesafe.config.Config;
import com.vaadin.flow.component.Component;

/**
 * Interface for rendering individual items of an entity.
 * Classes implementing this interface should provide a method for rendering items based on a given configuration and entity data.
 */

public interface TurboCrudItemFactory {
    Component renderItem(Config itemFactoryConfig, GenericEntity entity, Integer maxWidth);
}
