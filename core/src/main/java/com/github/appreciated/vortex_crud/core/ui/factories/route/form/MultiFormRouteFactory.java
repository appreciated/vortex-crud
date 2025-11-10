package com.github.appreciated.vortex_crud.core.ui.factories.route.form;

import com.github.appreciated.vortex_crud.core.config.VortexCrudPathToRouteResolver;
import com.github.appreciated.vortex_crud.core.config.model.MultiFormRendererConfiguration;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.config.model.RouteRendererConfiguration;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFactoryRegistry;
import com.github.appreciated.vortex_crud.core.security.VortexCrudRbacPermissionChecker;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService;
import com.github.appreciated.vortex_crud.core.ui.factories.form.FormCreator;
import com.github.appreciated.vortex_crud.core.ui.factories.route.DetailRouteSetting;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactoryRegistry;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import jakarta.annotation.Nullable;

public class MultiFormRouteFactory<ModelClass, FieldType, RepositoryType> implements VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType> {

    private final FormRouteFactory<ModelClass, FieldType, RepositoryType> formRouteFactory;

    private String titleColumn;

    public MultiFormRouteFactory(
            VortexCrudDataStoreFactoryRegistry<ModelClass, FieldType, RepositoryType> dataStoreFactoryRegistry,
            VortexCrudConfigService<ModelClass, FieldType, RepositoryType> configService,
            FormCreator<ModelClass, FieldType, RepositoryType> formCreator,
            VortexCrudRouteFactoryRegistry<ModelClass, FieldType, RepositoryType> factoryRegistry,
            com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService<FieldType> reflectionService,
            VortexCrudRbacPermissionChecker<ModelClass, FieldType, RepositoryType> permissionChecker
    ) {
        this.formRouteFactory = new FormRouteFactory<>(dataStoreFactoryRegistry, configService, formCreator, factoryRegistry, reflectionService, permissionChecker);
    }

    @Override
    public Component renderRoute(Integer currentPathIndex,
                                 VortexCrudPathToRouteResolver<ModelClass, FieldType, RepositoryType> routeResolver,
                                 @Nullable DetailRouteSetting detailRouteSetting) {
        RouteRenderer<ModelClass, FieldType, RepositoryType> routeRenderer = routeResolver.getRouteForIndex(currentPathIndex);

        MultiFormRendererConfiguration<ModelClass, FieldType, RepositoryType> formConfiguration =
                (MultiFormRendererConfiguration<ModelClass, FieldType, RepositoryType>) routeRenderer.configuration();
        Div div = new Div();
        for (RouteRendererConfiguration<ModelClass, FieldType, RepositoryType> child : formConfiguration.forms()) {
            assert detailRouteSetting != null;
            div.add(formRouteFactory.getForm(routeResolver, true, true, detailRouteSetting.isCreationMode(), false, routeRenderer, child));
        }
        return div;
    }

    @Override
    public boolean isContainerRoute() {
        return false;
    }
}
