package com.github.appreciated.vortex_crud.core.ui.factories.item;

import com.github.appreciated.vortex_crud.core.config.model.ItemFactory;
import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import com.github.appreciated.vortex_crud.core.file_provider.VortexCrudFileProviderRegistry;
import com.github.appreciated.vortex_crud.core.ui.components.ImageDisplayComponent;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.card.Card;
import com.vaadin.flow.component.card.CardVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;

@CssImport("vortex-crud-default-card-item-styles.css")
public class DefaultCardItem<FieldId> extends Card {

    public DefaultCardItem(ItemFactory<FieldId> config,
                           Object entity,
                           Integer maxWidth,
                           VortexCrudFileProviderRegistry provider,
                           ReflectionService<FieldId> reflectionService
    ) {
        setClassName("hoverable");
        if (maxWidth != null) {
            setMaxWidth(maxWidth, Unit.PIXELS);
        }

        // Optional image
        ImageDisplayComponent image = null;
        FieldId imageFieldId = config.getImageField();
        if (imageFieldId != null) {
            if (config.getImageFactory() == null) {
                throw new IllegalArgumentException("The item config has a image-field defined but does not provide a image-factory");
            }
            String imagePath = reflectionService.getString(entity, imageFieldId);
            image = new ImageDisplayComponent(provider.getFactory(config.getImageFactory()));
            image.setImageSource(imagePath);
        }

        H4 title = new H4(reflectionService.getString(entity, config.getTitleField()));
        title.getStyle().set("width", "100%")
                .set("overflow", "hidden")
                .set("text-overflow", "ellipsis");
        setTitle(title);

        if (config.getDescriptionField() != null) {
            Text description = new Text(reflectionService.getString(entity, config.getDescriptionField()));
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
