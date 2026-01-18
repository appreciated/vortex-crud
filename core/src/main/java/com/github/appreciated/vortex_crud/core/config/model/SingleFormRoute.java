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
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * Configuration for a form route that edits a specific single entry based on configuration, rather than using an ID
 * from the URL path.
 * ...
 */
@Accessors(fluent = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class SingleFormRoute<ModelClass, FieldType, RepositoryType> implements FormRouteProvider<ModelClass, FieldType, RepositoryType> {

    @Setter
    private DataStoreConfig<ModelClass, FieldType, RepositoryType> dataStoreConfig;

    @Setter
    @I18nKey
    private String title;

    private boolean defaultRoute;

    @Builder.Default
    private VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType> factory = new FormRouteFactory<>();

    @Builder.Default
    private VortexCrudDialogFactory<ModelClass, FieldType, RepositoryType> dialogFactory = new FormDialogFactory<>();

    private boolean hiddenInMenu;

    private final boolean isDeleteButtonHidden = true;

    private VortexCrudItemFactory<FieldType> itemFactory;

    private FieldType titleField;

    private FieldType descriptionField;

    private FieldType imageField;

    private VortexCrudResourceProvider resourceProvider;

    private boolean inlineEdit;

    private FieldType filterField;

    private SerializableSupplier<Component> iconFactory;

    private List<String> writeRoles;

    private List<String> readOnlyRoles;

    // private List<? extends InternalFormElement<FieldType>> children;

    private FieldType entityFilterField;

    /**
     * Provider that supplies the filter value dynamically (e.g., from security context).
     */
    private SerializableSupplier<Object> entityFilterValueProvider;

    public FieldType entityFilterField() {
        return entityFilterField;
    }

    public SerializableSupplier<Object> entityFilterValueProvider() {
        return entityFilterValueProvider;
    }

    private List<RouteAction<FieldType, ModelClass>> actions;

    @lombok.NonNull
    private List<InternalFormElement<FieldType>> fields;
}
