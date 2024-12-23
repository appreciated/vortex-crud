package com.github.appreciated.turbo_crud.core.ui.factories.route.submenu;

import com.github.appreciated.turbo_crud.core.config.TurboCrudPathToRouteResolver;
import com.github.appreciated.turbo_crud.core.service.TurboCrudConfigService;
import com.github.appreciated.turbo_crud.core.ui.factories.route.DetailRouteSetting;
import com.github.appreciated.turbo_crud.core.ui.factories.route.TurboCrudRouteFactory;
import com.github.appreciated.turbo_crud.core.ui.factories.route.TurboCrudRouteFactoryRegistry;
import com.vaadin.flow.component.Component;
import jakarta.annotation.Nullable;

public class SubmenuRouteFactory implements TurboCrudRouteFactory {

    private final TurboCrudRouteFactoryRegistry routeFactory;
    private final TurboCrudConfigService configService;

    public SubmenuRouteFactory(TurboCrudRouteFactoryRegistry routeFactory, TurboCrudConfigService configService) {
        this.routeFactory = routeFactory;
        this.configService = configService;
    }

    @Override
    public Component renderRoute(Integer currentPathIndex,
                                 TurboCrudPathToRouteResolver routeResolver,
                                 @Nullable DetailRouteSetting detailRouteSetting) {
        return new Submenu(currentPathIndex, routeResolver, routeFactory, configService);
    }

    @Override
    public boolean isContainerRoute() {
        return true;
    }
}
