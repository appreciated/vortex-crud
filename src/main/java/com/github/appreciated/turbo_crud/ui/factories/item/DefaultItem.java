package com.github.appreciated.turbo_crud.ui.factories.item;

import com.github.appreciated.turbo_crud.config.model.ItemFactory;
import com.github.appreciated.turbo_crud.file_provider.TurboCrudFileProviderRegistry;
import com.github.appreciated.turbo_crud.model.GenericEntity;
import com.github.appreciated.turbo_crud.ui.components.ImageDisplayComponent;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

@CssImport("card-styles.css")
public class DefaultItem extends HorizontalLayout {

    public DefaultItem(ItemFactory config, GenericEntity entity, Integer maxWidth, TurboCrudFileProviderRegistry provider) {
        if (maxWidth != null) {
            setMaxWidth(maxWidth + "px");
        }
        addClassName("card");

        // Optional image
        ImageDisplayComponent image = null;
        String imageField = config.getImageField();
        if (imageField != null) {
            if (config.getImageFactory() == null){
                throw new IllegalArgumentException("The item config has a image-field defined but does not provide a image-factory");
            }
            String imagePath = entity.getString(imageField);
            image = new ImageDisplayComponent(provider.getFactory(config.getImageFactory()));
            image.setImageSource(imagePath);
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
