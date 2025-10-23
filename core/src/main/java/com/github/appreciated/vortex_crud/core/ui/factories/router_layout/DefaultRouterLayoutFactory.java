package com.github.appreciated.vortex_crud.core.ui.factories.router_layout;

import com.github.appreciated.vortex_crud.core.security.SecurityService;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService;
import com.vaadin.flow.component.applayout.AppLayout;
import org.springframework.stereotype.Service;

@Service
public class DefaultRouterLayoutFactory<DataStoreId, FieldId, KeyType> implements VortexCrudRouterLayoutFactory {

    private final VortexCrudConfigService<DataStoreId, FieldId, KeyType> configService;
    private final SecurityService securityService;

    public DefaultRouterLayoutFactory(VortexCrudConfigService<DataStoreId, FieldId, KeyType> configService, SecurityService securityService) {
        this.configService = configService;
        this.securityService = securityService;
    }

    @Override
    public AppLayout createAppLayout() {
        return new DefaultRouterLayout<>(configService, securityService);
    }
}