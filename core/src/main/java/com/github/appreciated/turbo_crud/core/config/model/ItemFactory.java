package com.github.appreciated.turbo_crud.core.config.model;

import com.github.appreciated.turbo_crud.core.file_provider.TurboCrudFileProvider;
import com.github.appreciated.turbo_crud.core.ui.factories.item.TurboCrudItemFactory;

public interface ItemFactory {

    Class<? extends TurboCrudItemFactory> getFactory();

    String getTitleField();

    String getDescriptionField();

    String getImageField();

    Class<? extends TurboCrudFileProvider> getImageFactory();
}
