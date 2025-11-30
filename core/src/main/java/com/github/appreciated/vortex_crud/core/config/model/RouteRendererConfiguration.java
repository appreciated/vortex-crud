package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.file_provider.VortexCrudResourceProvider;
import com.github.appreciated.vortex_crud.core.ui.factories.item.VortexCrudItemFactory;

import java.util.List;

public interface RouteRendererConfiguration<ModelClass, FieldType, RepositoryType> {
    VortexCrudItemFactory<FieldType> factoryInstance();
    FieldType titleField();
    FieldType descriptionField();
    FieldType imageField();
    Class<? extends VortexCrudResourceProvider> resourceProvider();
    boolean inlineEdit();
    FieldType filterField();
    List<InternalFormElement<ModelClass, FieldType, RepositoryType>> children();
}