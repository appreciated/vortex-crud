package com.github.appreciated.vortex_crud.jpa.service.datastore;

import com.github.appreciated.vortex_crud.core.config.model.DataStoreConfig;
import com.github.appreciated.vortex_crud.core.config.model.DataStoreHooks;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFactoryRegistry;
import com.github.appreciated.vortex_crud.jpa.service.JpaFieldAnnotationRegistryService;
import com.github.appreciated.vortex_crud.jpa.service.config.JpaRepositoryDataStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JpaDataStoreFactoryRegistry implements VortexCrudDataStoreFactoryRegistry<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {

    private final HashMap<JpaRepository<?, ?>, VortexCrudDataStore<String, JpaRepository<?, ?>>> factories = new HashMap<>();
    private final HashMap<Class<?>, JpaRepository<?, ?>> modelFactoryMapping = new HashMap<>();
    private final JpaFieldService jpaFieldService;

    public JpaDataStoreFactoryRegistry(JpaFieldService jpaFieldService) {
        this.jpaFieldService = jpaFieldService;
    }

    @Override
    public VortexCrudDataStore<String, JpaRepository<?, ?>> getDataStore(JpaRepository<?, ?> table) {
        return Optional.ofNullable(factories.get(table))
                .orElseThrow(() -> new IllegalStateException("Cannot provide factory for key " + table));
    }

    @Override
    public void addFactory(JpaRepository<?, ?> table, VortexCrudDataStore<String, JpaRepository<?, ?>> factory) {
        factories.put(table, factory);
        modelFactoryMapping.put(((JpaRepositoryDataStore<?>) factory).getModelClass(), table);
    }

    public Map<JpaRepository<?, ?>, DataStoreConfig<JpaRepository<?, ?>, String, JpaRepository<?, ?>>> dataStores() {
        return factories.entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> DataStoreConfig.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder()
                        .factory(entry.getKey())
                        .dataStoreInstance((VortexCrudDataStore)entry.getValue())
                        .fields(jpaFieldService.getFieldsForDataStore((JpaRepositoryDataStore<?>) entry.getValue(), modelFactoryMapping))
                        .build()
        ));
    }

    public JpaRepository<?, ?> getFactory(Class<?> model) {
        return Optional.ofNullable(modelFactoryMapping.get(model)).orElseThrow(() -> new IllegalStateException("Cannot provide factory for key " + model));
    }
}
