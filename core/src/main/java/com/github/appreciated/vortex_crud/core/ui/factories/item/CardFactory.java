package com.github.appreciated.vortex_crud.core.ui.factories.item;

import com.github.appreciated.vortex_crud.core.config.model.ItemFactory;
import com.github.appreciated.vortex_crud.core.context.VortexCrudContext;
import com.vaadin.flow.component.Component;

/**
 * Default implementation of the {@link VortexCrudItemFactory} interface for rendering entity items as cards.
 * This renderer supports displaying images, titles, and descriptions in a card layout with customizable styling.
 */

public class CardFactory<FieldType> implements VortexCrudItemFactory<FieldType> {

    @Override
    public Component renderItem(ItemFactory<FieldType> itemFactory,
                                Object entity,
                                Integer maxWidth,
                                VortexCrudContext<?, FieldType, ?> context) {
        return new DefaultCardItem<>(itemFactory, entity, maxWidth, context);
    }
}
