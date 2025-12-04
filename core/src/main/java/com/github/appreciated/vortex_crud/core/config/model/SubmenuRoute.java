package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.VortexCrudDialogFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.submenu.SubmenuRouteFactory;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.function.SerializableSupplier;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

@Accessors(fluent = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class SubmenuRoute<ModelClass, FieldType, RepositoryType> implements RouteRendererMultipleChildren<ModelClass, FieldType, RepositoryType> {

    private DataStoreConfig<ModelClass, FieldType, RepositoryType> dataStoreConfig;

    private String title;

    private boolean isDefaultRoute;

    @Builder.Default
    private VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType> factory = new SubmenuRouteFactory<>();

    @Builder.Default
    private VortexCrudDialogFactory<ModelClass, FieldType, RepositoryType> dialogFactory = null;

    private boolean isHiddenInMenu;

    private RouteRendererConfiguration<ModelClass, FieldType, RepositoryType> configuration;

    private SerializableSupplier<Component> iconFactory;

    private List<String> writeRoles;

    private List<String> readOnlyRoles;

    private Map<String, RouteRenderer<ModelClass, FieldType, RepositoryType>> childrenMap;

    private List<DataStoreDropdownMenuAction<ModelClass, FieldType, RepositoryType>> menuActions;

    @Override
    public List<com.github.appreciated.vortex_crud.core.ui.actions.RouteAction<FieldType, ModelClass>> routeActions() {
        return null;
    }

    public DataStoreConfig<ModelClass, FieldType, RepositoryType> dataStoreConfig() { return dataStoreConfig; }
    public String title() { return title; }
    public boolean isDefaultRoute() { return isDefaultRoute; }
    public VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType> factory() { return factory; }
    public VortexCrudDialogFactory<ModelClass, FieldType, RepositoryType> dialogFactory() { return dialogFactory; }
    public boolean isHiddenInMenu() { return isHiddenInMenu; }
    public RouteRendererConfiguration<ModelClass, FieldType, RepositoryType> configuration() { return configuration; }
    public SerializableSupplier<Component> iconFactory() { return iconFactory; }
    public List<String> writeRoles() { return writeRoles; }
    public List<String> readOnlyRoles() { return readOnlyRoles; }
    public Map<String, RouteRenderer<ModelClass, FieldType, RepositoryType>> childrenMap() { return childrenMap; }
    public List<DataStoreDropdownMenuAction<ModelClass, FieldType, RepositoryType>> menuActions() { return menuActions; }
}
