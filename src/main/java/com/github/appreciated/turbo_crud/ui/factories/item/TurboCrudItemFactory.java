package com.github.appreciated.turbo_crud.ui.factories.item;

import com.github.appreciated.turbo_crud.config.model.ItemFactory;
import com.github.appreciated.turbo_crud.file_provider.TurboCrudFileProviderRegistry;
import com.github.appreciated.turbo_crud.model.GenericEntity;
import com.vaadin.flow.component.Component;

/**
 * Interface for rendering individual items of an entity.
 * Classes implementing this interface should provide a method for rendering items based on a given configuration and entity data.
 */

public interface TurboCrudItemFactory {
    Component renderItem(ItemFactory itemFactory, GenericEntity entity, Integer maxWidth, TurboCrudFileProviderRegistry fileProvider);
}
