package com.github.appreciated.turbo_crud.core.ui.routes;

import com.github.appreciated.turbo_crud.core.config.TurboCrudPathToRouteResolver;
import com.github.appreciated.turbo_crud.core.config.model.Route;
import com.github.appreciated.turbo_crud.core.ui.factories.route.DetailRouteSetting;
import com.github.appreciated.turbo_crud.core.ui.factories.route.TurboCrudRouteFactoryRegistry;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.RouteConfiguration;

import java.util.Map;

/**
 * An abstract dynamic route component that renders different views based on the route path.
 * Instead of fetching the configuration from a service, subclasses define the route configuration
 * through an abstract method {@link #getConfiguration()}.
 * Implements {@link BeforeEnterObserver} to handle navigation events and dynamically update the view.
 */

public abstract class TurboCrudRoute<DataStoreId, FieldId> extends Div implements BeforeEnterObserver {

    private final TurboCrudRouteFactoryRegistry<DataStoreId, FieldId> routeFactoryRegistry;

    public TurboCrudRoute(TurboCrudRouteFactoryRegistry<DataStoreId, FieldId> routeFactoryRegistry) {
        this.routeFactoryRegistry = routeFactoryRegistry;
        setSizeFull();
    }

    /**
     * Subclasses must implement this method to provide the route configuration.
     *
     * @return a map of route configurations.
     */
    protected abstract Route<DataStoreId, FieldId> getConfiguration();

    protected String getUrl() {
        return RouteConfiguration.forSessionScope().getUrl(getClass());
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        String path = event.getRouteParameters().get("path").orElse("");
        if (!path.isEmpty()) {
            path = "/" + path;
        }
        removeAll();

        String routePattern = getUrl();
        if (routePattern.contains("/")){
            throw new IllegalArgumentException("The routePattern must not contain a '/'");
        }
        TurboCrudPathToRouteResolver<DataStoreId, FieldId> pathRoutes = new TurboCrudPathToRouteResolver<>(
                routeFactoryRegistry,
                "%s%s".formatted(event.getLocation().getFirstSegment(), path),
                Map.of(routePattern,getConfiguration())
        );

        Route<DataStoreId, FieldId> currentRoute = pathRoutes.getCurrentRoute();
        Integer currentIndex = pathRoutes.getCurrentIndex();

        Component component = routeFactoryRegistry.getFactory(currentRoute.getFactory())
                .renderRoute(currentIndex, pathRoutes, new DetailRouteSetting(false, false, false));
        add(component);
    }
}
