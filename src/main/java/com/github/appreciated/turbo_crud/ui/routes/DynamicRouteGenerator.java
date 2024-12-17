package com.github.appreciated.turbo_crud.ui.routes;

import com.github.appreciated.turbo_crud.service.TurboCrudConfigService;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DynamicRouteGenerator implements VaadinServiceInitListener {

    private final TurboCrudConfigService turboCrudConfigService;

    public DynamicRouteGenerator(TurboCrudConfigService turboCrudConfigService) {
        this.turboCrudConfigService = turboCrudConfigService;
    }

    @Override
    public void serviceInit(ServiceInitEvent event) {
        Set<String> keys = turboCrudConfigService.getConfiguration().getRoutes().keySet();
        keys.forEach(this::registerRoute);
    }

    public void registerRoute(String path) {
        RouteConfiguration configuration = RouteConfiguration.forApplicationScope();
        configuration.setRoute(path+"/:path*", DynamicRoute.class, ProxyRouterLayout.class);
        RouteConfiguration configuration2 = RouteConfiguration.forApplicationScope();
        configuration2.setRoute(path, DynamicRoute.class, ProxyRouterLayout.class);
    }

}