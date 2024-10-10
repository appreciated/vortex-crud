package com.github.appreciated.flow_cms.ui.routes;

import com.github.appreciated.flow_cms.config.model.DetailFactory;
import com.github.appreciated.flow_cms.config.model.RouteConfig;
import com.github.appreciated.flow_cms.service.FlowCmsEntityManagerService;
import com.github.appreciated.flow_cms.service.FlowCmsConfigService;
import com.github.appreciated.flow_cms.service.GenericEntity;
import com.github.appreciated.flow_cms.ui.factories.detail.FlowCmsDetailFactoryRegistry;
import com.github.appreciated.flow_cms.ui.factories.route.DefaultRouteFactoryRegistryImpl;
import com.github.appreciated.flow_cms.ui.factories.route.FlowCmsRouteFactoryRegistry;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;

import java.util.Objects;

/**
 * A dynamic route component that renders different views based on the route path.
 * It uses the {@link FlowCmsConfigService} to retrieve configuration details and the {@link DefaultRouteFactoryRegistryImpl}
 * to generate the appropriate view container for the specified route.
 * Implements {@link BeforeEnterObserver} to handle navigation events and dynamically update the view.
 */

@Route(value = "view/:path*", layout = ProxyRouterLayout.class)
public class DynamicRoute extends Div implements BeforeEnterObserver {

    private final FlowCmsConfigService flowCmsConfigService;
    private final FlowCmsRouteFactoryRegistry containerFactory;
    private final FlowCmsDetailFactoryRegistry detailRendererFactory;
    private final FlowCmsEntityManagerService FlowCmsEntityManagerService;

    public DynamicRoute(FlowCmsConfigService flowCmsConfigService, FlowCmsRouteFactoryRegistry containerFactory, FlowCmsDetailFactoryRegistry detailRendererFactory, FlowCmsEntityManagerService FlowCmsEntityManagerService) {
        this.flowCmsConfigService = flowCmsConfigService;
        this.containerFactory = containerFactory;
        this.detailRendererFactory = detailRendererFactory;
        this.FlowCmsEntityManagerService = FlowCmsEntityManagerService;
        setSizeFull();
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        String path = event.getRouteParameters().get("path").orElse("");
        removeAll();

        RouteConfig configForRoute = flowCmsConfigService.getConfigForRoute(path);
        if (path.split("/").length == 1 || Objects.equals(configForRoute.getFactory(), "master-detail")) {
            Component viewContainer = containerFactory.createViewContainer(configForRoute);
            add(viewContainer);
        } else {
            DetailFactory detailFactory = configForRoute.getFactoryConfiguration().getDetailFactory();
            GenericEntity recordById = FlowCmsEntityManagerService.getRecordById(configForRoute.getTable(), path.split("/")[1]);
            add(detailRendererFactory.getFactory(detailFactory).renderDetail(configForRoute, recordById, false));
        }
    }
}