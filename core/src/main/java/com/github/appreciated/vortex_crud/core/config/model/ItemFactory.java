package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.file_provider.VortexCrudResourceProvider;
import com.github.appreciated.vortex_crud.core.ui.factories.item.VortexCrudItemFactory;

public interface ItemFactory<FieldId> {

    Class<? extends VortexCrudItemFactory> getFactory();

    FieldId getTitleField();

    FieldId getDescriptionField();

    FieldId getImageField();

    Class<? extends VortexCrudResourceProvider> getImageFactory();
}
