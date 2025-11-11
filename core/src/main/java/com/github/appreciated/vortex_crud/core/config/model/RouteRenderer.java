package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactory;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.function.SerializableSupplier;

import java.util.List;

/**
 * Base interface for all route renderers. Child navigation specifics are defined in specialized sub-interfaces.
 */
public interface RouteRenderer<ModelClass, FieldType, RepositoryType> extends AccessControlled {

    RepositoryType dataStoreKey();

    String title();

    boolean isDefaultRoute();

    Class<? extends VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType>> factory();

    boolean isHiddenInMenu();

    RouteRendererConfiguration<ModelClass, FieldType, RepositoryType> configuration();

    SerializableSupplier<Component> iconFactory();

    List<DataStoreDropdownMenuAction<FieldType, RepositoryType>> menuActions();

    /**
     * Custom action buttons that can be added to the route.
     * These actions provide full access to the data store and selected entities.
     *
     * @return List of custom actions
     */
    default List<CustomRouteAction<ModelClass>> customActions() {
        return List.of();
    }
}
