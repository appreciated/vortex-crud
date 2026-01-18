package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.annotation.I18nKey;
import com.github.appreciated.vortex_crud.core.file_provider.VortexCrudResourceProvider;
import com.github.appreciated.vortex_crud.core.ui.actions.RouteAction;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.VortexCrudDialogFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.item.CardFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.item.VortexCrudItemFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.kanban.KanbanFactory;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.function.SerializableSupplier;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Accessors(fluent = true)
@NoArgsConstructor
@Getter
public class KanbanRoute<ModelClass, FieldType, RepositoryType> implements RouteRendererSingleChild<ModelClass, FieldType, RepositoryType> {

    @lombok.NonNull
    private DataStoreConfig<ModelClass, FieldType, RepositoryType> dataStoreConfig;

    @I18nKey
    @lombok.NonNull
    private String title;

    private boolean defaultRoute;

    private VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType> factory;

    private VortexCrudDialogFactory<ModelClass, FieldType, RepositoryType> dialogFactory;

    private boolean hiddenInMenu;

    private VortexCrudItemFactory<FieldType> itemFactory;

    @lombok.NonNull
    private FieldType titleField;

    private FieldType descriptionField;

    private FieldType imageField;

    private VortexCrudResourceProvider resourceProvider;

    private boolean inlineEdit;

    private FieldType filterField;

    @lombok.NonNull
    private FieldType columnField;

    private FieldType rowIndexField;

    private SerializableSupplier<Component> iconFactory;

    private List<String> writeRoles;

    private List<String> readOnlyRoles;

    private FormRouteProvider<ModelClass, FieldType, RepositoryType> form;

    private List<RouteAction<FieldType, ModelClass>> actions;

    private List<RouteFilter<FieldType>> filters;

    @Builder
    public KanbanRoute(
            @lombok.NonNull DataStoreConfig<ModelClass, FieldType, RepositoryType> dataStoreConfig,
            @lombok.NonNull String title,
            boolean defaultRoute,
            VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType> factory,
            VortexCrudDialogFactory<ModelClass, FieldType, RepositoryType> dialogFactory,
            boolean hiddenInMenu,
            VortexCrudItemFactory<FieldType> itemFactory,
            @lombok.NonNull FieldType titleField,
            FieldType descriptionField,
            FieldType imageField,
            VortexCrudResourceProvider resourceProvider,
            boolean inlineEdit,
            FieldType filterField,
            @lombok.NonNull FieldType columnField,
            FieldType rowIndexField,
            SerializableSupplier<Component> iconFactory,
            List<String> writeRoles,
            List<String> readOnlyRoles,
            FormRoute<ModelClass, FieldType, RepositoryType> form,
            List<RouteAction<FieldType, ModelClass>> actions,
            @lombok.Singular List<RouteFilter<FieldType>> filters
    ) {
        this.dataStoreConfig = dataStoreConfig;
        this.title = title;
        this.defaultRoute = defaultRoute;
        this.factory = factory != null ? factory : new KanbanFactory<>();
        this.dialogFactory = dialogFactory;
        this.hiddenInMenu = hiddenInMenu;
        this.itemFactory = itemFactory != null ? itemFactory : new CardFactory<>();
        this.titleField = titleField;
        this.descriptionField = descriptionField;
        this.imageField = imageField;
        this.resourceProvider = resourceProvider;
        this.inlineEdit = inlineEdit;
        this.filterField = filterField;
        this.columnField = columnField;
        this.rowIndexField = rowIndexField;
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
}
