package com.github.appreciated.flow_cms.ui.route_renderer;

import com.typesafe.config.ConfigObject;
import com.vaadin.flow.component.Component;

interface FlowCmsRouteRendererFactory {
    Component createViewContainer(ConfigObject config);
}
