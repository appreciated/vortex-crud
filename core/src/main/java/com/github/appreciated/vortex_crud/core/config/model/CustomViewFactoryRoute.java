package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.annotation.I18nKey;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.view.CustomViewFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.view.CustomViewFactoryRouteFactory;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.function.SerializableSupplier;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.util.Collections;
import java.util.List;

@Accessors(fluent = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter
public class CustomViewFactoryRoute<ModelClass, FieldType, RepositoryType> implements FormRouteProvider<ModelClass, FieldType, RepositoryType> {

    @Setter
    @lombok.NonNull
    private DataStoreConfig<ModelClass, FieldType, RepositoryType> dataStoreConfig;

    @lombok.NonNull
    private CustomViewFactory<ModelClass> viewFactory;

    @Setter
    @I18nKey
    private String title;

    private boolean defaultRoute;

    @Builder.Default
    private VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType> factory = new CustomViewFactoryRouteFactory<>();

    private boolean hiddenInMenu;

    private SerializableSupplier<Component> iconFactory;

    private List<String> writeRoles;

    private List<String> readOnlyRoles;

    @Builder.Default
    private boolean isDeleteButtonHidden = true;

    @Override
    public boolean isDeleteButtonHidden() {
        return isDeleteButtonHidden;
    }

    @Override
    public List<InternalFormElement<FieldType>> fields() {
        // CustomViewFactoryRoute doesn't use fields, return empty list
        return Collections.emptyList();
    }
}
