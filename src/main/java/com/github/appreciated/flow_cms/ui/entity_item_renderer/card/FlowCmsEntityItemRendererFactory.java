package com.github.appreciated.flow_cms.ui.entity_item_renderer.card;

import com.github.appreciated.flow_cms.config.model.ItemRendererConfig;

public interface FlowCmsEntityItemRendererFactory {
    EntityItemRenderer getRenderer(ItemRendererConfig routeConfig);
}
