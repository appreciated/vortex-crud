package com.github.appreciated.turbo_crud.ui.factories.route.grid;

import com.github.appreciated.turbo_crud.config.model.RouteConfig;
import com.github.appreciated.turbo_crud.service.TurboCrudEntityManagerService;
import com.github.appreciated.turbo_crud.ui.components.RouteHeader;
import com.github.appreciated.turbo_crud.ui.factories.icon.TurboCrudIconFactory;
import com.github.appreciated.turbo_crud.ui.factories.item.TurboCrudItemFactoryRegistry;
import com.github.appreciated.turbo_crud.ui.factories.route.grid.components.VirtualItemGrid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class DefaultGridRouteFactoryImpl extends VerticalLayout {
    public DefaultGridRouteFactoryImpl(int i, RouteConfig config, String route, TurboCrudEntityManagerService entityManagerService, TurboCrudItemFactoryRegistry entityCardRendererFactory, TurboCrudIconFactory iconFactory) {
        HorizontalLayout header = new RouteHeader(config, iconFactory);
        header.setPadding(true);
        VirtualItemGrid virtualGrid = new VirtualItemGrid(i, route, config, entityManagerService, entityCardRendererFactory);
        add(header, virtualGrid);
        setSizeFull();
        setPadding(false);
        setSpacing(false);
    }
}
