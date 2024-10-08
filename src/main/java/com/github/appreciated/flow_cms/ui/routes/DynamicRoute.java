package com.github.appreciated.flow_cms.ui.routes;

import com.github.appreciated.flow_cms.service.FlowCmsConfigService;
import com.github.appreciated.flow_cms.ui.components.FlowCmsComponentFactory;
import com.github.appreciated.flow_cms.ui.route_renderer.DefaultRouteRendererFactoryImpl;
import com.github.appreciated.flow_cms.ui.router_layout.ProxyRouterLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * A dynamic route component that renders different views based on the route path.
 * It uses the {@link FlowCmsConfigService} to retrieve configuration details and the {@link DefaultRouteRendererFactoryImpl}
 * to generate the appropriate view container for the specified route.
 * Implements {@link BeforeEnterObserver} to handle navigation events and dynamically update the view.
 */

@Route(value = "view/:path*", layout = ProxyRouterLayout.class)
public class DynamicRoute extends Div implements BeforeEnterObserver {

    private final FlowCmsConfigService flowCmsConfigService;
    private final DefaultRouteRendererFactoryImpl containerFactory;

    public DynamicRoute(@Autowired FlowCmsConfigService flowCmsConfigService, @Autowired DefaultRouteRendererFactoryImpl containerFactory) {
        this.flowCmsConfigService = flowCmsConfigService;
        this.containerFactory = containerFactory;
        setSizeFull();
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        String path = event.getRouteParameters().get("path").orElse("");
        removeAll();

        Component viewContainer = containerFactory.createViewContainer(flowCmsConfigService.getConfigForRoute(path));
        add(viewContainer);
    }
}