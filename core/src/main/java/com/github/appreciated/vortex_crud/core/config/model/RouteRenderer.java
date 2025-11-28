package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.ui.actions.RouteAction;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactory;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.function.SerializableSupplier;

import java.util.List;

/**
 * Base interface for all route renderers. Child navigation specifics are defined in specialized sub-interfaces.
 */
public interface RouteRenderer<ModelClass, FieldType, RepositoryType> extends AccessControlled, HasDataStore<FieldType, ModelClass> {

    RepositoryType dataStoreKey();

    String title();

    boolean isDefaultRoute();

    Class<? extends VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType>> factory();

    boolean isHiddenInMenu();

    RouteRendererConfiguration<ModelClass, FieldType, RepositoryType> configuration();

    SerializableSupplier<Component> iconFactory();

    List<DataStoreDropdownMenuAction<FieldType, RepositoryType>> menuActions();

    /**
     * List of custom route actions with full access to data store and selected entities.
     * These actions will be rendered in the route header and automatically
     * enabled/disabled based on selection state.
     *
     * @return The list of route actions, or null if none
     */
    default List<RouteAction<FieldType, ModelClass>> routeActions() {
        return null;
    }
}
