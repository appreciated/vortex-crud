package com.github.appreciated.vortex_crud.core.ui.routes;

import com.github.appreciated.vortex_crud.core.config.VortexCrudPathToRouteResolver;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.service.VortexCrudContext;
import com.github.appreciated.vortex_crud.core.ui.factories.route.DetailRouteSetting;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;

/**
 * A dynamic route component that renders different views based on the route path.
 * It uses the {@link VortexCrudConfigService} to retrieve configuration details and the {@link VortexCrudRouteFactoryRegistry}
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

    /**
     * Constructs a new {@code InternalDynamicRoute}.
     *
     * @param context        The context to access application settings.
     */
    public InternalDynamicRoute(VortexCrudContext<ModelClass, FieldType, RepositoryType> context
    ) {
        this.context = context;
        setSizeFull();
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
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
        Component component = currentRouteRenderer.factoryInstance()
                .renderRoute(currentIndex, pathRoutes, new DetailRouteSetting(false, false, false), context);
        add(component);
    }
}
