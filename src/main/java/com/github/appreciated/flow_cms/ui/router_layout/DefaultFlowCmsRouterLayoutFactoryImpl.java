package com.github.appreciated.flow_cms.ui.router_layout;

import com.github.appreciated.flow_cms.service.FlowCmsConfigService;
import com.github.appreciated.flow_cms.ui.icon.FlowCmsIconRenderer;
import com.github.appreciated.flow_cms.ui.router_layout.components.DefaultRouterLayout;
import com.vaadin.flow.component.applayout.AppLayout;
import org.springframework.stereotype.Service;

/**
 * Default implementation of the FlowCmsRouterLayoutFactory interface.
 * This factory creates application layouts using the configuration provided by FlowCmsConfigService.
 */

@Service
public class DefaultFlowCmsRouterLayoutFactoryImpl implements FlowCmsRouterLayoutFactory {

    private final FlowCmsConfigService flowCmsConfigService;
    private final FlowCmsIconRenderer flowCmsIconRenderer;

    public DefaultFlowCmsRouterLayoutFactoryImpl(FlowCmsConfigService flowCmsConfigService, FlowCmsIconRenderer flowCmsIconRenderer) {
        this.flowCmsConfigService = flowCmsConfigService;
        this.flowCmsIconRenderer = flowCmsIconRenderer;
    }

    @Override
    public AppLayout createAppLayout() {
        return new DefaultRouterLayout(flowCmsConfigService, flowCmsIconRenderer);
    }
}