package com.github.appreciated.vortex_crud.core.ui.factories.route.submenu;

import com.github.appreciated.vortex_crud.core.config.VortexCrudPathToRouteResolver;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService;
import com.github.appreciated.vortex_crud.core.ui.factories.route.DetailRouteSetting;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactoryRegistry;
import com.vaadin.flow.component.Component;
import jakarta.annotation.Nullable;

public class SubmenuRouteFactory<DataStoreId, FieldId, KeyType> implements VortexCrudRouteFactory<DataStoreId, FieldId, KeyType> {

    private final VortexCrudRouteFactoryRegistry<DataStoreId, FieldId, KeyType> routeFactory;
    private final VortexCrudConfigService<DataStoreId, FieldId, KeyType> configService;

    public SubmenuRouteFactory(VortexCrudRouteFactoryRegistry<DataStoreId, FieldId, KeyType> routeFactory, VortexCrudConfigService<DataStoreId, FieldId, KeyType> configService) {
        this.routeFactory = routeFactory;
        this.configService = configService;
    }

    @Override
    public Component renderRoute(Integer currentPathIndex,
                                 VortexCrudPathToRouteResolver<DataStoreId, FieldId, KeyType> routeResolver,
                                 @Nullable DetailRouteSetting detailRouteSetting) {
        return new Submenu<>(currentPathIndex, routeResolver, routeFactory, configService);
    }

    @Override
    public boolean isContainerRoute() {
        return true;
    }
}
