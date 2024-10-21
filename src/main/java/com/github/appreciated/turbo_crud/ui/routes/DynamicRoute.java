package com.github.appreciated.turbo_crud.ui.routes;

import com.github.appreciated.turbo_crud.config.TurboCrudPathToRouteResolver;
import com.github.appreciated.turbo_crud.config.model.Route;
import com.github.appreciated.turbo_crud.service.TurboCrudConfigService;
import com.github.appreciated.turbo_crud.service.TurboCrudEntityManagerService;
import com.github.appreciated.turbo_crud.ui.factories.route.DefaultRouteFactoryRegistryImpl;
import com.github.appreciated.turbo_crud.ui.factories.route.TurboCrudRouteFactoryRegistry;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;

/**
 * A dynamic route component that renders different views based on the route path.
 * It uses the {@link TurboCrudConfigService} to retrieve configuration details and the {@link DefaultRouteFactoryRegistryImpl}
 * to generate the appropriate view container for the specified route.
 * Implements {@link BeforeEnterObserver} to handle navigation events and dynamically update the view.
 */

@com.vaadin.flow.router.Route(value = "view/:path*", layout = ProxyRouterLayout.class)
public class DynamicRoute extends Div implements BeforeEnterObserver {

    private final TurboCrudConfigService configService;
    private final TurboCrudRouteFactoryRegistry routeFactoryRegistry;

    public DynamicRoute(TurboCrudConfigService configService, TurboCrudRouteFactoryRegistry routeFactoryRegistry, TurboCrudEntityManagerService entityManagerService) {
        this.configService = configService;
        this.routeFactoryRegistry = routeFactoryRegistry;
        setSizeFull();
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        String path = event.getRouteParameters().get("path").orElse("");
        removeAll();
        TurboCrudPathToRouteResolver pathRoutes = new TurboCrudPathToRouteResolver(routeFactoryRegistry, path, configService.getConfiguration().getRoutesConfig());
        Route currentRoute = pathRoutes.getCurrentRoute();
        Integer currentIndex = pathRoutes.getCurrentIndex();
        Component component = routeFactoryRegistry.getFactory(currentRoute.getFactory())
                .renderRoute(currentIndex, pathRoutes, false, false);
        add(component);
    }
}