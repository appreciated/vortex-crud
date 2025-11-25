package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.ui.actions.RouteAction;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.list.ListRouteFactory;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.function.SerializableSupplier;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Accessors(fluent = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class ListRoute<ModelClass, FieldType, RepositoryType> implements RouteRendererSingleChild<ModelClass, FieldType, RepositoryType>, AccessControlled {

    private RepositoryType dataStoreKey;

    private String title;

    private boolean isDefaultRoute;

    @Builder.Default
    private Class<? extends VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType>> factory = (Class<? extends VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType>>) (Class<?>) ListRouteFactory.class;

    private VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType> factoryInstance;

    private boolean isHiddenInMenu;

    private RouteRendererConfiguration<ModelClass, FieldType, RepositoryType> configuration;

    private SerializableSupplier<Component> iconFactory;

    private List<String> writeRoles;

    private List<String> readOnlyRoles;

    private RouteRenderer<ModelClass, FieldType, RepositoryType> child;

    private List<InternalFormElement<ModelClass, FieldType, RepositoryType>> columns;

    /**
     * List of menu actions for adding custom components to the menu.
     * This can include dropdowns, filters, action buttons, etc.
     */
    private List<DataStoreDropdownMenuAction<FieldType, RepositoryType>> menuActions;

    /**
     * List of custom route actions with full access to data store and selected entities.
     * These actions will be rendered in the route header and automatically
     * enabled/disabled based on selection state.
     */
    private List<RouteAction<FieldType, ModelClass>> routeActions;
}