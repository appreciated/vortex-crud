package com.github.appreciated.vortex_crud.core.ui.routes;

import com.github.appreciated.vortex_crud.core.config.VortexCrudPathToRouteResolver;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.entity.VortexCrudDataStoreUtilStrategy;
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

public class InternalDynamicRoute<DataStoreId, FieldId, KeyType> extends Div implements BeforeEnterObserver {

    private final VortexCrudConfigService<DataStoreId, FieldId, KeyType> configService;
    private final VortexCrudRouteFactoryRegistry<DataStoreId, FieldId, KeyType> routeFactoryRegistry;
    private final VortexCrudDataStoreUtilStrategy dataStoreUtil;

    public InternalDynamicRoute(VortexCrudConfigService<DataStoreId, FieldId, KeyType> configService,
                                VortexCrudRouteFactoryRegistry<DataStoreId, FieldId, KeyType> routeFactoryRegistry,
                                VortexCrudDataStoreUtilStrategy dataStoreUtil
    ) {
        this.configService = configService;
        this.routeFactoryRegistry = routeFactoryRegistry;
        this.dataStoreUtil = dataStoreUtil;
        setSizeFull();
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        String path = event.getRouteParameters().get("path").orElse("");
        if (!path.isEmpty()) {
            path = "/" + path;
        }
        removeAll();
        VortexCrudPathToRouteResolver<DataStoreId, FieldId, KeyType> pathRoutes = new VortexCrudPathToRouteResolver<>(
                routeFactoryRegistry,
                "%s%s".formatted(event.getLocation().getFirstSegment(), path),
                configService.getConfiguration().getRouteRenderers(),
                dataStoreUtil
        );
        RouteRenderer<DataStoreId, FieldId, KeyType> currentRouteRenderer = pathRoutes.getCurrentRoute();
        Integer currentIndex = pathRoutes.determineActiveRouteIndex();
        Component component = routeFactoryRegistry.getFactory(currentRouteRenderer.getFactory())
                .renderRoute(currentIndex, pathRoutes, new DetailRouteSetting(false, false, false));
        add(component);
    }
}