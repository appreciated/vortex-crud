package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFactoryRegistry;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class KeyDataStoreProvider<ModelClass, FieldType, RepositoryType> implements DataStoreProvider<ModelClass, FieldType, RepositoryType> {

    private final RepositoryType key;

    @Override
    public VortexCrudDataStore<FieldType, ModelClass> getDataStore(VortexCrudDataStoreFactoryRegistry<ModelClass, FieldType, RepositoryType> registry) {
        return registry.getDataStore(key);
    }

    @Override
    public DataStoreConfig<ModelClass, FieldType, RepositoryType> getConfig(VortexCrudConfigService<ModelClass, FieldType, RepositoryType> configService) {
        return configService.configuration().dataStores().get(key);
    }

    @Override
    public RepositoryType getKey() {
        return key;
    }
}
