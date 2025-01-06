package com.github.appreciated.vortex_crud.core.config;

import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService;
import com.github.appreciated.vortex_crud.core.ui.routes.InternalDynamicRoute;
import com.github.appreciated.vortex_crud.core.ui.routes.ProxyRouterLayout;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DynamicRouteGenerator implements VaadinServiceInitListener {

    private final VortexCrudConfigService<?,?> vortexCrudConfigService;

    public DynamicRouteGenerator(VortexCrudConfigService<?,?> vortexCrudConfigService) {
        this.vortexCrudConfigService = vortexCrudConfigService;
    }

    @Override
    public void serviceInit(ServiceInitEvent event) {
        Set<String> keys = vortexCrudConfigService.getConfiguration().getRouteRenderers().keySet();
        keys.forEach(this::registerRoute);
    }

    public void registerRoute(String path) {
        RouteConfiguration configuration = RouteConfiguration.forApplicationScope();
        configuration.setRoute(path+"/:path?", InternalDynamicRoute.class, ProxyRouterLayout.class);
    }

}