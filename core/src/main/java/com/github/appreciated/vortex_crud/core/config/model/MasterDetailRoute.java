package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.file_provider.VortexCrudResourceProvider;
import com.github.appreciated.vortex_crud.core.ui.actions.RouteAction;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.VortexCrudDialogFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.item.CardFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.item.VortexCrudItemFactory;
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

    @Builder.Default
    private VortexCrudItemFactory<FieldType> itemFactory = new CardFactory<>();

    private FieldType titleField;

    private FieldType descriptionField;

    private FieldType imageField;

    private VortexCrudResourceProvider resourceProvider;

    private boolean inlineEdit;

    private FieldType filterField;

    private List<InternalFormElement<ModelClass, FieldType, RepositoryType>> children;

    private final boolean isDeleteButtonHidden = false;

    private SerializableSupplier<Component> iconFactory;

    private List<String> writeRoles;

    private List<String> readOnlyRoles;

    private RouteRenderer<ModelClass, FieldType, RepositoryType> form;

    private List<DataStoreDropdownMenuAction<ModelClass, FieldType, RepositoryType>> menuActions;

    private List<RouteAction<FieldType, ModelClass>> routeActions;

    @lombok.Singular
    private List<RouteFilter<FieldType>> routeFilters;

    public List<RouteFilter<FieldType>> filters() { return routeFilters; }

    public RouteRenderer<ModelClass, FieldType, RepositoryType> form() {
        return form;
    }

    @Override
    public VortexCrudItemFactory<FieldType> itemFactory() {
        return itemFactory;
    }

    @Override
    public List<InternalFormElement<ModelClass, FieldType, RepositoryType>> children() {
        // Delegate to form's children if this route doesn't have its own children defined
        if (children != null) {
            return children;
        }
        if (form instanceof FormRouteProvider) {
            return ((FormRouteProvider<ModelClass, FieldType, RepositoryType>) form).children();
        }
        return java.util.Collections.emptyList();
    }

    @Override
    public FieldType titleField() {
        // Delegate to form's titleField if this route doesn't have its own defined
        if (titleField != null) {
            return titleField;
        }
        if (form != null) {
            return form.titleField();
        }
        return null;
    }

}
