package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.ui.factories.item.VortexCrudItemFactory;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public class GridItemRendererConfiguration<ModelClass, FieldType, RepositoryType>
        extends RouteRendererConfiguration<ModelClass, FieldType, RepositoryType>
        implements ItemFactory<FieldType> {

    private List<InternalFormElement<ModelClass, FieldType, RepositoryType>> children;

    public GridItemRendererConfiguration(Class<? extends VortexCrudItemFactory<FieldType>> factory) {
        super(factory);
    }
}