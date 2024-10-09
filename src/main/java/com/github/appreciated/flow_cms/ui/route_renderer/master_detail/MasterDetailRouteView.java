package com.github.appreciated.flow_cms.ui.route_renderer.master_detail;

import com.github.appreciated.flow_cms.config.model.RouteConfig;
import com.github.appreciated.flow_cms.service.DynamicEntityManagerService;
import com.github.appreciated.flow_cms.ui.entity_detail.FlowCmsEntityDetailRendererFactory;
import com.github.appreciated.flow_cms.ui.entity_item_renderer.card.FlowCmsEntityItemRendererFactory;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class MasterDetailRouteView extends VerticalLayout {
    public MasterDetailRouteView(int i, RouteConfig config, DynamicEntityManagerService entityManagerService, FlowCmsEntityItemRendererFactory entityCardRendererFactory, FlowCmsEntityDetailRendererFactory detailRendererFactory) {
        HorizontalLayout header = new HorizontalLayout(new H3(getTranslation(config.getTitle())));
        header.setPadding(true);
        DefaultMasterDetailRenderer virtualGrid = new DefaultMasterDetailRenderer(i, config, entityManagerService, entityCardRendererFactory, detailRendererFactory);
        add(header, virtualGrid);
        setSizeFull();
        setPadding(false);
        setSpacing(false);
        getStyle().set("overflow", "hidden");
    }
}
