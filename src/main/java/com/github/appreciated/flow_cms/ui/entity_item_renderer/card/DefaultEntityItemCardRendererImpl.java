package com.github.appreciated.flow_cms.ui.entity_item_renderer.card;

import com.github.appreciated.flow_cms.service.GenericEntity;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class DefaultEntityItemCardRendererImpl implements EntityItemRenderer {

    @Override
    public Component renderItem(GenericEntity entity, int maxWidth) {
        VerticalLayout card = new VerticalLayout();
        card.setMaxWidth(maxWidth + "px");
        card.getStyle().set("border-radius", "8px");
        card.getStyle().set("box-shadow", "0 2px 5px rgba(0, 0, 0, 0.1)");
        card.getStyle().set("padding", "10px");
        card.getStyle().set("background-image", "linear-gradient(var(--lumo-contrast-5pct), var(--lumo-contrast-5pct))");

        Image image = new Image("https://via.placeholder.com/150", "Placeholder Image");

        Text label = new Text("Some Text");

        card.add(image, label);
        return card;
    }
}
