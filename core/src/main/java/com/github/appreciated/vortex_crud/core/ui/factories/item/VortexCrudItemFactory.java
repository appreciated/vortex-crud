package com.github.appreciated.vortex_crud.core.ui.factories.item;

import com.github.appreciated.vortex_crud.core.config.model.ItemFactory;
import com.github.appreciated.vortex_crud.core.service.VortexCrudContext;
import com.vaadin.flow.component.Component;

/**
 * Interface for rendering individual items of an entity.
 * Classes implementing this interface should provide a method for rendering items based on a given configuration and entity data.
 */

public interface VortexCrudItemFactory<FieldType> {
    Component renderItem(ItemFactory<FieldType> itemFactory,
                         Object entity,
                         Integer maxWidth,
                         VortexCrudContext<?, FieldType, ?> context
    );
}
