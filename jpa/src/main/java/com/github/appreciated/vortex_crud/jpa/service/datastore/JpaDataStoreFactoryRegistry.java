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
public class JpaDataStoreFactoryRegistry implements VortexCrudDataStoreFactoryRegistry<JpaRepository<?, ?>, String> {

    private final HashMap<Class<? extends JpaRepository<?, ?>>, JpaRepositoryDataStore<String>> factories = new HashMap<>();
    private final HashMap<Class<?>, Class<? extends JpaRepository<?, ?>>> modelFactoryMapping = new HashMap<>();
    private final JpaFieldService jpaFieldService;

    public JpaDataStoreFactoryRegistry(List<JpaRepository<?, ?>> repositoryList,
                                       JpaFieldService jpaFieldService,
                                       JpaFieldTypeResolverService fieldTypeResolverService) {
        this.jpaFieldService = jpaFieldService;
        repositoryList.forEach(repository -> addFactory((Class<? extends JpaRepository<?, ?>>) repository.getClass(), new JpaRepositoryDataStore<>(repository, fieldTypeResolverService)));
    }

    public VortexCrudDataStore<String, ?> getDataStore(JpaRepository<?, ?> table) {
        return Optional.ofNullable(factories.get(table)).orElseThrow(() -> new IllegalStateException("%s cannot provide factory for key '%s'".formatted(DefaultFieldFactoryRegistry.class.getName(), table)));
    }

    @Override
    public VortexCrudDataStore<String, ?> getDataStore(Class<? extends JpaRepository<?, ?>> table) {
        return factories.get(getFactory(table));
    }

    @Override
    public void addFactory(Class<? extends JpaRepository<?, ?>> key, VortexCrudDataStore<String, ?> factory) {
        factories.put(key, (JpaRepositoryDataStore<String>) factory);
        modelFactoryMapping.put(((JpaRepositoryDataStore<?>) factory).getModelClass(), key);
    }

    public Map<Class<? extends JpaRepository<?, ?>>, DataStoreConfig<JpaRepository<?, ?>, String>> getDataStores() {
        return factories.entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getKey,
                test -> {
                    DataStoreConfig.Builder<JpaRepository<?, ?>, String> builder = createBuilder(test);
                    builder.withFields(jpaFieldService.getFieldsForDataStore(test.getValue(), this));
                    return builder.build();
                }));
    }

    private DataStoreConfig.Builder<JpaRepository<?, ?>, String> createBuilder(Map.Entry<Class<? extends JpaRepository<?, ?>>, JpaRepositoryDataStore<String>> test) {
        return new DataStoreConfig.Builder<>(new DataStoreConfig<>((Class<? extends VortexCrudDataStore<String, ?>>) test.getValue().getClass()));
    }

    public Class<? extends JpaRepository<?, ?>> getFactory(Class<?> model) {
        return Optional.ofNullable(modelFactoryMapping.get(model)).orElseThrow(() -> new IllegalStateException("%s cannot provide factory for key '%s'".formatted(DefaultFieldFactoryRegistry.class.getName(), model)));
    }
}