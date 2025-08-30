package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.ui.factories.item.VortexCrudItemFactory;

/**
 * Minimal interface representing shared route configuration properties.
 */
public interface RouteConfig<DataStoreId, FieldId, KeyType> {

    /**
     * @return the item factory responsible for creating route items
     */
    Class<? extends VortexCrudItemFactory<FieldId>> getFactory();
}

