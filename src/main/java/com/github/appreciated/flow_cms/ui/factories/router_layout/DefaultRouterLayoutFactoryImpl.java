package com.github.appreciated.flow_cms.ui.factories.router_layout;

import com.github.appreciated.flow_cms.service.FlowCmsConfigService;
import com.github.appreciated.flow_cms.ui.factories.icon.FlowCmsIconFactory;
import com.vaadin.flow.component.applayout.AppLayout;
import org.springframework.stereotype.Service;

/**
 * Default implementation of the FlowCmsRouterLayoutFactory interface.
 * This factory creates application layouts using the configuration provided by FlowCmsConfigService.
 */

@Service
public class DefaultRouterLayoutFactoryImpl implements FlowCmsRouterLayoutFactory {

    private final FlowCmsConfigService configService;
    private final FlowCmsIconFactory iconFactory;

    public DefaultRouterLayoutFactoryImpl(FlowCmsConfigService configService, FlowCmsIconFactory iconFactory) {
        this.configService = configService;
        this.iconFactory = iconFactory;
    }

    @Override
    public AppLayout createAppLayout() {
        return new DefaultRouterLayout(configService, iconFactory);
    }
}