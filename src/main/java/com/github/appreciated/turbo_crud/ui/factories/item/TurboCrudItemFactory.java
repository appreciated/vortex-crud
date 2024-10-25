package com.github.appreciated.turbo_crud.ui.factories.item;

import com.github.appreciated.turbo_crud.config.model.ItemFactoryConfig;
import com.github.appreciated.turbo_crud.model.GenericEntity;
import com.vaadin.flow.component.Component;

/**
 * Interface for rendering individual items of an entity.
 * Classes implementing this interface should provide a method for rendering items based on a given configuration and entity data.
 */

public interface TurboCrudItemFactory {
    Component renderItem(ItemFactoryConfig itemFactoryConfig, GenericEntity entity, Integer maxWidth);
}
