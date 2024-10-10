package com.github.appreciated.flow_cms.ui.routes;

import com.github.appreciated.flow_cms.config.model.DetailRenderer;
import com.github.appreciated.flow_cms.config.model.RouteConfig;
import com.github.appreciated.flow_cms.service.DynamicEntityManagerService;
import com.github.appreciated.flow_cms.service.FlowCmsConfigService;
import com.github.appreciated.flow_cms.service.GenericEntity;
import com.github.appreciated.flow_cms.ui.entity_detail.FlowCmsEntityDetailRendererFactory;
import com.github.appreciated.flow_cms.ui.route_renderer.DefaultRouteRendererFactoryImpl;
import com.github.appreciated.flow_cms.ui.route_renderer.FlowCmsRouteRendererFactory;
import com.github.appreciated.flow_cms.ui.router_layout.components.ProxyRouterLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;

import java.util.Objects;

/**
 * A dynamic route component that renders different views based on the route path.
 * It uses the {@link FlowCmsConfigService} to retrieve configuration details and the {@link DefaultRouteRendererFactoryImpl}
 * to generate the appropriate view container for the specified route.
 * Implements {@link BeforeEnterObserver} to handle navigation events and dynamically update the view.
 */

@Route(value = "view/:path*", layout = ProxyRouterLayout.class)
public class DynamicRoute extends Div implements BeforeEnterObserver {

    private final FlowCmsConfigService flowCmsConfigService;
    private final FlowCmsRouteRendererFactory containerFactory;
    private final FlowCmsEntityDetailRendererFactory detailRendererFactory;
    private final DynamicEntityManagerService dynamicEntityManagerService;

    public DynamicRoute(FlowCmsConfigService flowCmsConfigService, FlowCmsRouteRendererFactory containerFactory, FlowCmsEntityDetailRendererFactory detailRendererFactory, DynamicEntityManagerService dynamicEntityManagerService) {
        this.flowCmsConfigService = flowCmsConfigService;
        this.containerFactory = containerFactory;
        this.detailRendererFactory = detailRendererFactory;
        this.dynamicEntityManagerService = dynamicEntityManagerService;
        setSizeFull();
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        String path = event.getRouteParameters().get("path").orElse("");
        removeAll();

        RouteConfig configForRoute = flowCmsConfigService.getConfigForRoute(path);
        if (path.split("/").length == 1 || Objects.equals(configForRoute.getRenderer(), "master-detail")) {
            Component viewContainer = containerFactory.createViewContainer(configForRoute);
            add(viewContainer);
        } else {
            DetailRenderer detailRenderer = configForRoute.getRenderConfiguration().getDetailRenderer();
            GenericEntity recordById = dynamicEntityManagerService.getRecordById(configForRoute.getTable(), path.split("/")[1]);
            add(detailRendererFactory.getRenderer(detailRenderer).renderDetail(configForRoute, recordById, false));
        }
    }
}