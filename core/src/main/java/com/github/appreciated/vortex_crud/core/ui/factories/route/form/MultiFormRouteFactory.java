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

public class MultiFormRouteFactory<DataStoreId, FieldId, ModelClass> implements VortexCrudRouteFactory<DataStoreId, FieldId, ModelClass> {

    private final FormRouteFactory<DataStoreId, FieldId, ModelClass> formRouteFactory;

    private String titleColumn;

    public MultiFormRouteFactory(
            VortexCrudDataStoreFactoryRegistry<DataStoreId, FieldId, ModelClass> dataStoreFactoryRegistry,
            VortexCrudConfigService<DataStoreId, FieldId, ModelClass> configService,
            FormCreator<DataStoreId, FieldId, ModelClass> formCreator,
            VortexCrudRouteFactoryRegistry<DataStoreId, FieldId, ModelClass> factoryRegistry,
            VortexCrudDataStoreFieldNameResolver<FieldId> resolver
    ) {
        this.formRouteFactory = new FormRouteFactory<>(dataStoreFactoryRegistry, configService, formCreator, factoryRegistry, resolver);
    }

    @Override
    public Component renderRoute(Integer currentPathIndex,
                                 VortexCrudPathToRouteResolver<DataStoreId, FieldId, ModelClass> routeResolver,
                                 @Nullable DetailRouteSetting detailRouteSetting) {
        RouteRenderer<DataStoreId, FieldId, ModelClass> routeRenderer = routeResolver.getRouteForIndex(currentPathIndex);

        MultiFormRendererConfiguration<DataStoreId, FieldId, ModelClass>  formConfiguration = (MultiFormRendererConfiguration<DataStoreId, FieldId, ModelClass> ) routeRenderer.getConfiguration();
        Div div = new Div();
        for (RouteRendererConfiguration<DataStoreId, FieldId, ModelClass> child : formConfiguration.getForms()) {
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
