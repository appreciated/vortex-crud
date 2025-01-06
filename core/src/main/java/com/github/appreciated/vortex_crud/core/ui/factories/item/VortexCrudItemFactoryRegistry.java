package com.github.appreciated.vortex_crud.core.ui.factories.item;

/**
 * Interface for factories that create EntityItemRenderer instances.
 * This factory provides the appropriate renderer based on the configuration specified in the ItemRendererConfig.
 */

public interface VortexCrudItemFactoryRegistry<FieldId> {
    VortexCrudItemFactory<FieldId> getFactory(Class<? extends VortexCrudItemFactory<FieldId>> factory);

    void addFactory(Class<? extends VortexCrudItemFactory<FieldId>> key, VortexCrudItemFactory<FieldId> factory);
}
