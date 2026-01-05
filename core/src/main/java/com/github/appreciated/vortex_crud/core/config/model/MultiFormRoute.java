package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.annotation.I18nKey;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.FormDialogFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.FormSlideFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.VortexCrudDialogFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.form.MultiFormRouteFactory;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.function.SerializableSupplier;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;

@Accessors(fluent = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class MultiFormRoute<ModelClass, FieldType, RepositoryType> implements FormRouteProvider<ModelClass, FieldType, RepositoryType> {

    @Setter
    private DataStoreConfig<ModelClass, FieldType, RepositoryType> dataStoreConfig;

    @I18nKey
    @Setter
    private String title;

    /**
     * List of form routes to be rendered as multiple forms within this multi-form route.
     */
    @lombok.NonNull
    private List<FormRoute<ModelClass, FieldType, RepositoryType>> forms;

    @Builder.Default
    private VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType> factory = new MultiFormRouteFactory<>();

    @Builder.Default
    private VortexCrudDialogFactory<ModelClass, FieldType, RepositoryType> dialogFactory = new FormDialogFactory<>();

    @Override
    public boolean defaultRoute() {
        return false;
    }

    @Override
    public boolean hiddenInMenu() {
        return false;
    }

    public FieldType titleField() {
        return null;
    }

    @Override
    public SerializableSupplier<Component> iconFactory() {
        return null;
    }

    @Override
    public boolean isDeleteButtonHidden() {
        return false;
    }

    @Override
    public List<InternalFormElement<FieldType>> fields() {
        return List.of();
    }

    @Override
    public List<String> writeRoles() {
        return List.of();
    }

    @Override
    public List<String> readOnlyRoles() {
        return List.of();
    }
}
