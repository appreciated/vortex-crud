package com.github.appreciated.vortex_crud.core.ui.factories.item;

import com.github.appreciated.vortex_crud.core.config.model.ItemFactory;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFieldNameResolver;
import com.github.appreciated.vortex_crud.core.file_provider.VortexCrudFileProviderRegistry;
import com.github.appreciated.vortex_crud.core.model.GenericEntity;
import com.github.appreciated.vortex_crud.core.ui.components.ImageDisplayComponent;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.card.Card;
import com.vaadin.flow.component.card.CardVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

@CssImport("master-detail-styles.css")
public class DefaultItem<FieldId> extends Card {

    public DefaultItem(ItemFactory<FieldId> config, GenericEntity entity, Integer maxWidth, VortexCrudFileProviderRegistry provider, VortexCrudDataStoreFieldNameResolver<FieldId> resolver) {
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
            String imagePath = entity.getString(resolver.getKeyForFieldId(imageFieldId));
            image = new ImageDisplayComponent(provider.getFactory(config.getImageFactory()));
            image.setImageSource(imagePath);
        }

        H4 title = new H4(entity.getString(resolver.getKeyForFieldId(config.getTitleField())));
        setTitle(title);

        if (config.getDescriptionField() != null) {
            Text description = new Text(entity.getString(resolver.getKeyForFieldId(config.getDescriptionField())));
            Div descriptionDiv = new Div(description);
            setSubtitle(descriptionDiv);
        }

        if (image != null) {
            setMedia(image);
        }

        addThemeVariants(CardVariant.LUMO_COVER_MEDIA);
    }
}
