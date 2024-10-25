package com.github.appreciated.turbo_crud.ui.factories.item;

/**
 * Interface for factories that create EntityItemRenderer instances.
 * This factory provides the appropriate renderer based on the configuration specified in the ItemRendererConfig.
 */

public interface TurboCrudItemFactoryRegistry {
    TurboCrudItemFactory getFactory(String factory);

    void addFactory(String key, TurboCrudItemFactory factory);
}
