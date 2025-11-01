package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.file_provider.VortexCrudResourceProvider;
import com.github.appreciated.vortex_crud.core.ui.factories.item.VortexCrudItemFactory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class RouteRendererConfiguration<ModelClass, FieldType, RepositoryType>
        implements RouteConfig<FieldType> {

    private Class<? extends VortexCrudItemFactory<FieldType>> factory;

    private FieldType titleField;

    private FieldType descriptionField;

    private FieldType imageField;

    private Class<? extends VortexCrudResourceProvider> imageFactory;

    private boolean inlineEdit;

    private FieldType filterField;

    private List<InternalFormElement<ModelClass, FieldType, RepositoryType>> children;

    private List<String> writeRoles;

    private List<String> readOnlyRoles;

    public RouteRendererConfiguration(Class<? extends VortexCrudItemFactory<FieldType>> factory) {
        this.factory = factory;
    }
}