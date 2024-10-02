package com.github.appreciated.flow_cms.ui.routes;

import com.github.appreciated.flow_cms.service.FlowCmsConfigService;
import com.github.appreciated.flow_cms.ui.components.FlowCmsComponentFactory;
import com.github.appreciated.flow_cms.ui.router_layout.ProxyRouterLayout;
import com.github.appreciated.flow_cms.ui.route_renderer.DefaultRouteRendererFactoryImpl;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * Depending on the route a different component should be rendered using the {@link FlowCmsComponentFactory}.
 */

@Route(value = "view/:path*", layout = ProxyRouterLayout.class)
public class DynamicRoute extends Div implements BeforeEnterObserver {

    private final FlowCmsConfigService flowCmsConfigService;
    private final DefaultRouteRendererFactoryImpl containerFactory;

    private String path;

    /**
     * Constructor for DynamicView.
     *
     * @param flowCmsConfigService the service to retrieve configuration for the routes
     */
    public DynamicRoute(@Autowired FlowCmsConfigService flowCmsConfigService, @Autowired DefaultRouteRendererFactoryImpl containerFactory) {
        this.flowCmsConfigService = flowCmsConfigService;
        this.containerFactory = containerFactory;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        path = event.getRouteParameters().get("path").orElse("");
        removeAll();
        add(containerFactory.createViewContainer(flowCmsConfigService.getConfigForRoute(path)));
    }
}