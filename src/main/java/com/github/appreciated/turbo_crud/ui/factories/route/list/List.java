package com.github.appreciated.turbo_crud.ui.factories.route.list;

import com.github.appreciated.turbo_crud.config.TurboCrudPathToRouteResolver;
import com.github.appreciated.turbo_crud.config.model.Route;
import com.github.appreciated.turbo_crud.service.TurboCrudConfigService;
import com.github.appreciated.turbo_crud.ui.components.RouteHeader;
import com.github.appreciated.turbo_crud.ui.factories.entity_manager.TurboCrudEntityManagerFactoryRegistry;
import com.github.appreciated.turbo_crud.ui.factories.icon.TurboCrudIconFactory;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class List extends VerticalLayout {
    public List(Integer currentPathIndex,
                TurboCrudPathToRouteResolver routeResolver,
                TurboCrudEntityManagerFactoryRegistry entityManagerFactoryRegistry,
                TurboCrudConfigService configService,
                TurboCrudListColumnCallbackRegistry columnCallbackRegistry,
                TurboCrudIconFactory iconFactory) {

        Route route = routeResolver.getRouteForIndex(currentPathIndex);
        HorizontalLayout header = new RouteHeader(route, iconFactory);
        header.setPadding(true);

        GenericEntityGrid entityGrid = new GenericEntityGrid(routeResolver, route, entityManagerFactoryRegistry, configService, columnCallbackRegistry);
        add(header, entityGrid);
        setSizeFull();
        setPadding(false);
        setSpacing(false);
    }
}
