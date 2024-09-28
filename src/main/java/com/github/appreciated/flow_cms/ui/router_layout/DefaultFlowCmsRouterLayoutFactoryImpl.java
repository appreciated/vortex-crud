package com.github.appreciated.flow_cms.ui.router_layout;

import com.github.appreciated.flow_cms.service.FlowCmsConfigService;
import com.vaadin.flow.component.applayout.AppLayout;
import org.springframework.stereotype.Service;

@Service
public class DefaultFlowCmsRouterLayoutFactoryImpl implements FlowCmsRouterLayoutFactory {

    private final FlowCmsConfigService flowCmsConfigService;

    public DefaultFlowCmsRouterLayoutFactoryImpl(FlowCmsConfigService flowCmsConfigService) {
        this.flowCmsConfigService = flowCmsConfigService;
    }

    @Override
    public AppLayout createAppLayout() {
        return new DefaultRouterLayout(flowCmsConfigService);
    }
}