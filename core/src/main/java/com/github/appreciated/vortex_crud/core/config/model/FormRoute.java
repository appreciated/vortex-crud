package com.github.appreciated.vortex_crud.core.config.model;

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

    private RepositoryType dataStoreKey;

    private String title;

    private boolean isDefaultRoute;

    @Builder.Default
    private VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType> factory = new FormRouteFactory<>();

    @Override
    public VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType> factoryInstance() {
        return factory;
    }

    private VortexCrudDialogFactory<ModelClass, FieldType, RepositoryType> dialogFactoryInstance;

    private boolean isHiddenInMenu;

    private final boolean isDeleteButtonHidden = false;

    private FormRendererConfiguration<ModelClass, FieldType, RepositoryType> formConfiguration;

    private SerializableSupplier<Component> iconFactory;

    private List<String> writeRoles;

    private List<String> readOnlyRoles;

    private List<? extends InternalFormElement<ModelClass, FieldType, RepositoryType>> children;

    /**
     * List of menu actions for adding custom components to the menu.
     * This can include dropdowns, filters, action buttons, etc.
     */
    private List<DataStoreDropdownMenuAction<FieldType, RepositoryType>> menuActions;

    @Override
    public RouteRendererConfiguration<ModelClass, FieldType, RepositoryType> configuration() {
        return formConfiguration;
    }
}