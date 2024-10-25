package com.github.appreciated.turbo_crud.ui.factories.route.submenu;

import com.github.appreciated.turbo_crud.config.TurboCrudPathToRouteResolver;
import com.github.appreciated.turbo_crud.service.TurboCrudConfigService;
import com.github.appreciated.turbo_crud.ui.factories.icon.TurboCrudIconFactory;
import com.github.appreciated.turbo_crud.ui.factories.route.DetailRouteSetting;
import com.github.appreciated.turbo_crud.ui.factories.route.TurboCrudRouteFactory;
import com.github.appreciated.turbo_crud.ui.factories.route.TurboCrudRouteFactoryRegistry;
import com.vaadin.flow.component.Component;
import jakarta.annotation.Nullable;

public class DefaultSubmenuRouteFactoryImpl implements TurboCrudRouteFactory {

    private final TurboCrudRouteFactoryRegistry routeFactory;
    private final TurboCrudConfigService configService;
    private final TurboCrudIconFactory iconFactory;

    public DefaultSubmenuRouteFactoryImpl(TurboCrudRouteFactoryRegistry routeFactory, TurboCrudConfigService configService, TurboCrudIconFactory iconFactory) {
        this.routeFactory = routeFactory;
        this.configService = configService;
        this.iconFactory = iconFactory;
    }

    @Override
    public Component renderRoute(Integer currentPathIndex,
                                 TurboCrudPathToRouteResolver routeResolver,
                                 @Nullable DetailRouteSetting detailRouteSetting) {
        return new Submenu(currentPathIndex, routeResolver, routeFactory, iconFactory, configService);
    }

    @Override
    public boolean isContainerRoute() {
        return true;
    }
}
