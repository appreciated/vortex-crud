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
public class PdfFieldRendererConfiguration<ModelClass, FieldType, RepositoryType> implements RouteRendererConfiguration<ModelClass, FieldType, RepositoryType> {

    private VortexCrudItemFactory<FieldType> factory;

    @Override
    public VortexCrudItemFactory<FieldType> factoryInstance() {
        return factory;
    }

    private FieldType titleField;

    private FieldType descriptionField;

    private FieldType imageField;

    private Class<? extends VortexCrudResourceProvider> resourceProvider;

    private boolean inlineEdit;

    private FieldType filterField;

    private List<InternalFormElement<ModelClass, FieldType, RepositoryType>> children;
}
