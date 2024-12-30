package com.github.appreciated.turbo_crud.core.config.model;

import com.github.appreciated.turbo_crud.core.file_provider.TurboCrudFileProvider;
import com.github.appreciated.turbo_crud.core.ui.factories.item.TurboCrudItemFactory;

public interface ItemFactory<FieldId> {

    Class<? extends TurboCrudItemFactory> getFactory();

    FieldId getTitleField();

    FieldId getDescriptionField();

    FieldId getImageField();

    Class<? extends TurboCrudFileProvider> getImageFactory();
}
