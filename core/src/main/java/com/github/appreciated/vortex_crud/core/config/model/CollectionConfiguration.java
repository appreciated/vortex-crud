package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Accessors(fluent = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class CollectionConfiguration<ModelClass, FieldType, RepositoryType> implements HasDataStore<FieldType, ModelClass> {

    private DataStoreConfig<ModelClass, FieldType, RepositoryType> dataStoreConfig;

    @Override
    public VortexCrudDataStore<FieldType, ModelClass> dataStoreInstance() {
        return dataStoreConfig != null ? dataStoreConfig.dataStoreInstance() : null;
    }

    private OneToMany<ModelClass, FieldType, RepositoryType> oneToMany;

    private ManyToMany<ModelClass, FieldType, RepositoryType> manyToMany;

    private List<FieldType> children;

    public OneToMany<ModelClass, FieldType, RepositoryType> oneToMany() {
        return oneToMany;
    }

    public ManyToMany<ModelClass, FieldType, RepositoryType> manyToMany() {
        return manyToMany;
    }

    public List<FieldType> children() {
        return children;
    }
}
