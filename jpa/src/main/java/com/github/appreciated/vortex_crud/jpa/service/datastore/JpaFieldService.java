package com.github.appreciated.vortex_crud.jpa.service.datastore;

import com.github.appreciated.vortex_crud.core.file_provider.VortexCrudResourceProvider;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.ReferenceFieldFactory;
import com.github.appreciated.vortex_crud.jpa.service.Field;
import com.github.appreciated.vortex_crud.jpa.service.ImageFieldConfiguration;
import com.github.appreciated.vortex_crud.jpa.service.SelectValues;
import jakarta.persistence.Column;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Id;
import jakarta.persistence.metamodel.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static jakarta.persistence.metamodel.Attribute.PersistentAttributeType.*;

/**
 * Service for processing fields from JpaRepositoryDataStore and converting them to Field objects
 * with appropriate configurations based on annotations.
 */
@Service
public class JpaFieldService {

    /**
     * Extracts and processes fields from a JpaRepositoryDataStore
     *
     * @param dataStore                   The data store containing fields to process
     * @param jpaDataStoreFactoryRegistry
     * @return A map of field names to configure Field objects
     */
    public Map<String, com.github.appreciated.vortex_crud.core.config.model.Field<JpaRepository<?, ?>, String>> getFieldsForDataStore(JpaRepositoryDataStore<?> dataStore, EntityManager entityManager, JpaDataStoreFactoryRegistry jpaDataStoreFactoryRegistry) {
        return dataStore.getFields().stream()
                .filter(field -> field.isAnnotationPresent(Field.class))
                .collect(Collectors.toMap(java.lang.reflect.Field::getName, entityField -> {
                    Field annotation = entityField.getAnnotation(Field.class);
                    boolean isPrimary = entityField.isAnnotationPresent(Id.class);
                    boolean isNullable = !entityField.isAnnotationPresent(Column.class) || entityField.getAnnotation(Column.class).nullable();

                    if (annotation.value() == ReferenceFieldFactory.class) {
                        Metamodel metamodel = entityManager.getMetamodel();
                        Class<?> model = dataStore.getModel();
                        EntityType<?> entityType = metamodel.entity(model);
                        Attribute<?, ?> attribute = entityType.getAttribute(entityField.getName());
                        Attribute.PersistentAttributeType type = attribute.getPersistentAttributeType();

                        if ((type == MANY_TO_MANY || type == ONE_TO_MANY || type == MANY_TO_ONE)) {
                            if (attribute instanceof PluralAttribute<?, ?, ?> pluralAttribute){
                                Class<?> targetEntityClass = pluralAttribute.getElementType().getJavaType();
                                return JpaField.of(annotation.value(), null, null, jpaDataStoreFactoryRegistry.getFactory(targetEntityClass), List.of()).build();
                            } else  if (attribute instanceof SingularAttribute<?, ?> singularAttribute){
                                Class<?> targetEntityClass = singularAttribute.getJavaType();
                                return JpaField.of(annotation.value(), null, null, jpaDataStoreFactoryRegistry.getFactory(targetEntityClass), List.of()).build();
                            }
                           } else {
                            throw new IllegalStateException("Field '%s' of type '%s' is annotated with ReferenceFieldFactory but is not a ManyToMany or OneToMany relationship".formatted(entityField.getName(), model.getSimpleName()));
                        }
                    }
                    if (entityField.isAnnotationPresent(ImageFieldConfiguration.class)) {
                        Class<? extends VortexCrudResourceProvider> imageFieldConfiguration = entityField.getAnnotation(ImageFieldConfiguration.class).value();
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