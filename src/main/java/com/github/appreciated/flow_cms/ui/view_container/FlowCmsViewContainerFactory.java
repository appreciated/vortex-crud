package com.github.appreciated.flow_cms.ui.view_container;

import com.typesafe.config.ConfigObject;
import com.vaadin.flow.component.Component;

interface FlowCmsViewContainerFactory {
    Component createViewContainer(ConfigObject config);
}
