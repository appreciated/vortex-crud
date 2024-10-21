package com.github.appreciated.turbo_crud.ui.factories.route.list;

import com.github.appreciated.turbo_crud.config.TurboCrudPathToRouteResolver;
import com.github.appreciated.turbo_crud.service.TurboCrudConfigService;
import com.github.appreciated.turbo_crud.service.TurboCrudEntityManagerService;
import com.github.appreciated.turbo_crud.ui.factories.icon.TurboCrudIconFactory;
import com.github.appreciated.turbo_crud.ui.factories.route.TurboCrudRouteFactory;
import com.vaadin.flow.component.Component;

public class DefaultListRouteFactoryImpl implements TurboCrudRouteFactory {

    private final TurboCrudEntityManagerService entityManagerService;
    private final TurboCrudConfigService configService;
    private final TurboCrudListColumnCallbackRegistry columnCallbackRegistry;
    private final TurboCrudIconFactory iconFactory;

    public DefaultListRouteFactoryImpl(TurboCrudEntityManagerService entityManagerService,
                                       TurboCrudConfigService configService,
                                       TurboCrudListColumnCallbackRegistry columnCallbackRegistry,
                                       TurboCrudIconFactory iconFactory) {
        this.entityManagerService = entityManagerService;
        this.configService = configService;
        this.columnCallbackRegistry = columnCallbackRegistry;
        this.iconFactory = iconFactory;
    }

    @Override
    public Component renderRoute(Integer currentPathIndex,
                                 TurboCrudPathToRouteResolver pathVariables,
                                 boolean isWrapped,
                                 boolean hideHeader) {

        return new List(currentPathIndex, pathVariables, entityManagerService, configService, columnCallbackRegistry, iconFactory);
    }

    @Override
    public boolean isContainerRoute() {
        return false;
    }

}
