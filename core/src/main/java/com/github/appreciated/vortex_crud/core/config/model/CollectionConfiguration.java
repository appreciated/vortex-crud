package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudQueryDataStore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;


public interface CollectionConfiguration<ModelClass, FieldType, RepositoryType> extends HasDataStore<FieldType, ModelClass> {

    public OneToMany<ModelClass, FieldType, RepositoryType> oneToMany();

    public ManyToMany<ModelClass, FieldType, RepositoryType> manyToMany();

    public List<FieldType> children();
}
