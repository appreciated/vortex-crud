package com.github.appreciated.flow_cms.ui.component_renderer.card;

import com.github.appreciated.flow_cms.service.GenericEntity;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public interface CardRenderer {
    Component createCardComponent(GenericEntity entity, int maxWidth);
}
