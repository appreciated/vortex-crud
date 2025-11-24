package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFactoryRegistry;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class InstanceDataStoreProvider<ModelClass, FieldType, RepositoryType> implements DataStoreProvider<ModelClass, FieldType, RepositoryType> {

    private final VortexCrudDataStore<FieldType, ModelClass> dataStore;
    private final DataStoreConfig<ModelClass, FieldType, RepositoryType> config;

    public InstanceDataStoreProvider(VortexCrudDataStore<FieldType, ModelClass> dataStore) {
        this(dataStore, null);
    }

    @Override
    public VortexCrudDataStore<FieldType, ModelClass> getDataStore(VortexCrudDataStoreFactoryRegistry<ModelClass, FieldType, RepositoryType> registry) {
        return dataStore;
    }

    @Override
    public DataStoreConfig<ModelClass, FieldType, RepositoryType> getConfig(VortexCrudConfigService<ModelClass, FieldType, RepositoryType> configService) {
        return config;
    }

    @Override
    public RepositoryType getKey() {
        return null;
    }
}
