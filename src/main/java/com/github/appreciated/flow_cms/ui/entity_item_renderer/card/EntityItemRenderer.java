package com.github.appreciated.flow_cms.ui.entity_item_renderer.card;

import com.github.appreciated.flow_cms.config.model.ItemRendererConfig;
import com.github.appreciated.flow_cms.service.GenericEntity;
import com.vaadin.flow.component.Component;

public interface EntityItemRenderer {
    Component renderItem(ItemRendererConfig itemRendererConfig, GenericEntity entity, Integer maxWidth);
}
