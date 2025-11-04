package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.ui.factories.dialog.VortexCrudDialogFactory;
import lombok.Builder;
import lombok.With;

@Builder(toBuilder = true)
@With
public record Collection<ModelClass, FieldType, RepositoryType>(
    CollectionConfig<FieldType> config,
    String label,
    Class<? extends VortexCrudDialogFactory<ModelClass, FieldType, RepositoryType>> factory,
    CollectionConfiguration<ModelClass, FieldType, RepositoryType> data,
    String emptyMessage,
    RouteRenderer<ModelClass, FieldType, RepositoryType> child
) {

}