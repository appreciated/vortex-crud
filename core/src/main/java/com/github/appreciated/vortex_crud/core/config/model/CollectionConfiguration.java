package com.github.appreciated.vortex_crud.core.config.model;

import lombok.Builder;
import lombok.With;

import java.util.List;

@Builder(toBuilder = true)
@With
public record CollectionConfiguration<ModelClass, FieldType, RepositoryType>(
    RepositoryType dataStore,
    OneToMany<ModelClass, FieldType, RepositoryType> oneToMany,
    ManyToMany<ModelClass, FieldType, RepositoryType> manyToMany,
    List<FieldType> children
) {
}