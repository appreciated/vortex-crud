package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.file_provider.VortexCrudResourceProvider;
import com.github.appreciated.vortex_crud.core.ui.factories.item.VortexCrudItemFactory;

public interface ItemFactory<FieldType> {

    VortexCrudItemFactory<FieldType> factoryInstance();

    FieldType titleField();

    FieldType descriptionField();

    FieldType imageField();

    Class<? extends VortexCrudResourceProvider> resourceProvider();
}
