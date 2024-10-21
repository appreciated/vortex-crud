package com.github.appreciated.turbo_crud.ui.factories.route.grid;

import com.github.appreciated.turbo_crud.config.TurboCrudPathToRouteResolver;
import com.github.appreciated.turbo_crud.config.model.Route;
import com.github.appreciated.turbo_crud.service.TurboCrudEntityManagerService;
import com.github.appreciated.turbo_crud.ui.factories.icon.TurboCrudIconFactory;
import com.github.appreciated.turbo_crud.ui.factories.item.TurboCrudItemFactoryRegistry;
import com.github.appreciated.turbo_crud.ui.factories.route.TurboCrudRouteFactory;
import com.vaadin.flow.component.Component;

public class DefaultGridRouteFactoryImpl implements TurboCrudRouteFactory {

    private final TurboCrudEntityManagerService entityManagerService;
    private final TurboCrudItemFactoryRegistry itemFactoryRegistry;
    private final TurboCrudIconFactory iconFactory;

    public DefaultGridRouteFactoryImpl(TurboCrudEntityManagerService entityManagerService,
                                       TurboCrudItemFactoryRegistry itemFactoryRegistry,
                                       TurboCrudIconFactory iconFactory) {
        this.entityManagerService = entityManagerService;
        this.itemFactoryRegistry = itemFactoryRegistry;
        this.iconFactory = iconFactory;
    }

    @Override
    public Component renderRoute(Integer currentPathIndex,
                                 TurboCrudPathToRouteResolver pathVariables,
                                 boolean isWrapped,
                                 boolean hideHeader) {

        Route route = pathVariables.getRouteForIndex(currentPathIndex);

        return new Grid(pathVariables,
                route,
                entityManagerService,
                itemFactoryRegistry,
                iconFactory);
    }

    @Override
    public boolean isContainerRoute() {
        return false;
    }

}