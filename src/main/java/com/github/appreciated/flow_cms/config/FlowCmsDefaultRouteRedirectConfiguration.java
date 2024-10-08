package com.github.appreciated.flow_cms.config;

import com.github.appreciated.flow_cms.config.model.RouteConfig;
import com.github.appreciated.flow_cms.service.FlowCmsConfigService;
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
public class FlowCmsDefaultRouteRedirectConfiguration implements VaadinServiceInitListener {

    private final Map<String, RouteConfig> routeConfigs;
    private static Optional<Map.Entry<String, RouteConfig>> defaultRoute;

    public FlowCmsDefaultRouteRedirectConfiguration(FlowCmsConfigService flowCmsConfigService) {
        this.routeConfigs = flowCmsConfigService.getConfiguration().getRoutesConfig();
    }

    @Override
    public void serviceInit(ServiceInitEvent event) {
        List<Map.Entry<String, RouteConfig>> defaultRoutes = routeConfigs
                .entrySet()
                .stream()
                .filter(configEntry -> configEntry.getValue().isDefault()).toList();
        if (defaultRoutes.size() > 1) {
            throw new IllegalStateException("More than one default route configured");
        } else {
            defaultRoute = defaultRoutes.stream().findFirst();
        }
        if (defaultRoute.isPresent()) {
            RouteConfiguration.forApplicationScope().setRoute("", FlowCmsDefaultRedirect.class);
        }
    }

    public static class FlowCmsDefaultRedirect extends Div implements BeforeEnterObserver {

        @Override
        public void beforeEnter(BeforeEnterEvent event) {
            event.forwardTo("/view/" + defaultRoute.get().getKey());
        }
    }
}