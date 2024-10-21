package com.github.appreciated.turbo_crud.ui.factories.route.master_detail;

import com.github.appreciated.turbo_crud.config.TurboCrudPathToRouteResolver;
import com.github.appreciated.turbo_crud.service.TurboCrudEntityManagerService;
import com.github.appreciated.turbo_crud.ui.factories.icon.TurboCrudIconFactory;
import com.github.appreciated.turbo_crud.ui.factories.item.TurboCrudItemFactoryRegistry;
import com.github.appreciated.turbo_crud.ui.factories.route.TurboCrudRouteFactory;
import com.github.appreciated.turbo_crud.ui.factories.route.TurboCrudRouteFactoryRegistry;
import com.vaadin.flow.component.Component;

public class DefaultMasterDetailRouteFactoryImpl implements TurboCrudRouteFactory {

    private final TurboCrudEntityManagerService entityManagerService;
    private final TurboCrudItemFactoryRegistry itemFactoryRegistry;
    private final TurboCrudRouteFactoryRegistry routeFactory;
    private final TurboCrudIconFactory iconFactory;

    public DefaultMasterDetailRouteFactoryImpl(TurboCrudEntityManagerService entityManagerService,
                                               TurboCrudItemFactoryRegistry itemFactoryRegistry,
                                               TurboCrudRouteFactoryRegistry routeFactory,
                                               TurboCrudIconFactory iconFactory) {
        this.entityManagerService = entityManagerService;
        this.itemFactoryRegistry = itemFactoryRegistry;
        this.routeFactory = routeFactory;
        this.iconFactory = iconFactory;
    }

    @Override
    public Component renderRoute(Integer currentPathIndex, TurboCrudPathToRouteResolver pathVariables, boolean isWrapped, boolean hideHeader) {
        return new MasterDetail(currentPathIndex, pathVariables, entityManagerService, itemFactoryRegistry, routeFactory, iconFactory);
    }

    @Override
    public boolean isContainerRoute() {
        return true;
    }

}
