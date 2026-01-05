package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.annotation.I18nKey;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.view.CustomViewFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.view.CustomViewFactoryRouteFactory;
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
@AllArgsConstructor
@SuperBuilder
@Getter
public class CustomViewFactoryRoute<ModelClass, FieldType, RepositoryType> implements RouteRenderer<ModelClass, FieldType, RepositoryType> {

    @lombok.NonNull
    private DataStoreConfig<ModelClass, FieldType, RepositoryType> dataStoreConfig;

    @lombok.NonNull
    private CustomViewFactory<ModelClass> viewFactory;

    @I18nKey
    private String title;

    private boolean defaultRoute;

    @Builder.Default
    private VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType> factory = new CustomViewFactoryRouteFactory<>();

    private boolean hiddenInMenu;

    private SerializableSupplier<Component> iconFactory;

    private List<String> writeRoles;

    private List<String> readOnlyRoles;
}
