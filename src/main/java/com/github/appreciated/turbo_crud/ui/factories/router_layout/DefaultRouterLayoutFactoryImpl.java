package com.github.appreciated.turbo_crud.ui.factories.router_layout;

import com.github.appreciated.turbo_crud.service.TurboCrudConfigService;
import com.github.appreciated.turbo_crud.ui.factories.icon.TurboCrudIconFactory;
import com.vaadin.flow.component.applayout.AppLayout;
import org.springframework.stereotype.Service;

/**
 * Default implementation of the TurboCrudRouterLayoutFactory interface.
 * This factory creates application layouts using the configuration provided by TurboCrudConfigService.
 */

@Service
public class DefaultRouterLayoutFactoryImpl implements TurboCrudRouterLayoutFactory {

    private final TurboCrudConfigService configService;
    private final TurboCrudIconFactory iconFactory;

    public DefaultRouterLayoutFactoryImpl(TurboCrudConfigService configService, TurboCrudIconFactory iconFactory) {
        this.configService = configService;
        this.iconFactory = iconFactory;
    }

    @Override
    public AppLayout createAppLayout() {
        return new DefaultRouterLayout(configService, iconFactory);
    }
}