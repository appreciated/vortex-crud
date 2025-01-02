package com.github.appreciated.turbo_crud.core.ui.factories.item;

import com.github.appreciated.turbo_crud.core.config.model.ItemFactory;
import com.github.appreciated.turbo_crud.core.entity.data_store.TurboCrudDataStoreFieldNameResolver;
import com.github.appreciated.turbo_crud.core.file_provider.TurboCrudFileProviderRegistry;
import com.github.appreciated.turbo_crud.core.model.GenericEntity;
import com.vaadin.flow.component.Component;

/**
 * Interface for rendering individual items of an entity.
 * Classes implementing this interface should provide a method for rendering items based on a given configuration and entity data.
 */

public interface TurboCrudItemFactory<FieldId> {
    Component renderItem(ItemFactory<FieldId> itemFactory, GenericEntity entity, Integer maxWidth, TurboCrudFileProviderRegistry fileProvider, TurboCrudDataStoreFieldNameResolver<FieldId> resolver);
}
