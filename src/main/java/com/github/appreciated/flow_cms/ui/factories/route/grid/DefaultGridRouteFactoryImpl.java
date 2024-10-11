package com.github.appreciated.flow_cms.ui.factories.route.grid;

import com.github.appreciated.flow_cms.config.model.RouteConfig;
import com.github.appreciated.flow_cms.service.FlowCmsEntityManagerService;
import com.github.appreciated.flow_cms.ui.components.RouteHeader;
import com.github.appreciated.flow_cms.ui.factories.icon.FlowCmsIconFactory;
import com.github.appreciated.flow_cms.ui.factories.item.FlowCmsItemFactoryRegistry;
import com.github.appreciated.flow_cms.ui.factories.route.grid.components.VirtualItemGrid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class DefaultGridRouteFactoryImpl extends VerticalLayout {
    public DefaultGridRouteFactoryImpl(int i, RouteConfig config, String route, FlowCmsEntityManagerService entityManagerService, FlowCmsItemFactoryRegistry entityCardRendererFactory, FlowCmsIconFactory iconFactory) {
        HorizontalLayout header = new RouteHeader(config, iconFactory);
        header.setPadding(true);
        VirtualItemGrid virtualGrid = new VirtualItemGrid(i, route, config, entityManagerService, entityCardRendererFactory);
        add(header, virtualGrid);
        setSizeFull();
        setPadding(false);
        setSpacing(false);
    }
}
