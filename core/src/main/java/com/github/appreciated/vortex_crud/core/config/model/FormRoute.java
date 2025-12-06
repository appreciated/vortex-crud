package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.ui.factories.dialog.FormDialogFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.VortexCrudDialogFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.form.FormRouteFactory;
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
public class FormRoute<ModelClass, FieldType, RepositoryType> implements FormRouteProvider<ModelClass, FieldType, RepositoryType> {

    private DataStoreConfig<ModelClass, FieldType, RepositoryType> dataStoreConfig;

    private String title;

    private boolean isDefaultRoute;

    @Builder.Default
    private VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType> factory = new FormRouteFactory<>();

    @Builder.Default
    private VortexCrudDialogFactory<ModelClass, FieldType, RepositoryType> dialogFactory = new FormDialogFactory<>();

    private boolean isHiddenInMenu;

    private final boolean isDeleteButtonHidden = false;

    private FormRendererConfiguration<ModelClass, FieldType, RepositoryType> formConfiguration;

    private SerializableSupplier<Component> iconFactory;

    private List<String> writeRoles;

    private List<String> readOnlyRoles;

    private List<? extends InternalFormElement<ModelClass, FieldType, RepositoryType>> children;

    private List<DataStoreDropdownMenuAction<ModelClass, FieldType, RepositoryType>> menuActions;

    @Override
    public RouteRendererConfiguration<ModelClass, FieldType, RepositoryType> configuration() {
        return formConfiguration;
    }

    public DataStoreConfig<ModelClass, FieldType, RepositoryType> dataStoreConfig() { return dataStoreConfig; }
    public String title() { return title; }
    public boolean isDefaultRoute() { return isDefaultRoute; }
    public VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType> factory() { return factory; }
    public VortexCrudDialogFactory<ModelClass, FieldType, RepositoryType> dialogFactory() { return dialogFactory; }
    public boolean isHiddenInMenu() { return isHiddenInMenu; }
    public boolean isDeleteButtonHidden() { return isDeleteButtonHidden; }
    public FormRendererConfiguration<ModelClass, FieldType, RepositoryType> formConfiguration() { return formConfiguration; }
    public SerializableSupplier<Component> iconFactory() { return iconFactory; }
    public List<String> writeRoles() { return writeRoles; }
    public List<String> readOnlyRoles() { return readOnlyRoles; }
    public List<? extends InternalFormElement<ModelClass, FieldType, RepositoryType>> children() { return children; }
    public List<DataStoreDropdownMenuAction<ModelClass, FieldType, RepositoryType>> menuActions() { return menuActions; }
}
