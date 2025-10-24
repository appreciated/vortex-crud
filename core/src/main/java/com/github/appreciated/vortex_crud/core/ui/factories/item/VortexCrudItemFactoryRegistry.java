package com.github.appreciated.vortex_crud.core.ui.factories.item;

/**
 * Interface for factories that create EntityItemRenderer instances.
 * This factory provides the appropriate renderer based on the configuration specified in the ItemRendererConfig.
 */

public interface VortexCrudItemFactoryRegistry<FieldType> {
    VortexCrudItemFactory<FieldType> getFactory(Class<? extends VortexCrudItemFactory<FieldType>> factory);

    void addFactory(Class<? extends VortexCrudItemFactory<FieldType>> key, VortexCrudItemFactory<FieldType> factory);
}
