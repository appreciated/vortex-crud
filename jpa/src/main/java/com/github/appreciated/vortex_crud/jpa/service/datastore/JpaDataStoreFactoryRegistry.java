package com.github.appreciated.vortex_crud.jpa.service.datastore;

import com.github.appreciated.vortex_crud.core.config.model.DataStoreConfig;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFactoryRegistry;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.DefaultFieldFactoryRegistry;
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

    private final HashMap<JpaRepository<?, ?>, JpaRepositoryDataStore<String>> factories = new HashMap<>();
    private final HashMap<Class<?>, JpaRepository<?, ?>> modelFactoryMapping = new HashMap<>();
    private final JpaFieldService jpaFieldService;

    public JpaDataStoreFactoryRegistry(List<JpaRepository<?, ?>> repositoryList,
                                       JpaFieldService jpaFieldService,
                                       JpaFieldTypeResolverService fieldTypeResolverService) {
        this.jpaFieldService = jpaFieldService;
        repositoryList.forEach(repository -> addFactory(repository, new JpaRepositoryDataStore<>(repository, fieldTypeResolverService)));
    }

    @Override
    public VortexCrudDataStore<String, ?> getDataStore(JpaRepository<?, ?> table) {
        return Optional.ofNullable(factories.get(table)).orElseThrow(() -> new IllegalStateException("%s cannot provide factory for key '%s'".formatted(DefaultFieldFactoryRegistry.class.getName(), table)));
    }

    @Override
    public void addFactory(JpaRepository<?, ?> key, VortexCrudDataStore<String, ?> factory) {
        factories.put(key, (JpaRepositoryDataStore<String>) factory);
        modelFactoryMapping.put(((JpaRepositoryDataStore<?>) factory).getModelClass(), key);
    }

    public Map<JpaRepository<?, ?>, DataStoreConfig<JpaRepository<?, ?>, String, JpaRepository<?, ?>>> getDataStores() {
        return factories.entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getKey,
                test -> {
                    DataStoreConfig.Builder<JpaRepository<?, ?>, String, JpaRepository<?, ?>> builder = createBuilder(test.getKey());
                    builder.withFields(jpaFieldService.getFieldsForDataStore(test.getValue(), this));
                    return builder.build();
                }));
    }

    private DataStoreConfig.Builder<JpaRepository<?, ?>, String, JpaRepository<?, ?>> createBuilder(Map.Entry<JpaRepository<?, ?>, JpaRepositoryDataStore<String>> test) {
        return new DataStoreConfig.Builder<>(new DataStoreConfig<>(test.getValue()));
    }

    public JpaRepository<?, ?> getFactory(Class<?> model) {
        return Optional.ofNullable(modelFactoryMapping.get(model)).orElseThrow(() -> new IllegalStateException("%s cannot provide factory for key '%s'".formatted(DefaultFieldFactoryRegistry.class.getName(), model)));
    }
}
