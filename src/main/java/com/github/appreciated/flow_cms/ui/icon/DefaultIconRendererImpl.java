package com.github.appreciated.flow_cms.ui.icon;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.icon.VaadinIcon;
import org.springframework.stereotype.Service;

/**
 * A default implementation of the {@link FlowCmsIconRenderer} interface.
 * <p>
 * This implementation uses the {@link VaadinIcon} library to render icons
 * based on the provided icon identifier (as a string). It converts the
 * identifier into a {@link VaadinIcon} and creates a {@link Component}
 * that represents the visual icon.
 * </p>
 * <p>
 * Note: The string value passed to the {@code renderIcon} method must match
 * one of the enum constants defined in {@link VaadinIcon}. If the value
 * does not match, an {@link IllegalArgumentException} will be thrown.
 * </p>
 */

@Service
public class DefaultIconRendererImpl implements FlowCmsIconRenderer {

    @Override
    public Component renderIcon(String iconEnum) {
        return VaadinIcon.valueOf(iconEnum).create();
    }
}