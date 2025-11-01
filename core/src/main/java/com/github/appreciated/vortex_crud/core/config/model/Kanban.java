package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.ui.factories.item.VortexCrudItemFactory;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public class Kanban<ModelClass, FieldType, RepositoryType>
        extends RouteRendererConfiguration<ModelClass, FieldType, RepositoryType>
        implements ItemFactory<FieldType> {

    private FieldType columnField;
    private FieldType rowIndexField;

    public Kanban(Class<? extends VortexCrudItemFactory<FieldType>> factory) {
        super(factory);
    }
}