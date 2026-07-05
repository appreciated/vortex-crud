package com.github.appreciated.vortex_crud.core.config;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.CustomRoute;
import com.github.appreciated.vortex_crud.core.config.model.IdentityAndAccessManagement;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService;
import com.github.appreciated.vortex_crud.core.service.validation.ConfigurationConsistencyValidator;
import com.github.appreciated.vortex_crud.core.service.validation.ConfigurationFieldValidator;
import com.github.appreciated.vortex_crud.core.service.validation.ConfigurationI18nValidator;
import com.github.appreciated.vortex_crud.core.ui.routes.InternalDynamicRoute;
import com.github.appreciated.vortex_crud.core.ui.routes.ProxyRouterLayout;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import org.springframework.stereotype.Component;

@Component
public class DynamicRouteGenerator implements VaadinServiceInitListener {

    private final VortexCrudConfigService<?, ?, ?> configService;
    private final ConfigurationI18nValidator i18nValidator;
    private final ConfigurationFieldValidator fieldValidator;
    private final ConfigurationConsistencyValidator consistencyValidator;

    public DynamicRouteGenerator(VortexCrudConfigService<?, ?, ?> configService,
                                  ConfigurationI18nValidator i18nValidator,
                                  ConfigurationFieldValidator fieldValidator,
                                  ConfigurationConsistencyValidator consistencyValidator) {
        this.configService = configService;
        this.i18nValidator = i18nValidator;
        this.fieldValidator = fieldValidator;
        this.consistencyValidator = consistencyValidator;
    }

    @Override
    public void serviceInit(ServiceInitEvent event) {
        Application<?, ?, ?> configuration = configService.configuration();
        i18nValidator.validate(configuration);
        fieldValidator.validate(configuration);
        consistencyValidator.validate(configuration);
        processConfiguration(configuration);
    }

    private <ModelClass, FieldType, RepositoryType> void processConfiguration(Application<ModelClass, FieldType, RepositoryType> application) {
        var routes = application.routes();
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

        IdentityAndAccessManagement<ModelClass, FieldType, RepositoryType> userManagement = application.identityAndAccessManagement();
        if (userManagement != null) {
            configuration.setRoute("login", userManagement.loginView());
            configuration.setRoute("sign-up", userManagement.signUpView());
        }
    }

}
