package com.github.appreciated.vortex_crud.core.ui.factories.route.form;

import com.github.appreciated.vortex_crud.core.config.VortexCrudPathToRouteResolver;
import com.github.appreciated.vortex_crud.core.config.model.MultiFormRendererConfiguration;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.config.model.RouteRendererConfiguration;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFactoryRegistry;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFieldNameResolver;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService;
import com.github.appreciated.vortex_crud.core.ui.factories.form.FormCreator;
import com.github.appreciated.vortex_crud.core.ui.factories.route.DetailRouteSetting;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactoryRegistry;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import jakarta.annotation.Nullable;

public class MultiFormRouteFactory<DataStoreId, FieldId, KeyType> implements VortexCrudRouteFactory<DataStoreId, FieldId, KeyType> {

    private final FormRouteFactory<DataStoreId, FieldId, KeyType> formRouteFactory;

    private String titleColumn;

    public MultiFormRouteFactory(
            VortexCrudDataStoreFactoryRegistry<DataStoreId, FieldId, KeyType> dataStoreFactoryRegistry,
            VortexCrudConfigService<DataStoreId, FieldId, KeyType> configService,
            FormCreator<DataStoreId, FieldId, KeyType> formCreator,
            VortexCrudRouteFactoryRegistry<DataStoreId, FieldId, KeyType> factoryRegistry,
            VortexCrudDataStoreFieldNameResolver<FieldId> resolver,
            com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService<FieldId> reflectionService
    ) {
        this.formRouteFactory = new FormRouteFactory<DataStoreId, FieldId, KeyType>(dataStoreFactoryRegistry, configService, formCreator, factoryRegistry, resolver, reflectionService);
    }

    @Override
    public Component renderRoute(Integer currentPathIndex,
                                 VortexCrudPathToRouteResolver<DataStoreId, FieldId, KeyType> routeResolver,
                                 @Nullable DetailRouteSetting detailRouteSetting) {
        RouteRenderer<DataStoreId, FieldId, KeyType> routeRenderer = routeResolver.getRouteForIndex(currentPathIndex);

        @SuppressWarnings("unchecked")
        MultiFormRendererConfiguration<DataStoreId, FieldId, KeyType> formConfiguration =
                (MultiFormRendererConfiguration<DataStoreId, FieldId, KeyType>) routeRenderer.getConfiguration();
        Div div = new Div();
        for (RouteRendererConfiguration<DataStoreId, FieldId, KeyType> child : formConfiguration.getForms()) {
            assert detailRouteSetting != null;
            div.add(formRouteFactory.getForm(routeResolver, true, true, detailRouteSetting.isCreationMode(), routeRenderer, child));
        }
        return div;
    }

    @Override
    public boolean isContainerRoute() {
        return false;
    }
}
