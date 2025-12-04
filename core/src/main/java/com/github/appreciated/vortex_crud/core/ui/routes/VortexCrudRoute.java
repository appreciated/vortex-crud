package com.github.appreciated.vortex_crud.core.ui.routes;

import com.github.appreciated.vortex_crud.core.config.VortexCrudPathToRouteResolver;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.context.VortexCrudContext;
import com.github.appreciated.vortex_crud.core.config.DetailRouteSetting;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.RouteConfiguration;

import java.util.Map;

/**
 * An abstract dynamic route component that renders different views based on the route path.
 * Instead of fetching the configuration from a service, subclasses define the route configuration
 * through an abstract method {@link #configuration()}.
 * Implements {@link BeforeEnterObserver} to handle navigation events and dynamically update the view.
 */

public abstract class VortexCrudRoute<ModelClass, FieldType, RepositoryType> extends Div implements BeforeEnterObserver {

    private final VortexCrudContext<ModelClass, FieldType, RepositoryType> context;

    public VortexCrudRoute(VortexCrudContext<ModelClass, FieldType, RepositoryType> context) {
        this.context = context;
        setSizeFull();
    }

    /**
     * Subclasses must implement this method to provide the route configuration.
     *
     * @return a map of route configurations.
     */
    protected abstract RouteRenderer<ModelClass, FieldType, RepositoryType> configuration();

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
        if (routePattern.contains("/")) {
            throw new IllegalArgumentException("The routePattern must not contain a '/'");
        }
        VortexCrudPathToRouteResolver<ModelClass, FieldType, RepositoryType> pathRoutes = new VortexCrudPathToRouteResolver<>(
                "%s%s".formatted(event.getLocation().getFirstSegment(), path),
                Map.of(routePattern, configuration()),
                context.dataStoreUtil()
        );

        RouteRenderer<ModelClass, FieldType, RepositoryType> currentRouteRenderer = pathRoutes.getCurrentRoute();
        Integer currentIndex = pathRoutes.determineActiveRouteIndex();

        Component component = currentRouteRenderer.factory()
                .renderRoute(context, currentIndex, pathRoutes, new DetailRouteSetting(false, false, false));
        add(component);
    }
}
