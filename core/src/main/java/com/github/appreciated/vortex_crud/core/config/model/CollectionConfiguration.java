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
    // Explicit getters for backwards compatibility
    public RepositoryType getDataStore() {
        return dataStore;
    }

    public OneToMany<ModelClass, FieldType, RepositoryType> getOneToMany() {
        return oneToMany;
    }

    public ManyToMany<ModelClass, FieldType, RepositoryType> getManyToMany() {
        return manyToMany;
    }

    public List<FieldType> getChildren() {
        return children;
    }
}