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

    private GridItemRendererConfiguration<ModelClass, FieldType, RepositoryType> configuration;

    private FormRendererConfiguration<ModelClass, FieldType, RepositoryType> formConfiguration;

    private final boolean isDeleteButtonHidden = false;

    private SerializableSupplier<Component> iconFactory;

    private List<String> writeRoles;

    private List<String> readOnlyRoles;

    private RouteRenderer<ModelClass, FieldType, RepositoryType> child;

    private List<DataStoreDropdownMenuAction<ModelClass, FieldType, RepositoryType>> menuActions;

    private List<RouteAction<FieldType, ModelClass>> routeActions;

    @Override
    public FormRendererConfiguration<ModelClass, FieldType, RepositoryType> formConfiguration() {
        if (formConfiguration != null) {
            return formConfiguration;
        }
        if (child instanceof FormRouteProvider) {
            return ((FormRouteProvider<ModelClass, FieldType, RepositoryType>) child).formConfiguration();
        }
        return null;
    }
}
