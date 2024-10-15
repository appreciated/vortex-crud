package com.github.appreciated.turbo_crud.ui.factories.route.list;

import com.github.appreciated.turbo_crud.config.model.RouteConfig;
import com.github.appreciated.turbo_crud.service.TurboCrudConfigService;
import com.github.appreciated.turbo_crud.service.TurboCrudEntityManagerService;
import com.github.appreciated.turbo_crud.ui.components.RouteHeader;
import com.github.appreciated.turbo_crud.ui.factories.icon.TurboCrudIconFactory;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class DefaultListRouteFactoryImpl extends VerticalLayout {
    public DefaultListRouteFactoryImpl(int i, RouteConfig config, String route, TurboCrudEntityManagerService entityManagerService, TurboCrudConfigService configService, TurboCrudListColumnCallbackRegistry columnCallbackRegistry, TurboCrudIconFactory iconFactory) {
        HorizontalLayout header = new RouteHeader(config, iconFactory);
        header.setPadding(true);
        GenericEntityGrid entityGrid = new GenericEntityGrid(i, config, route, entityManagerService, configService, columnCallbackRegistry);
        add(header, entityGrid);
        setSizeFull();
        setPadding(false);
        setSpacing(false);
    }
}
