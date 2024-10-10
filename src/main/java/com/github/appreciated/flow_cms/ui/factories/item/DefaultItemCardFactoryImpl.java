package com.github.appreciated.flow_cms.ui.factories.item;

import com.github.appreciated.flow_cms.config.model.ItemFactoryConfig;
import com.github.appreciated.flow_cms.service.GenericEntity;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

/**
 * Default implementation of the EntityItemRenderer interface for rendering entity items as cards.
 * This renderer supports displaying images, titles, and descriptions in a card layout with customizable styling.
 */

public class DefaultItemCardFactoryImpl implements FlowCmsItemFactory {

    @Override
    public Component renderItem(ItemFactoryConfig itemFactoryConfig, GenericEntity entity, Integer maxWidth) {
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
        if (itemFactoryConfig.getImageColumn() != null) {
            image = new Image(itemFactoryConfig.getImageColumn(), "Entity Image");
            image.setMaxWidth("150px");
            image.setMaxHeight("150px");
            image.getStyle().set("margin-right", "10px");
        }

        // Vertical layout for title and description
        VerticalLayout textContainer = new VerticalLayout();
        textContainer.setPadding(false);
        textContainer.setSpacing(false);

        H4 title = new H4(entity.getString(itemFactoryConfig.getTitleColumn()));
        Div titleDiv = new Div(title);
        textContainer.add(titleDiv);

        if (itemFactoryConfig.getDescriptionColumn() != null) {
            Text description = new Text(entity.getString(itemFactoryConfig.getDescriptionColumn()));
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