package com.github.appreciated.flow_cms.ui.factories.route.grid;

import com.github.appreciated.flow_cms.config.model.RouteConfig;
import com.github.appreciated.flow_cms.service.DynamicEntityManagerService;
import com.github.appreciated.flow_cms.ui.factories.card.FlowCmsEntityItemRendererFactory;
import com.github.appreciated.flow_cms.ui.factories.route.grid.components.VirtualItemGrid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class DefaultGridRouteImpl extends VerticalLayout {
    public DefaultGridRouteImpl(int i, RouteConfig config, DynamicEntityManagerService entityManagerService, FlowCmsEntityItemRendererFactory entityCardRendererFactory) {
        HorizontalLayout header = new HorizontalLayout(new H2(getTranslation(config.getTitle())));
        header.setPadding(true);
        VirtualItemGrid virtualGrid = new VirtualItemGrid(i, config, entityManagerService, entityCardRendererFactory);
        add(header, virtualGrid);
        setSizeFull();
        setPadding(false);
        setSpacing(false);
    }
}
