package com.github.appreciated.turbo_crud.core.ui.factories.route.submenu;

import com.github.appreciated.turbo_crud.core.config.TurboCrudPathToRouteResolver;
import com.github.appreciated.turbo_crud.core.service.TurboCrudConfigService;
import com.github.appreciated.turbo_crud.core.ui.factories.route.DetailRouteSetting;
import com.github.appreciated.turbo_crud.core.ui.factories.route.TurboCrudRouteFactory;
import com.github.appreciated.turbo_crud.core.ui.factories.route.TurboCrudRouteFactoryRegistry;
import com.vaadin.flow.component.Component;
import jakarta.annotation.Nullable;

public class SubmenuRouteFactory<DataStoreId, FieldId> implements TurboCrudRouteFactory<DataStoreId, FieldId> {

    private final TurboCrudRouteFactoryRegistry<DataStoreId, FieldId> routeFactory;
    private final TurboCrudConfigService<DataStoreId, FieldId> configService;

    public SubmenuRouteFactory(TurboCrudRouteFactoryRegistry<DataStoreId, FieldId> routeFactory, TurboCrudConfigService<DataStoreId, FieldId> configService) {
        this.routeFactory = routeFactory;
        this.configService = configService;
    }

    @Override
    public Component renderRoute(Integer currentPathIndex,
                                 TurboCrudPathToRouteResolver<DataStoreId, FieldId> routeResolver,
                                 @Nullable DetailRouteSetting detailRouteSetting) {
        return new Submenu<>(currentPathIndex, routeResolver, routeFactory, configService);
    }

    @Override
    public boolean isContainerRoute() {
        return true;
    }
}
