package com.github.appreciated.turbo_crud.core.ui.factories.route.form;

import com.github.appreciated.turbo_crud.core.config.TurboCrudPathToRouteResolver;
import com.github.appreciated.turbo_crud.core.config.model.MultiFormConfiguration;
import com.github.appreciated.turbo_crud.core.config.model.Route;
import com.github.appreciated.turbo_crud.core.config.model.RouteConfiguration;
import com.github.appreciated.turbo_crud.core.entity.data_store.TurboCrudDataStoreFactoryRegistry;
import com.github.appreciated.turbo_crud.core.entity.data_store.TurboCrudDataStoreFieldNameResolver;
import com.github.appreciated.turbo_crud.core.service.TurboCrudConfigService;
import com.github.appreciated.turbo_crud.core.ui.factories.form.FormCreator;
import com.github.appreciated.turbo_crud.core.ui.factories.route.DetailRouteSetting;
import com.github.appreciated.turbo_crud.core.ui.factories.route.TurboCrudRouteFactory;
import com.github.appreciated.turbo_crud.core.ui.factories.route.TurboCrudRouteFactoryRegistry;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import jakarta.annotation.Nullable;

public class MultiFormRouteFactory<DataStoreId, FieldId> implements TurboCrudRouteFactory<DataStoreId, FieldId> {

    private final FormRouteFactory<DataStoreId, FieldId> formRouteFactory;

    private String titleColumn;

    public MultiFormRouteFactory(
            TurboCrudDataStoreFactoryRegistry<DataStoreId, FieldId> dataStoreFactoryRegistry,
            TurboCrudConfigService<DataStoreId, FieldId> configService,
            FormCreator<DataStoreId, FieldId> formCreator,
            TurboCrudRouteFactoryRegistry<DataStoreId, FieldId> factoryRegistry,
            TurboCrudDataStoreFieldNameResolver<FieldId> resolver
    ) {
        this.formRouteFactory = new FormRouteFactory<>(dataStoreFactoryRegistry, configService, formCreator, factoryRegistry,resolver);
    }

    @Override
    public Component renderRoute(Integer currentPathIndex,
                                 TurboCrudPathToRouteResolver<DataStoreId, FieldId> routeResolver,
                                 @Nullable DetailRouteSetting detailRouteSetting) {
        Route<DataStoreId, FieldId> route = routeResolver.getRouteForIndex(currentPathIndex);

        MultiFormConfiguration<DataStoreId, FieldId> formConfiguration = (MultiFormConfiguration<DataStoreId, FieldId>) route.getConfiguration();
        Div div = new Div();
        for (RouteConfiguration<DataStoreId, FieldId> child : formConfiguration.getForms()) {
            assert detailRouteSetting != null;
            div.add(formRouteFactory.getForm(routeResolver, true, true, detailRouteSetting.isCreationMode(), route, child));
        }
        return div;
    }

    @Override
    public boolean isContainerRoute() {
        return false;
    }
}
