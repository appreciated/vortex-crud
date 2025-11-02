package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactory;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.function.SerializableSupplier;

/**
 * Base interface for all route renderers. Child navigation specifics are defined in specialized sub-interfaces.
 */
public interface RouteRenderer<ModelClass, FieldType, RepositoryType> extends AccessControlled {

    RepositoryType getDataStoreKey();

    String getTitle();

    boolean isDefaultRoute();

    Class<? extends VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType>> getFactory();

    boolean isHiddenInMenu();

    RouteConfig<FieldType> getConfiguration();

    SerializableSupplier<Component> getIconFactory();
}
