package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.file_provider.VortexCrudResourceProvider;

public interface ItemFactory<FieldType> {

    FieldType titleField();

    FieldType descriptionField();

    FieldType imageField();

    Class<? extends VortexCrudResourceProvider> resourceProvider();
}
