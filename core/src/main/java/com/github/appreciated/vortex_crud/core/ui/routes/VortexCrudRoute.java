package com.github.appreciated.vortex_crud.core.ui.routes;

import com.github.appreciated.vortex_crud.core.config.VortexCrudPathToRouteResolver;
import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.config.model.UserManagement;
import com.github.appreciated.vortex_crud.core.entity.User;
import com.github.appreciated.vortex_crud.core.entity.VortexCrudDataStoreUtilStrategy;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.core.ui.SecurityUtils;
import com.github.appreciated.vortex_crud.core.ui.factories.route.DetailRouteSetting;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactoryRegistry;
import com.github.appreciated.vortex_crud.core.ui.view.LoginView;
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

public abstract class VortexCrudRoute<DataStoreId, FieldId, KeyType, U extends User> extends Div implements BeforeEnterObserver {

    private final VortexCrudRouteFactoryRegistry<DataStoreId, FieldId, KeyType> routeFactoryRegistry;
    private final VortexCrudDataStoreUtilStrategy dataStoreUtil;
    private final VortexCrudConfigurationProvider<DataStoreId, FieldId, KeyType, U> configurationProvider;

    public VortexCrudRoute(
            VortexCrudRouteFactoryRegistry<DataStoreId, FieldId, KeyType> routeFactoryRegistry,
            VortexCrudDataStoreUtilStrategy dataStoreUtil,
            VortexCrudConfigurationProvider<DataStoreId, FieldId, KeyType, U> configurationProvider
    ) {
        this.routeFactoryRegistry = routeFactoryRegistry;
        this.dataStoreUtil = dataStoreUtil;
        this.configurationProvider = configurationProvider;
        setSizeFull();
    }

    /**
     * Subclasses must implement this method to provide the route configuration.
     *
     * @return a map of route configurations.
     */
    protected abstract RouteRenderer<DataStoreId, FieldId, KeyType> getConfiguration();

    protected String getUrl() {
        return RouteConfiguration.forSessionScope().getUrl(getClass());
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Application<DataStoreId, FieldId, KeyType, U> application = configurationProvider.get();
        UserManagement userManagement = application.getUserManagement();

        if (userManagement != null && userManagement.isEnabled() && !SecurityUtils.isUserLoggedIn()) {
            event.rerouteTo(LoginView.class);
        } else {
            String path = event.getRouteParameters().get("path").orElse("");
            if (!path.isEmpty()) {
                path = "/" + path;
            }
            removeAll();

            String routePattern = getUrl();
            if (routePattern.contains("/")) {
                throw new IllegalArgumentException("The routePattern must not contain a '/'");
            }
            VortexCrudPathToRouteResolver<DataStoreId, FieldId, KeyType> pathRoutes = new VortexCrudPathToRouteResolver<>(
                    routeFactoryRegistry,
                    "%s%s".formatted(event.getLocation().getFirstSegment(), path),
                    Map.of(routePattern, getConfiguration()),
                    dataStoreUtil
            );

            RouteRenderer<DataStoreId, FieldId, KeyType> currentRouteRenderer = pathRoutes.getCurrentRoute();
            Integer currentIndex = pathRoutes.determineActiveRouteIndex();

            Component component = routeFactoryRegistry.getFactory(currentRouteRenderer.getFactory())
                    .renderRoute(currentIndex, pathRoutes, new DetailRouteSetting(false, false, false));
            add(component);
        }
    }
}
