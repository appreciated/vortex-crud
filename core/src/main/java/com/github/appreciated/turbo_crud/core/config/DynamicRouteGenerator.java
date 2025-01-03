package com.github.appreciated.turbo_crud.core.config;

import com.github.appreciated.turbo_crud.core.service.TurboCrudConfigService;
import com.github.appreciated.turbo_crud.core.ui.routes.InternalDynamicRoute;
import com.github.appreciated.turbo_crud.core.ui.routes.ProxyRouterLayout;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DynamicRouteGenerator implements VaadinServiceInitListener {

    private final TurboCrudConfigService<?,?> turboCrudConfigService;

    public DynamicRouteGenerator(TurboCrudConfigService<?,?> turboCrudConfigService) {
        this.turboCrudConfigService = turboCrudConfigService;
    }

    @Override
    public void serviceInit(ServiceInitEvent event) {
        Set<String> keys = turboCrudConfigService.getConfiguration().getRouteRenderers().keySet();
        keys.forEach(this::registerRoute);
    }

    public void registerRoute(String path) {
        RouteConfiguration configuration = RouteConfiguration.forApplicationScope();
        configuration.setRoute(path+"/:path?", InternalDynamicRoute.class, ProxyRouterLayout.class);
    }

}