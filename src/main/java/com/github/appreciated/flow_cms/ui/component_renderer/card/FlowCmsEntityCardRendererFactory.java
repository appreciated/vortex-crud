package com.github.appreciated.flow_cms.ui.component_renderer.card;

import com.github.appreciated.flow_cms.config.model.CardRendererConfig;

public interface FlowCmsEntityCardRendererFactory {
    CardRenderer getCardRenderer(CardRendererConfig routeConfig);
}
