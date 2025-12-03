package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.ui.actions.RouteAction;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.VortexCrudDialogFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.menu.MenuActionComponentFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.calendar.CalendarFactory;
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
public class CalendarRoute<ModelClass, FieldType, RepositoryType> implements RouteRendererSingleChild<ModelClass, FieldType, RepositoryType> {

    private DataStoreConfig<ModelClass, FieldType, RepositoryType> dataStoreConfig;

    private String title;

    private boolean isDefaultRoute;

    @Builder.Default
    private VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType> factory = new CalendarFactory<>();

    @Builder.Default
    private VortexCrudDialogFactory<ModelClass, FieldType, RepositoryType> dialogFactory = null;

    private boolean isHiddenInMenu;

    private RouteRendererConfiguration<ModelClass, FieldType, RepositoryType> configuration;

    private SerializableSupplier<Component> iconFactory;

    private List<String> writeRoles;

    private List<String> readOnlyRoles;

    private RouteRenderer<ModelClass, FieldType, RepositoryType> child;

    private List<MenuActionComponentFactory<ModelClass, FieldType, RepositoryType>> menuActionFactories;

    private List<DataStoreDropdownMenuAction<ModelClass, FieldType, RepositoryType>> menuActions;

    private List<RouteAction<FieldType, ModelClass>> routeActions;

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
    public RouteRenderer<ModelClass, FieldType, RepositoryType> child() { return child; }
    public List<MenuActionComponentFactory<ModelClass, FieldType, RepositoryType>> menuActionFactories() { return menuActionFactories; }
    public List<DataStoreDropdownMenuAction<ModelClass, FieldType, RepositoryType>> menuActions() { return menuActions; }
    public List<RouteAction<FieldType, ModelClass>> routeActions() { return routeActions; }
}
