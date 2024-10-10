package com.github.appreciated.flow_cms.ui.factories.item;

import com.github.appreciated.flow_cms.config.model.ItemFactoryConfig;

/**
 * Interface for factories that create EntityItemRenderer instances.
 * This factory provides the appropriate renderer based on the configuration specified in the ItemRendererConfig.
 */

public interface FlowCmsItemFactoryRegistry {
    FlowCmsItemFactory getFactory(ItemFactoryConfig routeConfig);
}
