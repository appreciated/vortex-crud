package com.github.appreciated.flow_cms.ui.factories.card;

import com.github.appreciated.flow_cms.config.model.ItemRendererConfig;

/**
 * Interface for factories that create EntityItemRenderer instances.
 * This factory provides the appropriate renderer based on the configuration specified in the ItemRendererConfig.
 */

public interface FlowCmsEntityItemRendererFactory {
    FlowCmsEntityItemRenderer getRenderer(ItemRendererConfig routeConfig);
}
