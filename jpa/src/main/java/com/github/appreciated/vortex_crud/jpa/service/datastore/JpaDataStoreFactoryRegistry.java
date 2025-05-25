package com.github.appreciated.vortex_crud.jpa.service.datastore;

import com.github.appreciated.vortex_crud.core.config.model.DataStoreConfig;
import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFactoryRegistry;
import com.github.appreciated.vortex_crud.core.file_provider.VortexCrudResourceProvider;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.DefaultFieldFactoryRegistry;
import com.github.appreciated.vortex_crud.jpa.service.FieldRenderer;
import com.github.appreciated.vortex_crud.jpa.service.ImageFieldRendererConfiguration;
import com.github.appreciated.vortex_crud.jpa.service.SelectValues;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Factory implementation for creating entity item renderers.
 * It initializes and provides the appropriate renderer based on the ItemRendererConfig configuration.
 */

@Service
public class JpaDataStoreFactoryRegistry implements VortexCrudDataStoreFactoryRegistry<JpaRepository<?, ?>, String> {

    private final HashMap<JpaRepository<?, ?>, JpaRepositoryDataStore<String>> factories = new HashMap<>();

    public JpaDataStoreFactoryRegistry(@Autowired List<JpaRepository<?, ?>> repositoryList) {
        repositoryList.forEach(repository -> factories.put(repository, new JpaRepositoryDataStore(repository)));
    }

    public VortexCrudDataStore<String> getFactory(JpaRepository<?, ?> table) {
        return Optional.ofNullable(factories.get(table)).orElseThrow(() -> new IllegalStateException("%s cannot provide factory for key '%s'".formatted(DefaultFieldFactoryRegistry.class.getName(), table)));
    }

    @Override
    public void addFactory(JpaRepository<?, ?> key, VortexCrudDataStore<String> factory) {
        factories.put(key, (JpaRepositoryDataStore<String>) factory);
    }

    public Map<JpaRepository<?, ?>, DataStoreConfig<JpaRepository<?, ?>, String>> getDataStores() {
        return factories.entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getKey,
                test -> {
                    DataStoreConfig.Builder<JpaRepository<?, ?>, String> builder = createBuilder(test);
                    builder.withFields(getFieldsForJpaRepository(test.getValue()));
                    return builder.build();
                }));
    }

    private DataStoreConfig.Builder<JpaRepository<?, ?>, String> createBuilder(Map.Entry<JpaRepository<?, ?>, JpaRepositoryDataStore<String>> test) {
        return new DataStoreConfig.Builder<>(new DataStoreConfig<>((Class<? extends VortexCrudDataStore<String>>) test.getValue().getClass()));
    }

    public Map<String, Field<JpaRepository<?, ?>, String>> getFieldsForJpaRepository(JpaRepositoryDataStore<?> dataStore) {
        return Arrays.stream(dataStore.getFields())
                .filter(field -> field.isAnnotationPresent(FieldRenderer.class))
                .collect(Collectors.toMap(java.lang.reflect.Field::getName, entityField -> {
                    FieldRenderer annotation = entityField.getAnnotation(FieldRenderer.class);
                    boolean isPrimary = entityField.isAnnotationPresent(Id.class);
                    boolean isNullable = !entityField.isAnnotationPresent(Column.class) || entityField.getAnnotation(Column.class).nullable();

                    if (entityField.isAnnotationPresent(ImageFieldRendererConfiguration.class)) {
                        Class<? extends VortexCrudResourceProvider> imageFieldConfiguration = entityField.getAnnotation(ImageFieldRendererConfiguration.class).value();
                        return JpaField.of(annotation.value(), isPrimary, isNullable)
                                .withConfiguration(new com.github.appreciated.vortex_crud.core.config.model.ImageFieldRendererConfiguration<>(imageFieldConfiguration))
                                .build();
                    } else if (entityField.isAnnotationPresent(SelectValues.class)) {
                        // If field is a select
                        String jpaSelectValue = entityField.getAnnotation(SelectValues.class).value();
                        return JpaField.of(annotation.value(), jpaSelectValue).build();
                    } else {
                        // Otherwise it is a field that can use the basic field initialization
                        return JpaField.of(annotation.value(), isPrimary, isNullable).build();
                    }
                }));
    }
}
