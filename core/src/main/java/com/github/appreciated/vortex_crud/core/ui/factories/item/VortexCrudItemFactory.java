package com.github.appreciated.vortex_crud.core.ui.factories.item;

import com.github.appreciated.vortex_crud.core.config.model.ItemFactory;
import com.github.appreciated.vortex_crud.core.service.VortexCrudContext;
import com.vaadin.flow.component.Component;
import jakarta.annotation.Nullable;

public interface VortexCrudItemFactory<FieldType> {
    Component renderItem(VortexCrudContext<?, FieldType, ?> context,
                         ItemFactory<FieldType> configuration,
                         Object item,
                         @Nullable Integer width);
}
