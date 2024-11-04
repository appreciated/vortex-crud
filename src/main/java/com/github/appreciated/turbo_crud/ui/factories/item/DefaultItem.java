package com.github.appreciated.turbo_crud.ui.factories.item;

import com.github.appreciated.turbo_crud.config.model.ItemFactoryConfig;
import com.github.appreciated.turbo_crud.model.GenericEntity;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.StreamResource;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

@CssImport("card-styles.css")
public class DefaultItem extends HorizontalLayout {

    public DefaultItem(ItemFactoryConfig config, GenericEntity entity, Integer maxWidth) {
        if (maxWidth != null) {
            setMaxWidth(maxWidth + "px");
        }
        addClassName("card");

        // Optional image
        Image image = null;
        String imageField = config.getImageField();
        if (imageField != null) {
            String imagePath = entity.getString(imageField);
            image = new Image(
                    new StreamResource(imagePath.substring(imagePath.lastIndexOf("/") + 1), () -> {
                        try {
                            return new FileInputStream(imagePath);
                        } catch (FileNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    }),
                    imagePath
            );
            image.setMaxWidth("150px");
            image.setMaxHeight("150px");
            image.getStyle().set("margin-right", "10px");
        }

        // Vertical layout for title and description
        VerticalLayout textContainer = new VerticalLayout();
        textContainer.setPadding(false);
        textContainer.setSpacing(false);

        H4 title = new H4(entity.getString(config.getTitleField()));
        Div titleDiv = new Div(title);
        textContainer.add(titleDiv);

        if (config.getDescriptionField() != null) {
            Text description = new Text(entity.getString(config.getDescriptionField()));
            Div descriptionDiv = new Div(description);
            textContainer.add(descriptionDiv);
        }

        if (image != null) {
            add(image);
        }
        add(textContainer);
    }
}
