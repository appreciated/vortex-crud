package com.github.appreciated.vortex_crud.jpa.service.datastore;

import com.github.appreciated.vortex_crud.core.config.model.DataStoreConfig;
import com.github.appreciated.vortex_crud.core.config.model.DataStoreHooks;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFactoryRegistry;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.DefaultFieldFactoryRegistry;
import com.github.appreciated.vortex_crud.jpa.service.JpaFieldAnnotationRegistryService;
import com.github.appreciated.vortex_crud.jpa.service.config.JpaRepositoryDataStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Factory implementation for creating entity item renderers.
 * It initializes and provides the appropriate renderer based on the ItemRendererConfig configuration.
 */

@Service
public class JpaDataStoreFactoryRegistry implements VortexCrudDataStoreFactoryRegistry<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {

    private final HashMap<JpaRepository<?, ?>, VortexCrudDataStore<String, JpaRepository<?, ?>>> factories = new HashMap<>();
    private final HashMap<Class<?>, JpaRepository<?, ?>> modelFactoryMapping = new HashMap<>();
    private final JpaFieldService jpaFieldService;

    public JpaDataStoreFactoryRegistry(List<JpaRepository<?, ?>> repositoryList,
                                       JpaFieldService jpaFieldService,
                                       JpaFieldAnnotationRegistryService jpaFieldAnnotationRegistryService) {
        this.jpaFieldService = jpaFieldService;
        repositoryList.forEach(repository -> addFactory(repository, new JpaRepositoryDataStore(repository, jpaFieldAnnotationRegistryService, new DataStoreHooks<>())));
    }

    @Override
    public VortexCrudDataStore<String, JpaRepository<?, ?>> getDataStore(JpaRepository<?, ?> table) {
        return Optional.ofNullable(
                        factories.get(table))
                .orElseThrow(() -> new IllegalStateException("%s cannot provide factory for key '%s'".formatted(DefaultFieldFactoryRegistry.class.getName(), table)));
    }

    @Override
    public void addFactory(JpaRepository<?, ?> table, VortexCrudDataStore<String, JpaRepository<?, ?>> factory) {
        factories.put(table, factory);
        modelFactoryMapping.put(factory.getModelClass(), table);
    }

    public Map<JpaRepository<?, ?>, DataStoreConfig<JpaRepository<?, ?>, String, JpaRepository<?, ?>>> dataStores() {
        return factories.entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> DataStoreConfig.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder()
                        .factory(entry.getKey())
                        .fields(jpaFieldService.getFieldsForDataStore((JpaRepositoryDataStore<?>) entry.getValue(), this))
                        .build()
        ));
    }

    public JpaRepository<?, ?> getFactory(Class<?> model) {
        return Optional.ofNullable(modelFactoryMapping.get(model)).orElseThrow(() -> new IllegalStateException("%s cannot provide factory for key '%s'".formatted(DefaultFieldFactoryRegistry.class.getName(), model)));
    }
}
