package com.github.appreciated.vortex_crud.core.ui.factories.item;

import com.github.appreciated.vortex_crud.core.config.model.ItemFactory;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFieldNameResolver;
import com.github.appreciated.vortex_crud.core.file_provider.VortexCrudFileProviderRegistry;
import com.github.appreciated.vortex_crud.core.model.GenericEntity;
import com.github.appreciated.vortex_crud.core.ui.components.ImageDisplayComponent;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

@CssImport("card-styles.css")
public class DefaultItem<FieldId> extends VerticalLayout {

    public DefaultItem(ItemFactory<FieldId> config, GenericEntity entity, Integer maxWidth, VortexCrudFileProviderRegistry provider, VortexCrudDataStoreFieldNameResolver<FieldId> resolver) {
        if (maxWidth != null) {
            setMaxWidth(maxWidth + "px");
        }
        addClassName("card");
        setPadding(false);
        setSpacing(false);

        // Optional image
        ImageDisplayComponent image = null;
        FieldId imageFieldId = config.getImageField();
        if (imageFieldId != null) {
            if (config.getImageFactory() == null) {
                throw new IllegalArgumentException("The item config has a image-field defined but does not provide a image-factory");
            }
            String imagePath = entity.getString(resolver.getKeyForFieldId(imageFieldId));
            image = new ImageDisplayComponent(provider.getFactory(config.getImageFactory()));
            image.setImageSource(imagePath);
            image.setWidthFull();
            image.setMaxHeight("150px");
            image.setObjectFit("");
            image.getStyle().set("border-radius", "var(--lumo-border-radius-m)");
        }

        // Vertical layout for title and description
        VerticalLayout textContainer = new VerticalLayout();
        textContainer.setPadding(true);
        textContainer.setSpacing(false);

        H4 title = new H4(entity.getString(resolver.getKeyForFieldId(config.getTitleField())));
        Div titleDiv = new Div(title);
        textContainer.add(titleDiv);

        if (config.getDescriptionField() != null) {
            Text description = new Text(entity.getString(resolver.getKeyForFieldId(config.getDescriptionField())));
            Div descriptionDiv = new Div(description);
            textContainer.add(descriptionDiv);
        }

        if (image != null) {
            add(image);
        }
        add(textContainer);
    }
}
