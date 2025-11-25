package com.github.appreciated.vortex_crud.core.config;

import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinServiceInitListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Configuration class for handling default route redirects in a Vaadin application.
 * This class implements {@link VaadinServiceInitListener} to register a default route dynamically based on the application configuration.
 * It ensures that only one default route is configured and redirects to it if applicable.
 */

@Component
public class VortexCrudDefaultRouteRedirectConfiguration<ModelClass, FieldType, RepositoryType> implements VaadinServiceInitListener {

    private final Map<String, RouteRenderer<ModelClass, FieldType, RepositoryType>> routeConfigs;
    private static final Map<VaadinService, String> defaultRoutes = new ConcurrentHashMap<>();

    public VortexCrudDefaultRouteRedirectConfiguration(VortexCrudConfigService<ModelClass, FieldType, RepositoryType> configService) {
        this.routeConfigs = configService.configuration().routes();
    }

    @Override
    public void serviceInit(ServiceInitEvent event) {
        List<? extends Map.Entry<String, RouteRenderer<ModelClass, FieldType, RepositoryType>>> configuredDefaultRoutes = routeConfigs
                .entrySet()
                .stream()
                .filter(configEntry -> configEntry.getValue().isDefaultRoute()).toList();

        Optional<? extends Map.Entry<String, RouteRenderer<ModelClass, FieldType, RepositoryType>>> defaultRouteEntry;

        if (configuredDefaultRoutes.size() > 1) {
            throw new IllegalStateException("More than one default route configured");
        } else {
            defaultRouteEntry = configuredDefaultRoutes.stream().findFirst();
        }
        if (defaultRouteEntry.isPresent()) {
            defaultRoutes.put(event.getSource(), defaultRouteEntry.get().getKey());
            event.getSource().addServiceDestroyListener(e -> defaultRoutes.remove(e.getSource()));
            RouteConfiguration.forApplicationScope().setRoute("", VortexCrudDefaultRedirect.class);
        }
    }

    public static class VortexCrudDefaultRedirect extends Div implements BeforeEnterObserver {

        @Override
        public void beforeEnter(BeforeEnterEvent event) {
            String target = defaultRoutes.get(VaadinService.getCurrent());
            if (target != null) {
                event.forwardTo(target);
            }
        }
    }
}
