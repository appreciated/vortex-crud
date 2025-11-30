package com.github.appreciated.vortex_crud.jpa.service;

import com.github.appreciated.vortex_crud.core.config.model.*;
import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.DataStoreConfig;
import com.github.appreciated.vortex_crud.core.config.model.DataStoreHooks;
import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.jpa.service.config.JpaRepositoryDataStore;
import com.github.appreciated.vortex_crud.jpa.service.datastore.JpaFieldService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JpaVortexCrudConfigService implements VortexCrudConfigService<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {

    private final Application<JpaRepository<?, ?>, String, JpaRepository<?, ?>> configuration;

    public JpaVortexCrudConfigService(
            VortexCrudConfigurationProvider<JpaRepository<?, ?>, String, JpaRepository<?, ?>> configurationProvider,
            List<JpaRepository<?, ?>> repositoryList,
            JpaFieldService jpaFieldService,
            JpaFieldAnnotationRegistryService jpaFieldAnnotationRegistryService
    ) {
        // 1. Create DataStores
        Map<JpaRepository<?, ?>, JpaRepositoryDataStore<?>> repoDataStoreMap = new HashMap<>();
        Map<Class<?>, JpaRepository<?, ?>> modelRepositoryMap = new HashMap<>();

        for (JpaRepository<?, ?> repository : repositoryList) {
            JpaRepositoryDataStore<?> dataStore = new JpaRepositoryDataStore<>(repository, jpaFieldAnnotationRegistryService, new DataStoreHooks<>());
            repoDataStoreMap.put(repository, dataStore);
            modelRepositoryMap.put(dataStore.getModelClass(), repository);
        }

        Application<JpaRepository<?, ?>, String, JpaRepository<?, ?>> providedConfig = configurationProvider.get();

        Map<JpaRepository<?, ?>, DataStoreConfig<JpaRepository<?, ?>, String, JpaRepository<?, ?>>> dataStores = new HashMap<>();
        for (Map.Entry<JpaRepository<?, ?>, JpaRepositoryDataStore<?>> entry : repoDataStoreMap.entrySet()) {
            JpaRepository<?, ?> repo = entry.getKey();
            JpaRepositoryDataStore<?> store = entry.getValue();
            Map<String, Field<JpaRepository<?, ?>, String, JpaRepository<?, ?>>> fields = jpaFieldService.getFieldsForDataStore(store, modelRepositoryMap);

            DataStoreConfig<JpaRepository<?, ?>, String, JpaRepository<?, ?>> config = DataStoreConfig.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder()
                    .factory(repo)
                    .dataStoreInstance((VortexCrudDataStore) store)
                    .fields(fields)
                    .build();
            dataStores.put(repo, config);
        }

        // 4. Build Application
        if (providedConfig.dataStores() != null) {
            dataStores.putAll(providedConfig.dataStores());
        }

        this.configuration = providedConfig.toBuilder()
                .dataStores(dataStores)
                .build();
    }

    @Override
    public Application<JpaRepository<?, ?>, String, JpaRepository<?, ?>> configuration() {
        return configuration;
    }

    @Override
    public String applicationName() {
        return configuration.applicationName();
    }

}
