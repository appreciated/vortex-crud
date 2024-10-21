package com.github.appreciated.turbo_crud.ui.factories.route.list;

import com.github.appreciated.turbo_crud.config.TurboCrudPathToRouteResolver;
import com.github.appreciated.turbo_crud.service.TurboCrudConfigService;
import com.github.appreciated.turbo_crud.ui.factories.entity_manager.TurboCrudEntityManagerFactoryRegistry;
import com.github.appreciated.turbo_crud.ui.factories.icon.TurboCrudIconFactory;
import com.github.appreciated.turbo_crud.ui.factories.route.TurboCrudRouteFactory;
import com.vaadin.flow.component.Component;

public class DefaultListRouteFactoryImpl implements TurboCrudRouteFactory {

    private final TurboCrudEntityManagerFactoryRegistry entityManagerFactoryRegistry;
    private final TurboCrudConfigService configService;
    private final TurboCrudListColumnCallbackRegistry columnCallbackRegistry;
    private final TurboCrudIconFactory iconFactory;

    public DefaultListRouteFactoryImpl( TurboCrudEntityManagerFactoryRegistry entityManagerFactoryRegistry,
                                       TurboCrudConfigService configService,
                                       TurboCrudListColumnCallbackRegistry columnCallbackRegistry,
                                       TurboCrudIconFactory iconFactory) {
        this.entityManagerFactoryRegistry = entityManagerFactoryRegistry;
        this.configService = configService;
        this.columnCallbackRegistry = columnCallbackRegistry;
        this.iconFactory = iconFactory;
    }

    @Override
    public Component renderRoute(Integer currentPathIndex,
                                 TurboCrudPathToRouteResolver routeResolver,
                                 boolean isWrapped,
                                 boolean hideHeader) {

        return new List(currentPathIndex, routeResolver, entityManagerFactoryRegistry, configService, columnCallbackRegistry, iconFactory);
    }

    @Override
    public boolean isContainerRoute() {
        return false;
    }

}
