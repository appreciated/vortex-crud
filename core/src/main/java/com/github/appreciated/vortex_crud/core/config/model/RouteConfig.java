package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.ui.factories.item.VortexCrudItemFactory;

/**
 * Minimal interface representing shared route configuration properties.
 */
public interface RouteConfig<FieldType> {

    /**
     * @return the item factory responsible for creating route items
     */
    Class<? extends VortexCrudItemFactory<FieldType>> getFactory();
}

