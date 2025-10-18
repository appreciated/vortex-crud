package com.github.appreciated.vortex_crud.jpa.service.datastore;

import com.github.appreciated.vortex_crud.core.config.model.ImageFieldRendererConfiguration;
import com.github.appreciated.vortex_crud.core.file_provider.VortexCrudResourceProvider;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.ReferenceFieldFactory;
import com.github.appreciated.vortex_crud.jpa.service.ImageFieldConfiguration;
import com.github.appreciated.vortex_crud.jpa.service.ReferenceFieldConfiguration;
import com.github.appreciated.vortex_crud.jpa.service.SelectValues;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaField;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service for processing fields from JpaRepositoryDataStore and converting them to {@link com.github.appreciated.vortex_crud.core.config.model.Field} instances
 * based on their Vortex CRUD JPA annotations.
 */
@Service
public class JpaFieldService {

    private final JpaFieldTypeResolverService fieldTypeResolver;

    public JpaFieldService(JpaFieldTypeResolverService fieldTypeResolver) {
        this.fieldTypeResolver = fieldTypeResolver;
    }

    /**
     * Extracts and processes fields from a JpaRepositoryDataStore
     *
     * @param dataStore                   The data store containing fields to process
     * @param jpaDataStoreFactoryRegistry
     * @return A map of field names to configure Field objects
     */
    public Map<String, com.github.appreciated.vortex_crud.core.config.model.Field<JpaRepository<?, ?>, String, JpaRepository<?, ?>>> getFieldsForDataStore(JpaRepositoryDataStore<?> dataStore, JpaDataStoreFactoryRegistry jpaDataStoreFactoryRegistry) {
        return dataStore.getFields().stream()
                .filter(field -> field.isAnnotationPresent(com.github.appreciated.vortex_crud.core.config.model.annotations.Field.class))
                .collect(Collectors.toMap(java.lang.reflect.Field::getName, entityField -> {
                    com.github.appreciated.vortex_crud.core.config.model.annotations.Field annotation = entityField.getAnnotation(com.github.appreciated.vortex_crud.core.config.model.annotations.Field.class);
                    boolean isPrimary = entityField.isAnnotationPresent(jakarta.persistence.Id.class);
                    boolean isNullable = !entityField.isAnnotationPresent(jakarta.persistence.Column.class) || entityField.getAnnotation(jakarta.persistence.Column.class).nullable();

                    if (annotation.value() == ReferenceFieldFactory.class) {
                        Class<?> targetEntityClass = fieldTypeResolver.resolveTargetClass(dataStore, entityField);
                        JpaRepository<?, ?> fieldEntityFactory = jpaDataStoreFactoryRegistry.getFactory(targetEntityClass);
                        String filterField = entityField.isAnnotationPresent(ReferenceFieldConfiguration.class) ? entityField.getAnnotation(ReferenceFieldConfiguration.class).value() : null;
                        String[] filterFields = entityField.isAnnotationPresent(ReferenceFieldConfiguration.class) && entityField.getAnnotation(ReferenceFieldConfiguration.class).fields().length > 0 ? entityField.getAnnotation(ReferenceFieldConfiguration.class).fields() : null;
                        return new JpaField(annotation.value(), !isNullable, null);
                    }
                    if (entityField.isAnnotationPresent(ImageFieldConfiguration.class)) {
                        Class<? extends VortexCrudResourceProvider> imageFieldConfiguration = entityField.getAnnotation(ImageFieldConfiguration.class).value();
                        return new JpaField(annotation.value(), !isNullable, null);
                    } else if (entityField.isAnnotationPresent(SelectValues.class)) {
                        // If field is a select
                        String jpaSelectValue = entityField.getAnnotation(SelectValues.class).value();
                        return new JpaField(annotation.value(), !isNullable, null);
                    } else {
                        // Otherwise it is a field that can use the basic field initialization
                        return new JpaField(annotation.value(), !isNullable, null);
                    }
                }));
    }

}