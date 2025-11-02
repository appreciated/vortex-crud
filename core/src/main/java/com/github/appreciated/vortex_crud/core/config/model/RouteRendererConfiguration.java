package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.file_provider.VortexCrudResourceProvider;
import com.github.appreciated.vortex_crud.core.ui.factories.item.VortexCrudItemFactory;

import java.util.List;

public interface RouteRendererConfiguration<ModelClass, FieldType, RepositoryType> extends RouteConfig<FieldType> {

    Class<? extends VortexCrudItemFactory<FieldType>> factory();
    FieldType titleField();
    FieldType descriptionField();
    FieldType imageField();
    Class<? extends VortexCrudResourceProvider> imageFactory();
    boolean inlineEdit();
    FieldType filterField();
    List<InternalFormElement<ModelClass, FieldType, RepositoryType>> children();
    Class<? extends VortexCrudItemFactory<FieldType>> getFactory();
}