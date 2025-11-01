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
public class MultiFormRendererConfiguration<ModelClass, FieldType, RepositoryType>
        extends RouteRendererConfiguration<ModelClass, FieldType, RepositoryType> {

    private List<RouteRendererConfiguration<ModelClass, FieldType, RepositoryType>> forms;

    public MultiFormRendererConfiguration(Class<? extends VortexCrudItemFactory<FieldType>> factory) {
        super(factory);
    }
}