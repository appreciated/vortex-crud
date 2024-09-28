package com.github.appreciated.flow_cms.ui.components;

import com.typesafe.config.Config;
import com.vaadin.flow.component.Component;

public interface FlowCmsComponentFactory {

    Component createComponent(Config type);

}