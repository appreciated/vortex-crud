package com.github.appreciated.vortex_crud.core.ui.components;

import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class RouteHeader extends HorizontalLayout {
    public RouteHeader(RouteRenderer<?, ?, ?> config) {
        H1 h1 = new H1(config.title() != null ? getTranslation(config.title()) : "");
        if (config.iconFactory() != null) {
            Component icon = config.iconFactory().get();
            icon.getStyle()
                    .set("color", "var(--lumo-primary-text-color)")
                    .set("opacity", "0.5");
            add(icon);
        }
        add(h1);
        setAlignItems(Alignment.CENTER);
        setMinHeight("53px");
        getStyle().set("box-sizing", "content-box");
    }
}
