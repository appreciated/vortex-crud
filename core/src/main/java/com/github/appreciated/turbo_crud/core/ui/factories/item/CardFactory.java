package com.github.appreciated.turbo_crud.core.ui.factories.item;

import com.github.appreciated.turbo_crud.core.config.model.ItemFactory;
import com.github.appreciated.turbo_crud.core.file_provider.TurboCrudFileProviderRegistry;
import com.github.appreciated.turbo_crud.core.model.GenericEntity;
import com.vaadin.flow.component.Component;

/**
 * Default implementation of the {@link TurboCrudItemFactory} interface for rendering entity items as cards.
 * This renderer supports displaying images, titles, and descriptions in a card layout with customizable styling.
 */

public class CardFactory implements TurboCrudItemFactory {

    @Override
    public Component renderItem(ItemFactory itemFactory, GenericEntity entity, Integer maxWidth, TurboCrudFileProviderRegistry fileProvider) {
        return new DefaultItem(itemFactory, entity, maxWidth, fileProvider);
    }
}