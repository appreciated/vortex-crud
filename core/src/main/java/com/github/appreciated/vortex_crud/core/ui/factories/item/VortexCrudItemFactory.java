package com.github.appreciated.vortex_crud.core.ui.factories.item;

import com.github.appreciated.vortex_crud.core.config.model.ItemFactory;
import com.github.appreciated.vortex_crud.core.service.VortexCrudContext;
import com.vaadin.flow.component.Component;

public interface VortexCrudItemFactory<FieldType> {
    Component renderItem(ItemFactory<FieldType> itemFactory,
                         Object entity,
                         Integer maxWidth,
                         VortexCrudContext<?, FieldType, ?> context
    );
}
