package com.github.appreciated.vortex_crud.core.ui.components;

import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class RouteHeader extends HorizontalLayout {
    public RouteHeader(RouteRenderer<?, ?, ?> config) {
        H2 h2 = new H2(getTranslation(config.getTitle()));
        if (config.getIconFactory() != null) {
            Component icon = config.getIconFactory().get();
            icon.getStyle()
                    .set("color", "var(--lumo-primary-text-color)")
                    .set("opacity", "0.5");
            add(icon);
        }
        add(h2);
        setAlignItems(Alignment.CENTER);
        setMinHeight("53px");
        getStyle().set("box-sizing", "content-box");
    }
}
