package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFactoryRegistry;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService;

public interface DataStoreProvider<ModelClass, FieldType, RepositoryType> {
    VortexCrudDataStore<FieldType, ModelClass> getDataStore(VortexCrudDataStoreFactoryRegistry<ModelClass, FieldType, RepositoryType> registry);

    DataStoreConfig<ModelClass, FieldType, RepositoryType> getConfig(VortexCrudConfigService<ModelClass, FieldType, RepositoryType> configService);

    RepositoryType getKey();
}
