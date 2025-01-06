package com.github.appreciated.vortex_crud.core.ui.routes;

import com.github.appreciated.vortex_crud.core.config.VortexCrudPathToRouteResolver;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService;
import com.github.appreciated.vortex_crud.core.ui.factories.route.DefaultRouteFactoryRegistry;
import com.github.appreciated.vortex_crud.core.ui.factories.route.DetailRouteSetting;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactoryRegistry;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;

/**
 * A dynamic route component that renders different views based on the route path.
 * It uses the {@link VortexCrudConfigService} to retrieve configuration details and the {@link DefaultRouteFactoryRegistry}
 * to generate the appropriate view container for the specified route.
 * Implements {@link BeforeEnterObserver} to handle navigation events and dynamically update the view.
 */

public class InternalDynamicRoute<DataStoreId, FieldId> extends Div implements BeforeEnterObserver {

    private final VortexCrudConfigService<DataStoreId, FieldId> configService;
    private final VortexCrudRouteFactoryRegistry<DataStoreId, FieldId> routeFactoryRegistry;

    public InternalDynamicRoute(VortexCrudConfigService<DataStoreId, FieldId> configService, VortexCrudRouteFactoryRegistry<DataStoreId, FieldId> routeFactoryRegistry) {
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
        VortexCrudPathToRouteResolver<DataStoreId, FieldId> pathRoutes = new VortexCrudPathToRouteResolver<>(routeFactoryRegistry, "%s%s".formatted(event.getLocation().getFirstSegment(), path), configService.getConfiguration().getRouteRenderers());
        RouteRenderer<DataStoreId, FieldId> currentRouteRenderer = pathRoutes.getCurrentRoute();
        Integer currentIndex = pathRoutes.getCurrentIndex();
        Component component = routeFactoryRegistry.getFactory(currentRouteRenderer.getFactory())
                .renderRoute(currentIndex, pathRoutes, new DetailRouteSetting(false, false, false));
        add(component);
    }
}