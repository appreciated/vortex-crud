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

    private final FlowCmsConfigService configService;
    private final FlowCmsRouteFactoryRegistry routeFactoryRegistry;
    private final FlowCmsDetailFactoryRegistry detailFactoryRegistry;
    private final FlowCmsEntityManagerService entityManagerService;

    public DynamicRoute(FlowCmsConfigService configService, FlowCmsRouteFactoryRegistry routeFactoryRegistry, FlowCmsDetailFactoryRegistry detailFactoryRegistry, FlowCmsEntityManagerService entityManagerService) {
        this.configService = configService;
        this.routeFactoryRegistry = routeFactoryRegistry;
        this.detailFactoryRegistry = detailFactoryRegistry;
        this.entityManagerService = entityManagerService;
        setSizeFull();
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        String path = event.getRouteParameters().get("path").orElse("");
        removeAll();

        RouteConfig configForRoute = configService.getConfigForRoute(path);
        if (path.split("/").length == 1 || Objects.equals(configForRoute.getFactory(), "master-detail")) {
            Component viewContainer = routeFactoryRegistry.getFactory(configForRoute).renderRoute(0, path.split("/")[0], configForRoute, entityManagerService);
            add(viewContainer);
        } else {
            DetailFactory detailFactory = configForRoute.getFactoryConfiguration().getDetailFactory();
            GenericEntity recordById = entityManagerService.getRecordById(configForRoute.getTable(), path.split("/")[1]);
            add(detailFactoryRegistry.getFactory(detailFactory).renderDetail(configForRoute, recordById, false));
        }
    }
}