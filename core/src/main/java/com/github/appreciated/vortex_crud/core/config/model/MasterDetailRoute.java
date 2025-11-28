package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.ui.actions.RouteAction;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.master_detail.MasterDetailRouteFactory;
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
public class MasterDetailRoute<ModelClass, FieldType, RepositoryType> implements RouteRendererSingleChild<ModelClass, FieldType, RepositoryType>, FormRouteProvider<ModelClass, FieldType, RepositoryType> {

    private RepositoryType dataStoreKey;
    private VortexCrudDataStore<FieldType, ModelClass> dataStoreInstance;

    private String title;

    private boolean isDefaultRoute;

    @Builder.Default
    private Class<? extends VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType>> factory= (Class<? extends VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType>>) (Class<?>) MasterDetailRouteFactory.class;

    private boolean isHiddenInMenu;

    private final boolean isDeleteButtonHidden = false;

    private RouteRendererConfiguration<ModelClass, FieldType, RepositoryType> configuration;

    private SerializableSupplier<Component> iconFactory;

    private List<String> writeRoles;

    private List<String> readOnlyRoles;

    private FormRouteProvider<ModelClass, FieldType, RepositoryType> child;

    @Override
    public FormRendererConfiguration<ModelClass, FieldType, RepositoryType> formConfiguration() {
        return child.formConfiguration();
    }

    private List<DataStoreDropdownMenuAction<FieldType, RepositoryType>> menuActions;

    /**
     * List of custom route actions with full access to data store and selected entities.
     * These actions will be rendered in the route header and automatically
     * enabled/disabled based on selection state.
     */
    private List<RouteAction<FieldType, ModelClass>> routeActions;
}