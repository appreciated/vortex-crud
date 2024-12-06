package com.github.appreciated.turbo_crud.ui.factories.item;

import com.github.appreciated.turbo_crud.config.model.ItemFactory;
import com.github.appreciated.turbo_crud.file_provider.TurboCrudFileProviderRegistry;
import com.github.appreciated.turbo_crud.model.GenericEntity;
import com.vaadin.flow.component.Component;

/**
 * Default implementation of the {@link TurboCrudItemFactory} interface for rendering entity items as cards.
 * This renderer supports displaying images, titles, and descriptions in a card layout with customizable styling.
 */

public class TCItemCardFactoryImpl implements TurboCrudItemFactory {

    @Override
    public Component renderItem(ItemFactory itemFactory, GenericEntity entity, Integer maxWidth, TurboCrudFileProviderRegistry fileProvider) {
        return new DefaultItem(itemFactory, entity, maxWidth, fileProvider);
    }
}