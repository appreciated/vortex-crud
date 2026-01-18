package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.annotation.I18nKey;
import com.github.appreciated.vortex_crud.core.file_provider.VortexCrudResourceProvider;
import com.github.appreciated.vortex_crud.core.ui.actions.RouteAction;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.VortexCrudDialogFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.item.CardFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.item.VortexCrudItemFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.master_detail.MasterDetailRouteFactory;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.function.SerializableSupplier;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Accessors(fluent = true)
@NoArgsConstructor
@Getter
public class MasterDetailRoute<ModelClass, FieldType, RepositoryType> implements RouteRendererSingleChild<ModelClass, FieldType, RepositoryType>, FormRouteProvider<ModelClass, FieldType, RepositoryType> {

    @Setter
    private DataStoreConfig<ModelClass, FieldType, RepositoryType> dataStoreConfig;

    @I18nKey
    @Setter
    private String title;

    private boolean defaultRoute;

    private VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType> factory;

    private VortexCrudDialogFactory<ModelClass, FieldType, RepositoryType> dialogFactory;

    private boolean hiddenInMenu;

    private VortexCrudItemFactory<FieldType> itemFactory;

    private FieldType titleField;

    private FieldType descriptionField;

    private FieldType imageField;

    private VortexCrudResourceProvider resourceProvider;

    private boolean inlineEdit;

    private FieldType filterField;

    private final boolean isDeleteButtonHidden = false;

    private SerializableSupplier<Component> iconFactory;

    private List<String> writeRoles;

    private List<String> readOnlyRoles;

    @lombok.NonNull
    private FormRouteProvider<ModelClass, FieldType, RepositoryType> form;

    private List<RouteAction<FieldType, ModelClass>> actions;

    private List<RouteFilter<FieldType>> filters;

    @Builder
    public MasterDetailRoute(
            DataStoreConfig<ModelClass, FieldType, RepositoryType> dataStoreConfig,
            String title,
            boolean defaultRoute,
            VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType> factory,
            VortexCrudDialogFactory<ModelClass, FieldType, RepositoryType> dialogFactory,
            boolean hiddenInMenu,
            VortexCrudItemFactory<FieldType> itemFactory,
            FieldType titleField,
            FieldType descriptionField,
            FieldType imageField,
            VortexCrudResourceProvider resourceProvider,
            boolean inlineEdit,
            FieldType filterField,
            SerializableSupplier<Component> iconFactory,
            List<String> writeRoles,
            List<String> readOnlyRoles,
            @lombok.NonNull FormRoute<ModelClass, FieldType, RepositoryType> form,
            List<RouteAction<FieldType, ModelClass>> actions,
            @lombok.Singular List<RouteFilter<FieldType>> filters
    ) {
        this.dataStoreConfig = dataStoreConfig;
        this.title = title;
        this.defaultRoute = defaultRoute;
        this.factory = factory != null ? factory : new MasterDetailRouteFactory<>();
        this.dialogFactory = dialogFactory;
        this.hiddenInMenu = hiddenInMenu;
        this.itemFactory = itemFactory != null ? itemFactory : new CardFactory<>();
        this.titleField = titleField;
        this.descriptionField = descriptionField;
        this.imageField = imageField;
        this.resourceProvider = resourceProvider;
        this.inlineEdit = inlineEdit;
        this.filterField = filterField;
        this.iconFactory = iconFactory;
        this.writeRoles = writeRoles;
        this.readOnlyRoles = readOnlyRoles;
        this.actions = actions;
        this.filters = filters;

        // Inject parent's dataStoreConfig and title into child form
        this.form = form;
        if (this.form != null) {
            this.form.dataStoreConfig(this.dataStoreConfig);
            this.form.title(this.title);
        }
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

    @Override
    public List<InternalFormElement<FieldType>> fields() {
        return form.fields();
    }
}
