package com.github.appreciated.turbo_crud.ui.factories.item;

import com.github.appreciated.turbo_crud.config.model.ItemConfig;
import com.github.appreciated.turbo_crud.model.GenericEntity;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigBeanFactory;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

@CssImport("card-styles.css")
public class DefaultItem extends HorizontalLayout {

    public DefaultItem(Config config, GenericEntity entity, Integer maxWidth) {
        ItemConfig formConfiguration = ConfigBeanFactory.create(config, ItemConfig.class);

        if (maxWidth != null) {
            setMaxWidth(maxWidth + "px");
        }
        addClassName("card");

        // Optional image
        Image image = null;
        if (formConfiguration.getImageField() != null) {
            image = new Image(formConfiguration.getImageField(), "Entity Image");
            image.setMaxWidth("150px");
            image.setMaxHeight("150px");
            image.getStyle().set("margin-right", "10px");
        }

        // Vertical layout for title and description
        VerticalLayout textContainer = new VerticalLayout();
        textContainer.setPadding(false);
        textContainer.setSpacing(false);

        H4 title = new H4(entity.getString(formConfiguration.getTitleField()));
        Div titleDiv = new Div(title);
        textContainer.add(titleDiv);

        if (formConfiguration.getDescriptionField() != null) {
            Text description = new Text(entity.getString(formConfiguration.getDescriptionField()));
            Div descriptionDiv = new Div(description);
            textContainer.add(descriptionDiv);
        }

        if (image != null) {
            add(image);
        }
        add(textContainer);
    }
}
