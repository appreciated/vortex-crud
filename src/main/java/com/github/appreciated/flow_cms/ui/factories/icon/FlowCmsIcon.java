package com.github.appreciated.flow_cms.ui.factories.icon;

import com.vaadin.flow.component.Component;

/**
 * An interface for rendering icons in a Flow-based CMS system.
 * Implementations of this interface are responsible for providing
 * a UI component based on the given icon identifier.
 */
public interface FlowCmsIcon {
    Component renderIcon(String iconEnum);
}
