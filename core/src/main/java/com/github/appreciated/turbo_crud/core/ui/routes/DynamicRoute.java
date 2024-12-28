package com.github.appreciated.turbo_crud.core.ui.routes;

import com.github.appreciated.turbo_crud.core.config.TurboCrudPathToRouteResolver;
import com.github.appreciated.turbo_crud.core.config.model.Route;
import com.github.appreciated.turbo_crud.core.service.TurboCrudConfigService;
import com.github.appreciated.turbo_crud.core.ui.factories.route.DefaultRouteFactoryRegistry;
import com.github.appreciated.turbo_crud.core.ui.factories.route.DetailRouteSetting;
import com.github.appreciated.turbo_crud.core.ui.factories.route.TurboCrudRouteFactoryRegistry;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;

/**
 * A dynamic route component that renders different views based on the route path.
 * It uses the {@link TurboCrudConfigService} to retrieve configuration details and the {@link DefaultRouteFactoryRegistry}
 * to generate the appropriate view container for the specified route.
 * Implements {@link BeforeEnterObserver} to handle navigation events and dynamically update the view.
 */

public class DynamicRoute<DataStoreId, FieldId> extends Div implements BeforeEnterObserver {

    private final TurboCrudConfigService<DataStoreId, FieldId> configService;
    private final TurboCrudRouteFactoryRegistry<DataStoreId, FieldId> routeFactoryRegistry;

    public DynamicRoute(TurboCrudConfigService<DataStoreId, FieldId> configService, TurboCrudRouteFactoryRegistry<DataStoreId, FieldId> routeFactoryRegistry) {
        this.configService = configService;
        this.routeFactoryRegistry = routeFactoryRegistry;
        setSizeFull();
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        String path = event.getRouteParameters().get("path").orElse("");
        if (!path.isEmpty()) {
            path = "/" + path;
        }
        removeAll();
        TurboCrudPathToRouteResolver<DataStoreId, FieldId> pathRoutes = new TurboCrudPathToRouteResolver<>(routeFactoryRegistry, "%s%s".formatted(event.getLocation().getFirstSegment(), path), configService.getConfiguration().getRoutes());
        Route<DataStoreId, FieldId> currentRoute = pathRoutes.getCurrentRoute();
        Integer currentIndex = pathRoutes.getCurrentIndex();
        Component component = routeFactoryRegistry.getFactory(currentRoute.getFactory())
                .renderRoute(currentIndex, pathRoutes, new DetailRouteSetting(false, false, false));
        add(component);
    }
}