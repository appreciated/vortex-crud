package com.github.appreciated.turbo_crud.ui.factories.route.master_detail;

import com.github.appreciated.turbo_crud.config.TurboCrudPathToRouteResolver;
import com.github.appreciated.turbo_crud.ui.factories.entity_manager.TurboCrudEntityManagerFactoryRegistry;
import com.github.appreciated.turbo_crud.ui.factories.icon.TurboCrudIconFactory;
import com.github.appreciated.turbo_crud.ui.factories.item.TurboCrudItemFactoryRegistry;
import com.github.appreciated.turbo_crud.ui.factories.route.TurboCrudRouteFactory;
import com.github.appreciated.turbo_crud.ui.factories.route.TurboCrudRouteFactoryRegistry;
import com.vaadin.flow.component.Component;

public class DefaultMasterDetailRouteFactoryImpl implements TurboCrudRouteFactory {

    private final TurboCrudEntityManagerFactoryRegistry entityManagerFactoryRegistry;
    private final TurboCrudItemFactoryRegistry itemFactoryRegistry;
    private final TurboCrudRouteFactoryRegistry routeFactory;
    private final TurboCrudIconFactory iconFactory;

    public DefaultMasterDetailRouteFactoryImpl(TurboCrudEntityManagerFactoryRegistry entityManagerFactoryRegistry,
                                               TurboCrudItemFactoryRegistry itemFactoryRegistry,
                                               TurboCrudRouteFactoryRegistry routeFactory,
                                               TurboCrudIconFactory iconFactory) {
        this.entityManagerFactoryRegistry = entityManagerFactoryRegistry;
        this.itemFactoryRegistry = itemFactoryRegistry;
        this.routeFactory = routeFactory;
        this.iconFactory = iconFactory;
    }

    @Override
    public Component renderRoute(Integer currentPathIndex, TurboCrudPathToRouteResolver routeResolver, boolean isWrapped, boolean hideHeader) {
        return new MasterDetail(currentPathIndex, routeResolver, entityManagerFactoryRegistry, itemFactoryRegistry, routeFactory, iconFactory);
    }

    @Override
    public boolean isContainerRoute() {
        return true;
    }

}
