package com.github.appreciated.vortex_crud.core.ui.routes;

import com.github.appreciated.vortex_crud.core.config.DetailRouteSetting;
import com.github.appreciated.vortex_crud.core.config.VortexCrudDefaultRouteRedirectConfiguration;
import com.github.appreciated.vortex_crud.core.config.VortexCrudPathToRouteResolver;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.context.VortexCrudContext;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;

import java.util.Objects;

/**
 * A dynamic route component that renders different views based on the route path.
 * It uses the {@link VortexCrudContext} to retrieve configuration details and the factory instance
 * to generate the appropriate view container for the specified route.
 * Implements {@link BeforeEnterObserver} to handle navigation events and dynamically update the view.
 * <p>
 * This class serves as the entry point for all dynamic routes in the application.
 * </p>
 *
 * @param <ModelClass>     The type of the model class.
 * @param <FieldType>      The type of the field identifier.
 * @param <RepositoryType> The type of the repository.
 */
public class InternalDynamicRoute<ModelClass, FieldType, RepositoryType> extends Div implements BeforeEnterObserver {

    private final VortexCrudContext<ModelClass, FieldType, RepositoryType> context;
    private final VortexCrudDefaultRouteRedirectConfiguration<ModelClass, FieldType, RepositoryType> configuration;

    /**
     * Constructs a new {@code InternalDynamicRoute}.
     *
     * @param context The context containing services and configuration.
     */
    public InternalDynamicRoute(VortexCrudContext<ModelClass, FieldType, RepositoryType> context, VortexCrudDefaultRouteRedirectConfiguration<ModelClass, FieldType, RepositoryType> configuration) {
        this.context = context;
        this.configuration = configuration;
        setSizeFull();
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (Objects.equals(event.getLocation().getPath(), "")) {
            String target = configuration.getDefaultRouteEntry().getKey();
            if (target != null) {
                event.forwardTo(target);
                return;
            }
        }

        String path = event.getRouteParameters().get("path").orElse("");
        // Ensure path starts with / if it's not empty, to match route resolution expectations
        if (!path.isEmpty() && !path.startsWith("/")) {
            path = "/" + path;
        }
        removeAll();
        VortexCrudPathToRouteResolver<ModelClass, FieldType, RepositoryType> pathRoutes = new VortexCrudPathToRouteResolver<>(
                "%s%s".formatted(event.getLocation().getFirstSegment(), path),
                context.configService().configuration().routes(),
                context.dataStoreUtil()
        );
        RouteRenderer<ModelClass, FieldType, RepositoryType> currentRouteRenderer = pathRoutes.getCurrentRoute();
        Integer currentIndex = pathRoutes.determineActiveRouteIndex();
        Component component = currentRouteRenderer.factory()
                .renderRoute(context, currentIndex, pathRoutes, new DetailRouteSetting(false, false, false));
        add(component);
    }
}
