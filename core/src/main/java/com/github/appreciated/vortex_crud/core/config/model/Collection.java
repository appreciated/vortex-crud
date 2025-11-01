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
    // Explicit getters for backwards compatibility
    public CollectionConfiguration<ModelClass, FieldType, RepositoryType> getData() {
        return data;
    }

    public RouteRenderer<ModelClass, FieldType, RepositoryType> getChild() {
        return child;
    }

    public Class<? extends VortexCrudDialogFactory<ModelClass, FieldType, RepositoryType>> getFactory() {
        return factory;
    }

    public String getLabel() {
        return label;
    }

    public CollectionConfig<FieldType> getConfig() {
        return config;
    }

    public String getEmptyMessage() {
        return emptyMessage;
    }
}