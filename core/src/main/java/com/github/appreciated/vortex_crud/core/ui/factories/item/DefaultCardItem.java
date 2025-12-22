package com.github.appreciated.vortex_crud.core.ui.factories.item;

import com.github.appreciated.vortex_crud.core.config.model.ItemFactory;
import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import com.github.appreciated.vortex_crud.core.service.VortexCrudContext;
import com.github.appreciated.vortex_crud.core.ui.components.ImageDisplayComponent;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.card.Card;
import com.vaadin.flow.component.card.CardVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;

@CssImport("./vortex-crud-default-card-item-styles.css")
public class DefaultCardItem<FieldType> extends Card {

    public DefaultCardItem(ItemFactory<FieldType> config,
                           Object entity,
                           Integer maxWidth,
                           VortexCrudContext<?, FieldType, ?> context
    ) {
        ReflectionService<FieldType> reflectionService = context.reflectionService();

        setClassName("hoverable");
        if (maxWidth != null) {
            setMaxWidth(maxWidth, Unit.PIXELS);
        }

        // Optional image
        ImageDisplayComponent image = null;
        FieldType imageFieldType = config.imageField();
        if (imageFieldType != null) {
            if (config.resourceProvider() == null) {
                throw new IllegalArgumentException("The item config has a image-field defined but does not provide a image-factory");
            }
            String imagePath = reflectionService.getString(entity, imageFieldType);
            image = new ImageDisplayComponent(config.resourceProvider());
            image.setImageSource(imagePath);
        }

        H4 title = new H4(reflectionService.getString(entity, config.titleField()));
        title.getStyle().set("width", "100%")
                .set("overflow", "hidden")
                .set("text-overflow", "ellipsis");
        setTitle(title);

        if (config.descriptionField() != null) {
            Text description = new Text(reflectionService.getString(entity, config.descriptionField()));
            Div descriptionDiv = new Div(description);
            descriptionDiv.getStyle().set("width", "100%")
                    .set("overflow", "hidden")
                    .set("text-overflow", "ellipsis");
            setSubtitle(descriptionDiv);
        }

        if (image != null) {
            setMedia(image);
        }

        addThemeVariants(CardVariant.LUMO_COVER_MEDIA);
    }
}
