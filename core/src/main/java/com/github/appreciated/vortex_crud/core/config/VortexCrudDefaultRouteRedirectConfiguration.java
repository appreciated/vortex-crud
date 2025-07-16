package com.github.appreciated.vortex_crud.core.config;

import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Configuration class for handling default route redirects in a Vaadin application.
 * This class implements {@link VaadinServiceInitListener} to register a default route dynamically based on the application configuration.
 * It ensures that only one default route is configured and redirects to it if applicable.
 */

@Component
public class VortexCrudDefaultRouteRedirectConfiguration<DataStoreId, FieldId, KeyType> implements VaadinServiceInitListener {

    private final Map<String, RouteRenderer<DataStoreId, FieldId, KeyType>> routeConfigs;
    private static Optional<? extends Map.Entry<String, RouteRenderer<?, ?, ?>>> defaultRoute;

    public VortexCrudDefaultRouteRedirectConfiguration(VortexCrudConfigService<DataStoreId, FieldId, KeyType> configService) {
        this.routeConfigs = configService.getConfiguration().getRouteRenderers();
    }

    @Override
    public void serviceInit(ServiceInitEvent event) {
        List<? extends Map.Entry<String, RouteRenderer<DataStoreId, FieldId, KeyType>>> defaultRoutes = routeConfigs
                .entrySet()
                .stream()
                .filter(configEntry -> configEntry.getValue().isDefaultRoute()).toList();
        if (defaultRoutes.size() > 1) {
            throw new IllegalStateException("More than one default route configured");
        } else {
            defaultRoute = (Optional<? extends Map.Entry<String, RouteRenderer<?, ?, ?>>>) (Optional<?>) defaultRoutes.stream().findFirst();

        }
        if (defaultRoute.isPresent()) {
            RouteConfiguration.forApplicationScope().setRoute("", VortexCrudDefaultRedirect.class);
        }
    }

    public static class VortexCrudDefaultRedirect extends Div implements BeforeEnterObserver {

        @Override
        public void beforeEnter(BeforeEnterEvent event) {
            event.forwardTo(defaultRoute.get().getKey());
        }
    }
}