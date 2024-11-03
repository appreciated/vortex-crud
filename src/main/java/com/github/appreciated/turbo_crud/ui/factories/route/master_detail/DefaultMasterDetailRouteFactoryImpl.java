package com.github.appreciated.turbo_crud.ui.factories.route.master_detail;

import com.github.appreciated.turbo_crud.config.TurboCrudPathToRouteResolver;
import com.github.appreciated.turbo_crud.service.TurboCrudConfigService;
import com.github.appreciated.turbo_crud.entity.manager.TurboCrudEntityManagerFactoryRegistry;
import com.github.appreciated.turbo_crud.ui.factories.icon.TurboCrudIconFactory;
import com.github.appreciated.turbo_crud.ui.factories.item.TurboCrudItemFactoryRegistry;
import com.github.appreciated.turbo_crud.ui.factories.route.DetailRouteSetting;
import com.github.appreciated.turbo_crud.ui.factories.route.TurboCrudRouteFactory;
import com.github.appreciated.turbo_crud.ui.factories.route.TurboCrudRouteFactoryRegistry;
import com.vaadin.flow.component.Component;
import jakarta.annotation.Nullable;

public class DefaultMasterDetailRouteFactoryImpl implements TurboCrudRouteFactory {

    private final TurboCrudEntityManagerFactoryRegistry entityManagerFactoryRegistry;
    private final TurboCrudItemFactoryRegistry itemFactoryRegistry;
    private final TurboCrudRouteFactoryRegistry routeFactory;
    private final TurboCrudIconFactory iconFactory;
    private final TurboCrudConfigService configService;

    public DefaultMasterDetailRouteFactoryImpl(TurboCrudEntityManagerFactoryRegistry entityManagerFactoryRegistry,
                                               TurboCrudItemFactoryRegistry itemFactoryRegistry,
                                               TurboCrudRouteFactoryRegistry routeFactory,
                                               TurboCrudIconFactory iconFactory,
                                               TurboCrudConfigService configService
    ) {
        this.entityManagerFactoryRegistry = entityManagerFactoryRegistry;
        this.itemFactoryRegistry = itemFactoryRegistry;
        this.routeFactory = routeFactory;
        this.iconFactory = iconFactory;
        this.configService = configService;
    }

    @Override
    public Component renderRoute(Integer currentPathIndex,
                                 TurboCrudPathToRouteResolver routeResolver,
                                 @Nullable DetailRouteSetting detailRouteSetting) {
        return new MasterDetail(currentPathIndex, routeResolver, entityManagerFactoryRegistry, itemFactoryRegistry, routeFactory, configService, iconFactory);
    }

    @Override
    public boolean isContainerRoute() {
        return true;
    }

}
