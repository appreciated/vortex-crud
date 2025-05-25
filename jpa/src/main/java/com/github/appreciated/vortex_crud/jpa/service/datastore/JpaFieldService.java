package com.github.appreciated.vortex_crud.jpa.service.datastore;

import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.github.appreciated.vortex_crud.core.file_provider.VortexCrudResourceProvider;
import com.github.appreciated.vortex_crud.jpa.service.FieldRenderer;
import com.github.appreciated.vortex_crud.jpa.service.ImageFieldRendererConfiguration;
import com.github.appreciated.vortex_crud.jpa.service.SelectValues;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service for processing fields from JpaRepositoryDataStore and converting them to Field objects
 * with appropriate configurations based on annotations.
 */
@Service
public class JpaFieldService {

    /**
     * Extracts and processes fields from a JpaRepositoryDataStore
     *
     * @param dataStore The data store containing fields to process
     * @return A map of field names to configure Field objects
     */
    public Map<String, Field<JpaRepository<?, ?>, String>> getFieldsForDataStore(JpaRepositoryDataStore<?> dataStore) {
        return dataStore.getFields().stream()
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