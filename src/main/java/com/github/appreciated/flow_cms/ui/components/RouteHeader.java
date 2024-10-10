package com.github.appreciated.flow_cms.ui.components;

import com.github.appreciated.flow_cms.config.model.RouteConfig;
import com.github.appreciated.flow_cms.ui.factories.icon.FlowCmsIcon;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class RouteHeader extends HorizontalLayout {
    public RouteHeader(RouteConfig config, FlowCmsIcon flowCmsIcon) {
        H2 h2 = new H2(getTranslation(config.getTitle()));

        if (config.getIcon() != null) {
            add(flowCmsIcon.renderIcon(config.getIcon()));
        }
        add(h2);
        setAlignItems(Alignment.CENTER);
    }
}
