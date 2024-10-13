package com.github.appreciated.turbo_crud.ui.factories.item;

import com.github.appreciated.turbo_crud.config.model.ItemFactoryConfig;

/**
 * Interface for factories that create EntityItemRenderer instances.
 * This factory provides the appropriate renderer based on the configuration specified in the ItemRendererConfig.
 */

public interface TurboCrudItemFactoryRegistry {
    TurboCrudItemFactory getFactory(ItemFactoryConfig routeConfig);

    void addFactory(String key, TurboCrudItemFactory factory);
}
