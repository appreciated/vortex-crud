package com.github.appreciated.turbo_crud.core.ui.components;

import com.github.appreciated.turbo_crud.core.config.model.Route;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class RouteHeader extends HorizontalLayout {
    public RouteHeader(Route config) {
        H2 h2 = new H2(getTranslation(config.getTitle()));
        if (config.getIconFactory() != null) {
            add(config.getIconFactory().get());
        }
        add(h2);
        setAlignItems(Alignment.CENTER);
        setMinHeight("53px");
        getStyle().set("box-sizing", "content-box");
    }
}
