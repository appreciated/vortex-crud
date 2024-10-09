package com.github.appreciated.flow_cms.ui.route_renderer.grid;

import com.github.appreciated.flow_cms.config.model.RouteConfig;
import com.github.appreciated.flow_cms.service.DynamicEntityManagerService;
import com.github.appreciated.flow_cms.ui.entity_item_renderer.card.FlowCmsEntityItemRendererFactory;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class DefaultGridView extends VerticalLayout {
    public DefaultGridView(int i, RouteConfig config, DynamicEntityManagerService entityManagerService, FlowCmsEntityItemRendererFactory entityCardRendererFactory) {
        HorizontalLayout header = new HorizontalLayout(new H2(getTranslation(config.getTitle())));
        header.setPadding(true);
        VirtualItemGrid virtualGrid = new VirtualItemGrid(i, config, entityManagerService, entityCardRendererFactory);
        add(header, virtualGrid);
        setSizeFull();
        setPadding(false);
        setSpacing(false);
    }
}
