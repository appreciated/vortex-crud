package com.github.appreciated.flow_cms.ui.factories.icon;

import com.vaadin.flow.component.Component;

/**
 * An interface for rendering icons based on a passed icon identifier.
 */
public interface TurboCrudIconFactory {
    Component renderIcon(String iconEnum);
}
