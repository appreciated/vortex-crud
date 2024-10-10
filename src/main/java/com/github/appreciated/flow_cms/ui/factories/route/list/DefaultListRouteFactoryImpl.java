package com.github.appreciated.flow_cms.ui.factories.route.list;

import com.github.appreciated.flow_cms.config.model.RouteConfig;
import com.github.appreciated.flow_cms.service.FlowCmsEntityManagerService;
import com.github.appreciated.flow_cms.service.FlowCmsConfigService;
import com.github.appreciated.flow_cms.ui.components.RouteHeader;
import com.github.appreciated.flow_cms.ui.factories.icon.FlowCmsIconFactory;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class DefaultListRouteFactoryImpl extends VerticalLayout {
    public DefaultListRouteFactoryImpl(int i, RouteConfig config, FlowCmsEntityManagerService entityManagerService, FlowCmsConfigService configService, FlowCmsListColumnCallbackRegistry columnCallbackRegistry, FlowCmsIconFactory iconFactory) {
        HorizontalLayout header = new RouteHeader(config, iconFactory);
        header.setPadding(true);
        GenericEntityGrid entityGrid = new GenericEntityGrid(i, config, entityManagerService, configService, columnCallbackRegistry);
        add(header, entityGrid);
        setSizeFull();
        setPadding(false);
        setSpacing(false);
    }
}
