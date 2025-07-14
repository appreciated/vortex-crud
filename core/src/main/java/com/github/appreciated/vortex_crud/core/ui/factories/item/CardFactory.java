package com.github.appreciated.vortex_crud.core.ui.factories.item;

import com.github.appreciated.vortex_crud.core.config.model.ItemFactory;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFieldNameResolver;
import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import com.github.appreciated.vortex_crud.core.file_provider.VortexCrudFileProviderRegistry;
import com.vaadin.flow.component.Component;

/**
 * Default implementation of the {@link VortexCrudItemFactory} interface for rendering entity items as cards.
 * This renderer supports displaying images, titles, and descriptions in a card layout with customizable styling.
 */

public class CardFactory<FieldId> implements VortexCrudItemFactory<FieldId> {

    @Override
    public Component renderItem(ItemFactory<FieldId> itemFactory,
                                Object entity,
                                Integer maxWidth,
                                VortexCrudFileProviderRegistry fileProvider,
                                VortexCrudDataStoreFieldNameResolver<FieldId> resolver,
                                ReflectionService<FieldId> reflectionService) {
        return new DefaultCardItem<>(itemFactory, entity, maxWidth, fileProvider, reflectionService);
    }
}