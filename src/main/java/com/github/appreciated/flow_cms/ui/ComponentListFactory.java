package com.github.appreciated.flow_cms.ui;

import com.vaadin.flow.component.Component;

public interface ComponentListFactory {

    Component createComponent(String type);

}