package com.github.appreciated.flow_cms.ui.entity_item_renderer.card;

import com.github.appreciated.flow_cms.config.model.ItemRendererConfig;
import com.github.appreciated.flow_cms.service.GenericEntity;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

/**
 * Default implementation of the EntityItemRenderer interface for rendering entity items as cards.
 * This renderer supports displaying images, titles, and descriptions in a card layout with customizable styling.
 */

public class DefaultEntityItemCardRendererImpl implements EntityItemRenderer {

    @Override
    public Component renderItem(ItemRendererConfig itemRendererConfig, GenericEntity entity, Integer maxWidth) {
        HorizontalLayout card = new HorizontalLayout();
        if (maxWidth != null) {
            card.setMaxWidth(maxWidth + "px");
        }
        card.getStyle().set("border-radius", "8px");
        card.getStyle().set("box-shadow", "0 2px 5px rgba(0, 0, 0, 0.1)");
        card.getStyle().set("padding", "10px");
        card.getStyle().set("cursor", "pointer");
        card.getStyle().set("background-image", "linear-gradient(var(--lumo-contrast-5pct), var(--lumo-contrast-5pct))");

        // Optional image
        Image image = null;
        if (itemRendererConfig.getImage_field() != null) {
            image = new Image(itemRendererConfig.getImage_field(), "Entity Image");
            image.setMaxWidth("150px");
            image.setMaxHeight("150px");
            image.getStyle().set("margin-right", "10px");
        }

        // Vertical layout for title and description
        VerticalLayout textContainer = new VerticalLayout();
        textContainer.setPadding(false);
        textContainer.setSpacing(false);

        Text title = new Text((String) entity.get(itemRendererConfig.getTitle_field()));
        Div titleDiv = new Div(title);
        textContainer.add(titleDiv);

        if (itemRendererConfig.getDescription_field() != null) {
            Text description = new Text((String) entity.get(itemRendererConfig.getDescription_field()));
            Div descriptionDiv = new Div(description);
            textContainer.add(descriptionDiv);
        }

        if (image != null) {
            card.add(image);
        }
        card.add(textContainer);
        return card;
    }
}