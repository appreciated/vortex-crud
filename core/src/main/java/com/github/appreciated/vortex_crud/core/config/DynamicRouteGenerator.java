package com.github.appreciated.vortex_crud.core.config;

import com.github.appreciated.vortex_crud.core.config.model.CustomRoute;
import com.github.appreciated.vortex_crud.core.config.model.IdentityAndAccessManagement;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService;
import com.github.appreciated.vortex_crud.core.ui.routes.InternalDynamicRoute;
import com.github.appreciated.vortex_crud.core.ui.routes.ProxyRouterLayout;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

@Component
public class DynamicRouteGenerator implements VaadinServiceInitListener {

    private final VortexCrudConfigService<?, ?, ?> configService;

    public DynamicRouteGenerator(VortexCrudConfigService<?, ?, ?> configService) {
        this.configService = configService;
    }

    @Override
    public void serviceInit(ServiceInitEvent event) {
        var routes = configService.configuration().routes();
        RouteConfiguration configuration = RouteConfiguration.forApplicationScope();

        routes.forEach((path, routeRenderer) -> {
            if (routeRenderer instanceof CustomRoute<?, ?, ?> customRoute) {
                // Register custom component with ProxyRouterLayout
                configuration.setRoute(path, customRoute.componentClass(), ProxyRouterLayout.class);
            } else {
                // Register normal VortexCrud dynamic route
                configuration.setRoute(path + "/:path*", InternalDynamicRoute.class, ProxyRouterLayout.class);
            }
        });

        IdentityAndAccessManagement<?, ?, ?> userManagement = configService.configuration().identityAndAccessManagement();
        if (userManagement != null) {
            configuration.setRoute("login", userManagement.loginView());
            configuration.setRoute("sign-up", userManagement.signUpView());
        }
    }

}