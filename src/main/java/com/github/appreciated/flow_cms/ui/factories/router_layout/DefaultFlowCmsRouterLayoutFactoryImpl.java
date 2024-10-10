package com.github.appreciated.flow_cms.ui.factories.router_layout;

import com.github.appreciated.flow_cms.service.FlowCmsConfigService;
import com.github.appreciated.flow_cms.ui.factories.icon.FlowCmsIcon;
import com.vaadin.flow.component.applayout.AppLayout;
import org.springframework.stereotype.Service;

/**
 * Default implementation of the FlowCmsRouterLayoutFactory interface.
 * This factory creates application layouts using the configuration provided by FlowCmsConfigService.
 */

@Service
public class DefaultFlowCmsRouterLayoutFactoryImpl implements FlowCmsRouterLayoutFactory {

    private final FlowCmsConfigService flowCmsConfigService;
    private final FlowCmsIcon flowCmsIcon;

    public DefaultFlowCmsRouterLayoutFactoryImpl(FlowCmsConfigService flowCmsConfigService, FlowCmsIcon flowCmsIcon) {
        this.flowCmsConfigService = flowCmsConfigService;
        this.flowCmsIcon = flowCmsIcon;
    }

    @Override
    public AppLayout createAppLayout() {
        return new DefaultRouterLayout(flowCmsConfigService, flowCmsIcon);
    }
}