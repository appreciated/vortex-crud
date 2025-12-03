package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.ui.actions.RouteAction;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.VortexCrudDialogFactory;
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

    private DataStoreConfig<ModelClass, FieldType, RepositoryType> dataStoreConfig;

    private String title;

    private boolean isDefaultRoute;

    @Builder.Default
    private VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType> factory = new MasterDetailRouteFactory<>();

    @Builder.Default
    private VortexCrudDialogFactory<ModelClass, FieldType, RepositoryType> dialogFactory = null;

    private boolean isHiddenInMenu;

    // Renamed from formConfiguration and changed type to match usage in MasterDetail
    private GridItemRendererConfiguration<ModelClass, FieldType, RepositoryType> configuration;

    // Separate field for formConfiguration to satisfy FormRouteProvider
    // In MasterDetail context, the "form" is typically the child, so this might be redundant or for the detail view if not using child
    // However, MasterDetailRoute implements FormRouteProvider, so it must return a FormRendererConfiguration.
    // If we assume MasterDetail view doesn't directly configure the form properties (leaving that to child),
    // we might need to delegate or have a separate config.
    // For now, let's add the field to satisfy the interface, but note that MasterDetail.java uses 'configuration()' for the list.
    private FormRendererConfiguration<ModelClass, FieldType, RepositoryType> formConfiguration;

    private final boolean isDeleteButtonHidden = false;

    private SerializableSupplier<Component> iconFactory;

    private List<String> writeRoles;

    private List<String> readOnlyRoles;

    private RouteRenderer<ModelClass, FieldType, RepositoryType> child;

    private List<DataStoreDropdownMenuAction<ModelClass, FieldType, RepositoryType>> menuActions;

    private List<RouteAction<FieldType, ModelClass>> routeActions;

    @Override
    public RouteRendererConfiguration<ModelClass, FieldType, RepositoryType> configuration() {
        return configuration;
    }

    public DataStoreConfig<ModelClass, FieldType, RepositoryType> dataStoreConfig() { return dataStoreConfig; }
    public String title() { return title; }
    public boolean isDefaultRoute() { return isDefaultRoute; }
    public VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType> factory() { return factory; }
    public VortexCrudDialogFactory<ModelClass, FieldType, RepositoryType> dialogFactory() { return dialogFactory; }
    public boolean isHiddenInMenu() { return isHiddenInMenu; }
    public GridItemRendererConfiguration<ModelClass, FieldType, RepositoryType> configuration_field() { return configuration; }
    public FormRendererConfiguration<ModelClass, FieldType, RepositoryType> formConfiguration() { return formConfiguration; }
    public boolean isDeleteButtonHidden() { return isDeleteButtonHidden; }
    public SerializableSupplier<Component> iconFactory() { return iconFactory; }
    public List<String> writeRoles() { return writeRoles; }
    public List<String> readOnlyRoles() { return readOnlyRoles; }
    public RouteRenderer<ModelClass, FieldType, RepositoryType> child() { return child; }
    public List<DataStoreDropdownMenuAction<ModelClass, FieldType, RepositoryType>> menuActions() { return menuActions; }
    public List<RouteAction<FieldType, ModelClass>> routeActions() { return routeActions; }
}
