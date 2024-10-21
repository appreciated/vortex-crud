package com.github.appreciated.turbo_crud.ui.factories.route.grid;

import com.github.appreciated.turbo_crud.config.TurboCrudPathToRouteResolver;
import com.github.appreciated.turbo_crud.config.model.Route;
import com.github.appreciated.turbo_crud.ui.factories.entity_manager.TurboCrudEntityManagerFactoryRegistry;
import com.github.appreciated.turbo_crud.ui.factories.icon.TurboCrudIconFactory;
import com.github.appreciated.turbo_crud.ui.factories.item.TurboCrudItemFactoryRegistry;
import com.github.appreciated.turbo_crud.ui.factories.route.TurboCrudRouteFactory;
import com.vaadin.flow.component.Component;

public class DefaultGridRouteFactoryImpl implements TurboCrudRouteFactory {

    private final TurboCrudEntityManagerFactoryRegistry entityManagerFactoryRegistry;
    private final TurboCrudItemFactoryRegistry itemFactoryRegistry;
    private final TurboCrudIconFactory iconFactory;

    public DefaultGridRouteFactoryImpl(TurboCrudEntityManagerFactoryRegistry entityManagerFactoryRegistry,
                                       TurboCrudItemFactoryRegistry itemFactoryRegistry,
                                       TurboCrudIconFactory iconFactory) {
        this.entityManagerFactoryRegistry = entityManagerFactoryRegistry;
        this.itemFactoryRegistry = itemFactoryRegistry;
        this.iconFactory = iconFactory;
    }

    @Override
    public Component renderRoute(Integer currentPathIndex,
                                 TurboCrudPathToRouteResolver routeResolver,
                                 boolean isWrapped,
                                 boolean hideHeader) {

        Route route = routeResolver.getRouteForIndex(currentPathIndex);

        return new Grid(routeResolver,
                route,
                entityManagerFactoryRegistry,
                itemFactoryRegistry,
                iconFactory);
    }

    @Override
    public boolean isContainerRoute() {
        return false;
    }

}