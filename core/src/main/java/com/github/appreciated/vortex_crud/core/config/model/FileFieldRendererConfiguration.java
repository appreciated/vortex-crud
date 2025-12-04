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
public class FileFieldRendererConfiguration<ModelClass, FieldType, RepositoryType> implements RouteRendererConfiguration<ModelClass, FieldType, RepositoryType> {

    private VortexCrudItemFactory<FieldType> factory;

    private FieldType titleField;

    private FieldType descriptionField;

    private FieldType imageField;

    private VortexCrudResourceProvider resourceProvider;

    private boolean inlineEdit;

    private FieldType filterField;

    private List<InternalFormElement<ModelClass, FieldType, RepositoryType>> children;

    public VortexCrudItemFactory<FieldType> factory() { return factory; }
    public FieldType titleField() { return titleField; }
    public FieldType descriptionField() { return descriptionField; }
    public FieldType imageField() { return imageField; }
    public VortexCrudResourceProvider resourceProvider() { return resourceProvider; }
    public boolean inlineEdit() { return inlineEdit; }
    public FieldType filterField() { return filterField; }
    public List<InternalFormElement<ModelClass, FieldType, RepositoryType>> children() { return children; }
}
