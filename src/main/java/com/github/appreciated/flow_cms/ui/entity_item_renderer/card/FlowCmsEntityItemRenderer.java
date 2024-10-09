package com.github.appreciated.flow_cms.ui.entity_item_renderer.card;

import com.github.appreciated.flow_cms.config.model.ItemRendererConfig;
import com.github.appreciated.flow_cms.service.GenericEntity;
import com.vaadin.flow.component.Component;

/**
 * Interface for rendering individual items of an entity.
 * Classes implementing this interface should provide a method for rendering items based on a given configuration and entity data.
 */

public interface FlowCmsEntityItemRenderer {
    Component renderItem(ItemRendererConfig itemRendererConfig, GenericEntity entity, Integer maxWidth);
}
