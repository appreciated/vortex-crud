package com.github.appreciated.vortex_crud.core.ui.factories.item;

import com.github.appreciated.vortex_crud.core.config.model.ItemFactory;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFieldNameResolver;
import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import com.github.appreciated.vortex_crud.core.file_provider.VortexCrudFileProviderRegistry;
import com.vaadin.flow.component.Component;

/**
 * Interface for rendering individual items of an entity.
 * Classes implementing this interface should provide a method for rendering items based on a given configuration and entity data.
 */

public interface VortexCrudItemFactory<FieldId> {
    Component renderItem(ItemFactory<FieldId> itemFactory,
                         Object entity,
                         Integer maxWidth,
                         VortexCrudFileProviderRegistry fileProvider,
                         VortexCrudDataStoreFieldNameResolver<FieldId> resolver,
                         ReflectionService reflectionService
    );
}
