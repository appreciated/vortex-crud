package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.file_provider.VortexCrudResourceProvider;
import com.github.appreciated.vortex_crud.core.ui.factories.item.VortexCrudItemFactory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Accessors(fluent = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class MultiFormRendererConfiguration<ModelClass, FieldType, RepositoryType> implements RouteRendererConfiguration<ModelClass, FieldType, RepositoryType> {

    // Not using ItemFactory here as MultiFormRendererConfiguration doesn't implement ItemFactory
    // But keeping structure consistent if needed in future
    private VortexCrudItemFactory<FieldType> factoryInstance;

    private FieldType titleField;

    private FieldType descriptionField;

    private FieldType imageField;

    private Class<? extends VortexCrudResourceProvider> resourceProvider;

    private boolean inlineEdit;

    private FieldType filterField;

    private List<InternalFormElement<ModelClass, FieldType, RepositoryType>> children;

    private List<RouteRendererConfiguration<ModelClass, FieldType, RepositoryType>> forms;
}