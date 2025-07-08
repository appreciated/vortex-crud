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

public class MultiFormRouteFactory<DataStoreId, FieldId> implements VortexCrudRouteFactory<DataStoreId, FieldId> {

    private final FormRouteFactory<DataStoreId, FieldId> formRouteFactory;

    private String titleColumn;

    public MultiFormRouteFactory(
            VortexCrudDataStoreFactoryRegistry<DataStoreId, FieldId> dataStoreFactoryRegistry,
            VortexCrudConfigService<DataStoreId, FieldId> configService,
            FormCreator<DataStoreId, FieldId> formCreator,
            VortexCrudRouteFactoryRegistry<DataStoreId, FieldId> factoryRegistry,
            VortexCrudDataStoreFieldNameResolver<FieldId> resolver
    ) {
        this.formRouteFactory = new FormRouteFactory<>(dataStoreFactoryRegistry, configService, formCreator, factoryRegistry,resolver);
    }

    @Override
    public Component renderRoute(Integer currentPathIndex,
                                 VortexCrudPathToRouteResolver<DataStoreId, FieldId> routeResolver,
                                 @Nullable DetailRouteSetting detailRouteSetting) {
        RouteRenderer<DataStoreId, FieldId> routeRenderer = routeResolver.getRouteForIndex(currentPathIndex);

        MultiFormRendererConfiguration<DataStoreId, FieldId> formConfiguration = (MultiFormRendererConfiguration<DataStoreId, FieldId>) routeRenderer.getConfiguration();
        Div div = new Div();
        for (RouteRendererConfiguration<DataStoreId, FieldId> child : formConfiguration.getForms()) {
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
