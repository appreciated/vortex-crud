package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.file_provider.VortexCrudResourceProvider;
import com.github.appreciated.vortex_crud.core.ui.factories.item.VortexCrudItemFactory;

public interface ItemFactory<FieldType> {

    Class<? extends VortexCrudItemFactory> getFactory();

    FieldType getTitleField();

    FieldType getDescriptionField();

    FieldType getImageField();

    Class<? extends VortexCrudResourceProvider> getImageFactory();
}
