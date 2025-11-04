package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.file_provider.VortexCrudResourceProvider;
import com.github.appreciated.vortex_crud.core.ui.factories.item.VortexCrudItemFactory;
import lombok.Builder;

import java.util.List;

@Builder
public record MultiFormRendererConfiguration<ModelClass, FieldType, RepositoryType>(
        Class<? extends VortexCrudItemFactory<FieldType>> factory,
        FieldType titleField,
        FieldType descriptionField,
        FieldType imageField,
        Class<? extends VortexCrudResourceProvider> resourceProvider,
        boolean inlineEdit,
        FieldType filterField,
        List<InternalFormElement<ModelClass, FieldType, RepositoryType>> children,
        List<RouteRendererConfiguration<ModelClass, FieldType, RepositoryType>> forms
) implements RouteRendererConfiguration<ModelClass, FieldType, RepositoryType> {

}