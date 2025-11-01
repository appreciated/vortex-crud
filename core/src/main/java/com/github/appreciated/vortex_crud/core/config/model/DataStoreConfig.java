package com.github.appreciated.vortex_crud.core.config.model;

import lombok.Builder;
import lombok.With;

import java.util.Map;

@Builder(toBuilder = true)
@With
public record DataStoreConfig<ModelClass, FieldType, RepositoryType>(
    RepositoryType factory,
    Map<FieldType, Field<ModelClass, FieldType, RepositoryType>> fields
) {
    // Explicit getters for backwards compatibility
    public RepositoryType getFactory() {
        return factory;
    }

    public Map<FieldType, Field<ModelClass, FieldType, RepositoryType>> getFields() {
        return fields;
    }
}