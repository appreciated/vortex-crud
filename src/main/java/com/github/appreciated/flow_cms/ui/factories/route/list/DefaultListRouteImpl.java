package com.github.appreciated.flow_cms.ui.factories.route.list;

import com.github.appreciated.flow_cms.config.model.RouteConfig;
import com.github.appreciated.flow_cms.service.FlowCmsEntityManagerService;
import com.github.appreciated.flow_cms.service.FlowCmsConfigService;
import com.github.appreciated.flow_cms.ui.components.RouteHeader;
import com.github.appreciated.flow_cms.ui.factories.icon.FlowCmsIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class DefaultListRouteImpl extends VerticalLayout {
    public DefaultListRouteImpl(int i, RouteConfig config, FlowCmsEntityManagerService entityManagerService, FlowCmsConfigService cmsConfigService, FlowCmsListColumnFactory listColumnFactory, FlowCmsIcon flowCmsIcon) {
        HorizontalLayout header = new RouteHeader(config, flowCmsIcon);
        header.setPadding(true);
        GenericEntityGrid entityGrid = new GenericEntityGrid(i, config, entityManagerService, cmsConfigService, listColumnFactory);
        add(header, entityGrid);
        setSizeFull();
        setPadding(false);
        setSpacing(false);
    }
}
