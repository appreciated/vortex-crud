package com.github.appreciated.turbo_crud.core.ui.factories.item;

/**
 * Interface for factories that create EntityItemRenderer instances.
 * This factory provides the appropriate renderer based on the configuration specified in the ItemRendererConfig.
 */

public interface TurboCrudItemFactoryRegistry<FieldId> {
    TurboCrudItemFactory<FieldId> getFactory(Class<? extends TurboCrudItemFactory> factory);

    void addFactory(Class<? extends TurboCrudItemFactory<FieldId>> key, TurboCrudItemFactory<FieldId> factory);
}
