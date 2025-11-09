package com.github.appreciated.vortex_crud.core.config;

import com.github.appreciated.vortex_crud.core.config.model.IdentityAndAccessManagement;
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

    private final VortexCrudConfigService<?, ?, ?> configService;

    public DynamicRouteGenerator(VortexCrudConfigService<?, ?, ?> configService) {
        this.configService = configService;
    }

    @Override
    public void serviceInit(ServiceInitEvent event) {
        Set<String> keys = configService.configuration().routes().keySet();
        keys.forEach(this::registerRoute);

        IdentityAndAccessManagement<?, ?, ?> userManagement = configService.configuration().identityAndAccessManagement();
        if (userManagement != null) {
            RouteConfiguration configuration = RouteConfiguration.forApplicationScope();
            configuration.setRoute("login", userManagement.loginView());
            configuration.setRoute("sign-up", userManagement.signUpView());
        }
    }

    public void registerRoute(String path) {
        RouteConfiguration configuration = RouteConfiguration.forApplicationScope();
        configuration.setRoute(path + "/:path*", InternalDynamicRoute.class, ProxyRouterLayout.class);
    }

}