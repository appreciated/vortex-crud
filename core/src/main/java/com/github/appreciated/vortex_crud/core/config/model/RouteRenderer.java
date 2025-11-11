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
     * Custom action components that can be added to the route.
     * Actions can be global, single-entity, or multi-entity based, and provide
     * full access to the data store and selected entities through the context.
     *
     * @return List of route actions
     */
    default List<RouteAction<ModelClass>> routeActions() {
        return List.of();
    }
}
