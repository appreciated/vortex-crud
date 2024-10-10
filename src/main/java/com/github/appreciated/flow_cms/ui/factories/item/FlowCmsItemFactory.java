package com.github.appreciated.flow_cms.ui.factories.item;

import com.github.appreciated.flow_cms.config.model.ItemFactoryConfig;
import com.github.appreciated.flow_cms.service.GenericEntity;
import com.vaadin.flow.component.Component;

/**
 * Interface for rendering individual items of an entity.
 * Classes implementing this interface should provide a method for rendering items based on a given configuration and entity data.
 */

public interface FlowCmsItemFactory {
    Component renderItem(ItemFactoryConfig itemFactoryConfig, GenericEntity entity, Integer maxWidth);
}
