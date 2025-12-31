package com.github.appreciated.vortex_crud.core.config.model;

import java.util.List;


public interface CollectionConfiguration<ModelClass, FieldType, RepositoryType> extends HasDataStore<FieldType, ModelClass> {

    public OneToMany<ModelClass, FieldType, RepositoryType> oneToMany();

    public ManyToMany<ModelClass, FieldType, RepositoryType> manyToMany();

    public List<FieldType> children();
}
