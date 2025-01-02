package com.github.appreciated.turbo_crud.core.ui.factories.item;

import com.github.appreciated.turbo_crud.core.config.model.ItemFactory;
import com.github.appreciated.turbo_crud.core.entity.data_store.TurboCrudDataStoreFieldNameResolver;
import com.github.appreciated.turbo_crud.core.file_provider.TurboCrudFileProviderRegistry;
import com.github.appreciated.turbo_crud.core.model.GenericEntity;
import com.vaadin.flow.component.Component;

/**
 * Default implementation of the {@link TurboCrudItemFactory} interface for rendering entity items as cards.
 * This renderer supports displaying images, titles, and descriptions in a card layout with customizable styling.
 */

public class CardFactory<FieldId> implements TurboCrudItemFactory<FieldId> {

    @Override
    public Component renderItem(ItemFactory<FieldId> itemFactory, GenericEntity entity, Integer maxWidth, TurboCrudFileProviderRegistry fileProvider, TurboCrudDataStoreFieldNameResolver<FieldId> resolver) {
        return new DefaultItem<>(itemFactory, entity, maxWidth, fileProvider, resolver);
    }
}