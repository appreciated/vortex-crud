package com.github.appreciated.flow_cms.ui.app_layout;

import com.github.appreciated.flow_cms.service.FlowCmsConfigService;
import com.vaadin.flow.component.applayout.AppLayout;
import org.springframework.stereotype.Service;

@Service
public class DefaultFlowCmsRouterLayoutFactory implements FlowCmsRouterLayoutFactory {

    private final FlowCmsConfigService flowCmsConfigService;

    public DefaultFlowCmsRouterLayoutFactory(FlowCmsConfigService flowCmsConfigService) {
        this.flowCmsConfigService = flowCmsConfigService;
    }

    @Override
    public AppLayout createAppLayout() {
        return new AppLayoutNavbarPlacement(flowCmsConfigService);
    }
}