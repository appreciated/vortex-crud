package com.github.appreciated.flow_cms.ui.factories.route.grid;

import com.github.appreciated.flow_cms.config.model.RouteConfig;
import com.github.appreciated.flow_cms.service.FlowCmsEntityManagerService;
import com.github.appreciated.flow_cms.ui.components.RouteHeader;
import com.github.appreciated.flow_cms.ui.factories.icon.FlowCmsIcon;
import com.github.appreciated.flow_cms.ui.factories.item.FlowCmsItemRendererFactory;
import com.github.appreciated.flow_cms.ui.factories.route.grid.components.VirtualItemGrid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class DefaultGridRouteImpl extends VerticalLayout {
    public DefaultGridRouteImpl(int i, RouteConfig config, FlowCmsEntityManagerService entityManagerService, FlowCmsItemRendererFactory entityCardRendererFactory, FlowCmsIcon flowCmsIcon) {
        HorizontalLayout header = new RouteHeader(config, flowCmsIcon);
        header.setPadding(true);
        VirtualItemGrid virtualGrid = new VirtualItemGrid(i, config, entityManagerService, entityCardRendererFactory);
        add(header, virtualGrid);
        setSizeFull();
        setPadding(false);
        setSpacing(false);
    }
}
