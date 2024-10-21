package com.github.appreciated.turbo_crud.ui.factories.route.submenu;

import com.github.appreciated.turbo_crud.config.TurboCrudPathToRouteResolver;
import com.github.appreciated.turbo_crud.ui.factories.route.TurboCrudRouteFactory;
import com.github.appreciated.turbo_crud.ui.factories.route.TurboCrudRouteFactoryRegistry;
import com.vaadin.flow.component.Component;

public class DefaultSubmenuRouteFactoryImpl implements TurboCrudRouteFactory {

    private final TurboCrudRouteFactoryRegistry routeFactory;

    public DefaultSubmenuRouteFactoryImpl(TurboCrudRouteFactoryRegistry routeFactory) {
        this.routeFactory = routeFactory;
    }

    @Override
    public Component renderRoute(Integer currentPathIndex,
                                 TurboCrudPathToRouteResolver routeResolver,
                                 boolean isWrapped,
                                 boolean hideHeader) {
        return  new Submenu(currentPathIndex, routeResolver, routeFactory);
    }

    @Override
    public boolean isContainerRoute() {
        return true;
    }
}
