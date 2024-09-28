package com.github.appreciated.flow_cms.ui.app_layout;

import com.vaadin.flow.component.applayout.AppLayout;
import org.springframework.stereotype.Service;

@Service
public class DefaultFlowCmsAppLayoutFactory implements FlowCmsAppLayoutFactory {

    @Override
    public AppLayout createAppLayout() {
        return new AppLayoutNavbarPlacement();
    }
}