package com.github.appreciated.turbo_crud.core.config;

import com.github.appreciated.turbo_crud.core.config.model.Route;
import com.github.appreciated.turbo_crud.core.service.TurboCrudConfigService;
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
public class TurboCrudDefaultRouteRedirectConfiguration implements VaadinServiceInitListener {

    private final Map<String, Route> routeConfigs;
    private static Optional<Map.Entry<String, Route>> defaultRoute;

    public TurboCrudDefaultRouteRedirectConfiguration(TurboCrudConfigService configService) {
        this.routeConfigs = configService.getConfiguration().getRoutes();
    }

    @Override
    public void serviceInit(ServiceInitEvent event) {
        List<Map.Entry<String, Route>> defaultRoutes = routeConfigs
                .entrySet()
                .stream()
                .filter(configEntry -> configEntry.getValue().isDefaultRoute()).toList();
        if (defaultRoutes.size() > 1) {
            throw new IllegalStateException("More than one default route configured");
        } else {
            defaultRoute = defaultRoutes.stream().findFirst();
        }
        if (defaultRoute.isPresent()) {
            RouteConfiguration.forApplicationScope().setRoute("", TurboCrudDefaultRedirect.class);
        }
    }

    public static class TurboCrudDefaultRedirect extends Div implements BeforeEnterObserver {

        @Override
        public void beforeEnter(BeforeEnterEvent event) {
            event.forwardTo(defaultRoute.get().getKey());
        }
    }
}