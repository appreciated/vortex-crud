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
@Getter
public class CustomViewFactoryRoute<ModelClass, FieldType, RepositoryType> implements
        RouteRendererSingleChild<ModelClass, FieldType, RepositoryType>,
        RouteRendererMultipleChildren<ModelClass, FieldType, RepositoryType>,
        FormRouteProvider<ModelClass, FieldType, RepositoryType> {

    @Setter
    @lombok.NonNull
    private DataStoreConfig<ModelClass, FieldType, RepositoryType> dataStoreConfig;

    @lombok.NonNull
    private CustomViewFactory<ModelClass> viewFactory;

    @Setter
    @I18nKey
    private String title;

    private boolean defaultRoute;

    private VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType> factory;

    private boolean hiddenInMenu;

    private SerializableSupplier<Component> iconFactory;

    private List<String> writeRoles;

    private List<String> readOnlyRoles;

    // Single child support
    private FormRouteProvider<ModelClass, FieldType, RepositoryType> form;

    // Multiple children support
    private java.util.Map<String, RouteRenderer<ModelClass, FieldType, RepositoryType>> routes;

    private boolean isDeleteButtonHidden;

    @Builder
    public CustomViewFactoryRoute(
            @lombok.NonNull DataStoreConfig<ModelClass, FieldType, RepositoryType> dataStoreConfig,
            @lombok.NonNull CustomViewFactory<ModelClass> viewFactory,
            String title,
            boolean defaultRoute,
            VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType> factory,
            boolean hiddenInMenu,
            SerializableSupplier<Component> iconFactory,
            List<String> writeRoles,
            List<String> readOnlyRoles,
            FormRouteProvider<ModelClass, FieldType, RepositoryType> form,
            java.util.Map<String, RouteRenderer<ModelClass, FieldType, RepositoryType>> routes,
            boolean isDeleteButtonHidden
    ) {
        this.dataStoreConfig = dataStoreConfig;
        this.viewFactory = viewFactory;
        this.title = title;
        this.defaultRoute = defaultRoute;
        this.factory = factory != null ? factory : new CustomViewFactoryRouteFactory<>();
        this.hiddenInMenu = hiddenInMenu;
        this.iconFactory = iconFactory;
        this.writeRoles = writeRoles;
        this.readOnlyRoles = readOnlyRoles;
        this.isDeleteButtonHidden = isDeleteButtonHidden;

        // Inject parent's dataStoreConfig into child form
        this.form = form;
        if (this.form != null && this.form.dataStoreConfig() == null) {
            this.form.dataStoreConfig(this.dataStoreConfig);
            this.form.title(this.title);
        }

        // Inject parent's dataStoreConfig into child routes
        this.routes = routes;
        if (this.routes != null) {
            for (RouteRenderer<ModelClass, FieldType, RepositoryType> childRoute : this.routes.values()) {
                if (childRoute.dataStoreConfig() == null && childRoute instanceof FormRouteProvider) {
                    @SuppressWarnings("unchecked")
                    FormRouteProvider<ModelClass, FieldType, RepositoryType> formRoute =
                        (FormRouteProvider<ModelClass, FieldType, RepositoryType>) childRoute;
                    formRoute.dataStoreConfig(this.dataStoreConfig);
                    formRoute.title(this.title);
                }
            }
        }
    }

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
