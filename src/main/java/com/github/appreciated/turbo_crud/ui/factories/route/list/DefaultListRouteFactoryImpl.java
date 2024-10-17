package com.github.appreciated.turbo_crud.ui.factories.route.list;

import com.github.appreciated.turbo_crud.config.TurboCrudPathSegments;
import com.github.appreciated.turbo_crud.config.model.Route;
import com.github.appreciated.turbo_crud.service.TurboCrudConfigService;
import com.github.appreciated.turbo_crud.service.TurboCrudEntityManagerService;
import com.github.appreciated.turbo_crud.ui.components.RouteHeader;
import com.github.appreciated.turbo_crud.ui.factories.icon.TurboCrudIconFactory;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class DefaultListRouteFactoryImpl extends VerticalLayout {
    public DefaultListRouteFactoryImpl(TurboCrudPathSegments pathVariables,
                                       Route config,
                                       TurboCrudEntityManagerService entityManagerService,
                                       TurboCrudConfigService configService,
                                       TurboCrudListColumnCallbackRegistry columnCallbackRegistry,
                                       TurboCrudIconFactory iconFactory) {
        HorizontalLayout header = new RouteHeader(config, iconFactory);
        header.setPadding(true);
        GenericEntityGrid entityGrid = new GenericEntityGrid(pathVariables, config, entityManagerService, configService, columnCallbackRegistry);
        add(header, entityGrid);
        setSizeFull();
        setPadding(false);
        setSpacing(false);
    }
}
