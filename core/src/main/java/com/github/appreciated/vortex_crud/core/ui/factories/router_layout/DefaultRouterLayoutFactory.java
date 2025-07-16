package com.github.appreciated.vortex_crud.core.ui.factories.router_layout;

import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService;
import com.vaadin.flow.component.applayout.AppLayout;
import org.springframework.stereotype.Service;

/**
 * Default implementation of the VortexCrudRouterLayoutFactory interface.
 * This factory creates application layouts using the configuration provided by VortexCrudConfigService.
 */

@Service
public class DefaultRouterLayoutFactory<DataStoreId, FieldId, KeyType> implements VortexCrudRouterLayoutFactory {

    private final VortexCrudConfigService<DataStoreId, FieldId, KeyType> configService;

    public DefaultRouterLayoutFactory(VortexCrudConfigService<DataStoreId, FieldId, KeyType> configService) {
        this.configService = configService;
    }

    @Override
    public AppLayout createAppLayout() {
        return new DefaultRouterLayout<>(configService);
    }
}