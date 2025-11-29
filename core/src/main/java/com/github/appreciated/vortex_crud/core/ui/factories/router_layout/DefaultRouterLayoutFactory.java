package com.github.appreciated.vortex_crud.core.ui.factories.router_layout;

import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import com.github.appreciated.vortex_crud.core.security.VortexCrudLogoutService;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService;
import com.vaadin.flow.component.applayout.AppLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultRouterLayoutFactory<ModelClass, FieldType, RepositoryType> implements VortexCrudRouterLayoutFactory {

    private final VortexCrudConfigService<ModelClass, FieldType, RepositoryType> configService;
    private final VortexCrudLogoutService logoutService;
    private final ReflectionService<FieldType> reflectionService;

    public DefaultRouterLayoutFactory(
            VortexCrudConfigService<ModelClass, FieldType, RepositoryType> configService,
            @Autowired(required = false) VortexCrudLogoutService logoutService,
            ReflectionService<FieldType> reflectionService) {
        this.configService = configService;
        this.logoutService = logoutService;
        this.reflectionService = reflectionService;
    }

    @Override
    public AppLayout createAppLayout() {
        return new DefaultRouterLayout<>(configService, logoutService, reflectionService);
    }
}