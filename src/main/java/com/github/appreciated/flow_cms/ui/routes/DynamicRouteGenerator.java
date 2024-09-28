package com.github.appreciated.flow_cms.ui.routes;

import com.github.appreciated.flow_cms.service.FlowCmsConfigService;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;

import java.util.Set;

@org.springframework.stereotype.Component
public class DynamicRouteGenerator implements VaadinServiceInitListener {

    private final FlowCmsConfigService flowCmsConfigService;

    public DynamicRouteGenerator(FlowCmsConfigService flowCmsConfigService) {
        this.flowCmsConfigService = flowCmsConfigService;
    }

    @Override
    public void serviceInit(ServiceInitEvent event) {
        Set<String> keys = flowCmsConfigService.getViews().keySet();
        keys.forEach(this::registerRoute);
    }

    public void registerRoute(String collection) {
        RouteConfiguration configuration = RouteConfiguration.forApplicationScope();
        configuration.setRoute("/view/"+collection, DynamicRoute.class);
    }

}