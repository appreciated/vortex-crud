package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.ui.factories.dialog.VortexCrudDialogFactory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Collection<ModelClass, FieldType, RepositoryType> {

    private CollectionConfig<FieldType> configuration;

    private String label;

    private VortexCrudDialogFactory<ModelClass, FieldType, RepositoryType> factoryInstance;

    private CollectionConfiguration<ModelClass, FieldType, RepositoryType> data;

    private String emptyMessage;

    private RouteRenderer<ModelClass, FieldType, RepositoryType> child;
}