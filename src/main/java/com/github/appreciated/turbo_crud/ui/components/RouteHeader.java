package com.github.appreciated.turbo_crud.ui.components;

import com.github.appreciated.turbo_crud.config.model.Route;
import com.github.appreciated.turbo_crud.ui.factories.icon.TurboCrudIconFactory;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class RouteHeader extends HorizontalLayout {
    public RouteHeader(Route config, TurboCrudIconFactory iconFactory) {
        H2 h2 = new H2(getTranslation(config.getTitle()));
        if (config.getIcon() != null) {
            add(iconFactory.renderIcon(config.getIcon()));
        }
        add(h2);
        setAlignItems(Alignment.CENTER);
    }
}
