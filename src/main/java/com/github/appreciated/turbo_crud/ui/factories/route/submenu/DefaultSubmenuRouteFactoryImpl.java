package com.github.appreciated.turbo_crud.ui.factories.route.submenu;

import com.github.appreciated.turbo_crud.config.TurboCrudPathToRouteResolver;
import com.github.appreciated.turbo_crud.service.TurboCrudConfigService;
import com.github.appreciated.turbo_crud.ui.factories.route.TurboCrudRouteFactory;
import com.github.appreciated.turbo_crud.ui.factories.route.TurboCrudRouteFactoryRegistry;
import com.vaadin.flow.component.Component;

public class DefaultSubmenuRouteFactoryImpl implements TurboCrudRouteFactory {

    private final TurboCrudRouteFactoryRegistry routeFactory;
    private final TurboCrudConfigService configService;

    public DefaultSubmenuRouteFactoryImpl(TurboCrudRouteFactoryRegistry routeFactory, TurboCrudConfigService configService) {
        this.routeFactory = routeFactory;
        this.configService = configService;
    }

    @Override
    public Component renderRoute(Integer currentPathIndex,
                                 TurboCrudPathToRouteResolver routeResolver,
                                 boolean isWrapped,
                                 boolean hideHeader) {
        return new Submenu(currentPathIndex, routeResolver, routeFactory, configService);
    }

    @Override
    public boolean isContainerRoute() {
        return true;
    }
}
