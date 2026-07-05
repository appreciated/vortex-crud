package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.annotation.I18nKey;
import com.github.appreciated.vortex_crud.core.file_provider.VortexCrudResourceProvider;
import com.github.appreciated.vortex_crud.core.ui.actions.RouteAction;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.FormDialogFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.VortexCrudDialogFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.item.VortexCrudItemFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.form.FormRouteFactory;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.function.SerializableSupplier;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

@Accessors(fluent = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class FormRoute<ModelClass, FieldType, RepositoryType> implements
        RouteRendererMultipleChildren<ModelClass, FieldType, RepositoryType>,
        FormRouteProvider<ModelClass, FieldType, RepositoryType> {

    @Setter
    private DataStoreConfig<ModelClass, FieldType, RepositoryType> dataStoreConfig;

    @Setter
    @I18nKey
    private String title;

    private boolean defaultRoute;

    private VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType> factory = new FormRouteFactory<>();

    private VortexCrudDialogFactory<ModelClass, FieldType, RepositoryType> dialogFactory = new FormDialogFactory<>();

    private boolean hiddenInMenu;

    private final boolean isDeleteButtonHidden = false;

    private VortexCrudItemFactory<FieldType> itemFactory;

    private FieldType titleField;

    private FieldType descriptionField;

    private FieldType imageField;

    private VortexCrudResourceProvider resourceProvider;

    private boolean inlineEdit;

    private FieldType searchField;

    @lombok.NonNull
    private List<InternalFormElement<FieldType>> fields;

    private SerializableSupplier<Component> iconFactory;

    private List<String> writeRoles;

    private List<String> readOnlyRoles;

    private List<RouteAction<FieldType, ModelClass>> actions;

    private Map<String, RouteRenderer<ModelClass, FieldType, RepositoryType>> routes;

    private FormLogic<ModelClass, FieldType, RepositoryType> formLogic;

    private DynamicFieldsConfiguration<ModelClass, FieldType, RepositoryType> dynamicFields;

    @Builder
    public FormRoute(
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
            FieldType searchField,
            @lombok.NonNull List<InternalFormElement<FieldType>> fields,
            SerializableSupplier<Component> iconFactory,
            List<String> writeRoles,
            List<String> readOnlyRoles,
            List<RouteAction<FieldType, ModelClass>> actions,
            Map<String, RouteRenderer<ModelClass, FieldType, RepositoryType>> routes,
            FormLogic<ModelClass, FieldType, RepositoryType> formLogic,
            DynamicFieldsConfiguration<ModelClass, FieldType, RepositoryType> dynamicFields
    ) {
        this.defaultRoute = defaultRoute;
        this.factory = factory != null ? factory : new FormRouteFactory<>();
        this.dialogFactory = dialogFactory != null ? dialogFactory : new FormDialogFactory<>();
        this.hiddenInMenu = hiddenInMenu;
        this.itemFactory = itemFactory;
        this.titleField = titleField;
        this.descriptionField = descriptionField;
        this.imageField = imageField;
        this.resourceProvider = resourceProvider;
        this.inlineEdit = inlineEdit;
        this.searchField = searchField;
        this.fields = fields;
        this.iconFactory = iconFactory;
        this.writeRoles = writeRoles;
        this.readOnlyRoles = readOnlyRoles;
        this.actions = actions;
        this.routes = routes;
        this.formLogic = formLogic;
        this.dynamicFields = dynamicFields;
    }
}
