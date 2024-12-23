package com.github.appreciated.turbo_crud.core.ui.factories.router_layout;

import com.github.appreciated.turbo_crud.core.service.TurboCrudConfigService;
import com.vaadin.flow.component.applayout.AppLayout;
import org.springframework.stereotype.Service;

/**
 * Default implementation of the TurboCrudRouterLayoutFactory interface.
 * This factory creates application layouts using the configuration provided by TurboCrudConfigService.
 */

@Service
public class DefaultRouterLayoutFactory implements TurboCrudRouterLayoutFactory {

    private final TurboCrudConfigService configService;

    public DefaultRouterLayoutFactory(TurboCrudConfigService configService) {
        this.configService = configService;
    }

    @Override
    public AppLayout createAppLayout() {
        return new DefaultRouterLayout(configService);
    }
}