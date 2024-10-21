package com.github.appreciated.turbo_crud.ui.factories.route.grid;

import com.github.appreciated.turbo_crud.config.TurboCrudPathToRouteResolver;
import com.github.appreciated.turbo_crud.config.model.Route;
import com.github.appreciated.turbo_crud.ui.components.RouteHeader;
import com.github.appreciated.turbo_crud.ui.factories.entity_manager.TurboCrudEntityManagerFactoryRegistry;
import com.github.appreciated.turbo_crud.ui.factories.icon.TurboCrudIconFactory;
import com.github.appreciated.turbo_crud.ui.factories.item.TurboCrudItemFactoryRegistry;
import com.github.appreciated.turbo_crud.ui.factories.route.grid.components.VirtualItemGrid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class Grid extends VerticalLayout {
    public Grid(TurboCrudPathToRouteResolver routeResolver, Route config, TurboCrudEntityManagerFactoryRegistry entityManagerFactoryRegistry, TurboCrudItemFactoryRegistry itemFactoryRegistry, TurboCrudIconFactory iconFactory) {
        HorizontalLayout header = new RouteHeader(config, iconFactory);
        header.setPadding(true);
        VirtualItemGrid virtualGrid = new VirtualItemGrid(routeResolver, config, entityManagerFactoryRegistry, itemFactoryRegistry);
        add(header, virtualGrid);
        setSizeFull();
        setPadding(false);
        setSpacing(false);
    }
}

