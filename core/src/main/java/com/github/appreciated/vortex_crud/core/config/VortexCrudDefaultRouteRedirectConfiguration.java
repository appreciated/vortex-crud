package com.github.appreciated.vortex_crud.core.config;

import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService;
import com.github.appreciated.vortex_crud.core.ui.routes.InternalDynamicRoute;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Configuration class for handling default route redirects in a Vaadin application.
 * This class implements {@link VaadinServiceInitListener} to register a default route dynamically based on the application configuration.
 * It ensures that only one default route is configured and redirects to it if applicable.
 */

@Component
public class VortexCrudDefaultRouteRedirectConfiguration<ModelClass, FieldType, RepositoryType> implements VaadinServiceInitListener {

    private final Map<String, RouteRenderer<ModelClass, FieldType, RepositoryType>> routeConfigs;

    @Getter
    Map.Entry<String, RouteRenderer<ModelClass, FieldType, RepositoryType>> defaultRouteEntry;

    public VortexCrudDefaultRouteRedirectConfiguration(VortexCrudConfigService<ModelClass, FieldType, RepositoryType> configService) {
        this.routeConfigs = configService.configuration().routes();
    }

    @Override
    public void serviceInit(ServiceInitEvent event) {
        List<? extends Map.Entry<String, RouteRenderer<ModelClass, FieldType, RepositoryType>>> configuredDefaultRoutes = routeConfigs
                .entrySet()
                .stream()
                .filter(configEntry -> configEntry.getValue().isDefaultRoute()).toList();


        if (configuredDefaultRoutes.size() > 1) {
            throw new IllegalStateException("More than one default route configured");
        } else {
            defaultRouteEntry = configuredDefaultRoutes.stream().findFirst().orElse(null);
        }
        if (defaultRouteEntry != null) {
            RouteConfiguration.forApplicationScope().setRoute("", InternalDynamicRoute.class);
        }
    }
}
