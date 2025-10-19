package com.github.appreciated.vortex_crud.jpa.service.datastore;

import com.github.appreciated.vortex_crud.core.config.model.ImageFieldRendererConfiguration;
import com.github.appreciated.vortex_crud.core.file_provider.VortexCrudResourceProvider;
import com.github.appreciated.vortex_crud.jpa.service.JpaFieldAnnoationRegistryService;
import com.github.appreciated.vortex_crud.jpa.service.config.JpaRepositoryDataStore;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service for processing fields from JpaRepositoryDataStore and converting them to {@link com.github.appreciated.vortex_crud.core.config.model.Field} instances
 * based on their Vortex CRUD JPA annotations.
 */
@Service
public class JpaFieldService {

    private final JpaFieldTypeResolverService fieldTypeResolver;
    private final JpaFieldAnnoationRegistryService jpaFieldAnnoationRegistryService;

    public JpaFieldService(JpaFieldTypeResolverService fieldTypeResolver, JpaFieldAnnoationRegistryService jpaFieldAnnoationRegistryService) {
        this.fieldTypeResolver = fieldTypeResolver;
        this.jpaFieldAnnoationRegistryService = jpaFieldAnnoationRegistryService;
    }

    private <T extends Annotation> Optional<T> getAnnotation(Field field, Class<T> annotationClass) {
        if (field.isAnnotationPresent(annotationClass)) {
            return Optional.of(field.getAnnotation(annotationClass));
        }
        return Optional.empty();
    }

    /**
     * Extracts and processes fields from a JpaRepositoryDataStore
     *
     * @param dataStore The data store containing fields to process
     * @return A map of field names to configure Field objects
     */
    public Map<String, com.github.appreciated.vortex_crud.core.config.model.Field<JpaRepository<?, ?>, String, JpaRepository<?, ?>>> getFieldsForDataStore(JpaRepositoryDataStore<?> dataStore, JpaDataStoreFactoryRegistry jpaDataStoreFactoryRegistry) {
        return dataStore.getFields().stream()
                .filter(jpaFieldAnnoationRegistryService::hasFieldAnnotation)
                .collect(Collectors.toMap(java.lang.reflect.Field::getName, entityField -> {
                    boolean isPrimary = entityField.isAnnotationPresent(Id.class);
                    boolean isNullable = !entityField.isAnnotationPresent(Column.class) || entityField.getAnnotation(Column.class).nullable();
                    boolean required = !isNullable && !isPrimary;

                    return getAnnotation(entityField, com.github.appreciated.vortex_crud.jpa.service.annoations.ReferenceField.class).map(referenceField -> {
                        Class<?> targetEntityClass = fieldTypeResolver.resolveTargetClass(dataStore, entityField);
                        JpaRepository<?, ?> repository = jpaDataStoreFactoryRegistry.getFactory(targetEntityClass);
                        java.util.List<String> children = java.util.Arrays.asList(referenceField.fields());
                        String filterField = referenceField.value();
                        return (com.github.appreciated.vortex_crud.core.config.model.Field<JpaRepository<?, ?>, String, JpaRepository<?, ?>>) new com.github.appreciated.vortex_crud.core.config.model.fields.ReferenceField<JpaRepository<?, ?>, String, JpaRepository<?, ?>>(repository, filterField, children, required, null);
                    }).or(() -> getAnnotation(entityField, com.github.appreciated.vortex_crud.jpa.service.annoations.ImageField.class).map(imageField -> {
                        Class<? extends VortexCrudResourceProvider> provider = java.util.Optional.ofNullable(entityField.getAnnotation(com.github.appreciated.vortex_crud.jpa.service.annoations.ImageFieldConfiguration.class))
                                .map(com.github.appreciated.vortex_crud.jpa.service.annoations.ImageFieldConfiguration::value)
                                .orElse(null);
                        ImageFieldRendererConfiguration<JpaRepository<?, ?>, String, JpaRepository<?, ?>> cfg = new ImageFieldRendererConfiguration<>(provider);
                        return (com.github.appreciated.vortex_crud.core.config.model.Field<JpaRepository<?, ?>, String, JpaRepository<?, ?>>) new com.github.appreciated.vortex_crud.core.config.model.fields.ImageField<>(cfg, required, null);
                    })).or(() -> getAnnotation(entityField, com.github.appreciated.vortex_crud.jpa.service.annoations.SelectField.class).map(selectField ->
                            (com.github.appreciated.vortex_crud.core.config.model.Field<JpaRepository<?, ?>, String, JpaRepository<?, ?>>) new com.github.appreciated.vortex_crud.core.config.model.fields.SelectField<JpaRepository<?, ?>, String, JpaRepository<?, ?>>(selectField.value(), required, null)
                    )).or(() -> getAnnotation(entityField, com.github.appreciated.vortex_crud.jpa.service.annoations.BigDecimalNumberField.class).map(ann ->
                            (com.github.appreciated.vortex_crud.core.config.model.Field<JpaRepository<?, ?>, String, JpaRepository<?, ?>>) new com.github.appreciated.vortex_crud.core.config.model.fields.BigDecimalField<JpaRepository<?, ?>, String, JpaRepository<?, ?>>(required, null)
                    )).or(() -> getAnnotation(entityField, com.github.appreciated.vortex_crud.jpa.service.annoations.CheckboxField.class).map(ann ->
                            (com.github.appreciated.vortex_crud.core.config.model.Field<JpaRepository<?, ?>, String, JpaRepository<?, ?>>) new com.github.appreciated.vortex_crud.core.config.model.fields.CheckboxField<JpaRepository<?, ?>, String, JpaRepository<?, ?>>(required, null)
                    )).or(() -> getAnnotation(entityField, com.github.appreciated.vortex_crud.jpa.service.annoations.DateField.class).map(ann ->
                            (com.github.appreciated.vortex_crud.core.config.model.Field<JpaRepository<?, ?>, String, JpaRepository<?, ?>>) new com.github.appreciated.vortex_crud.core.config.model.fields.DateField<JpaRepository<?, ?>, String, JpaRepository<?, ?>>(required, null)
                    )).or(() -> getAnnotation(entityField, com.github.appreciated.vortex_crud.jpa.service.annoations.DateTimePickerField.class).map(ann ->
                            (com.github.appreciated.vortex_crud.core.config.model.Field<JpaRepository<?, ?>, String, JpaRepository<?, ?>>) new com.github.appreciated.vortex_crud.core.config.model.fields.DateTimePickerField<JpaRepository<?, ?>, String, JpaRepository<?, ?>>(required, null)
                    )).or(() -> getAnnotation(entityField, com.github.appreciated.vortex_crud.jpa.service.annoations.DoubleNumberField.class).map(ann ->
                            (com.github.appreciated.vortex_crud.core.config.model.Field<JpaRepository<?, ?>, String, JpaRepository<?, ?>>) new com.github.appreciated.vortex_crud.core.config.model.fields.DoubleField<JpaRepository<?, ?>, String, JpaRepository<?, ?>>(required, null)
                    )).or(() -> getAnnotation(entityField, com.github.appreciated.vortex_crud.jpa.service.annoations.IdField.class).map(ann ->
                            (com.github.appreciated.vortex_crud.core.config.model.Field<JpaRepository<?, ?>, String, JpaRepository<?, ?>>) new com.github.appreciated.vortex_crud.core.config.model.fields.IdField<JpaRepository<?, ?>, String, JpaRepository<?, ?>>(required, null)
                    )).or(() -> getAnnotation(entityField, com.github.appreciated.vortex_crud.jpa.service.annoations.IntegerNumberField.class).map(ann ->
                            (com.github.appreciated.vortex_crud.core.config.model.Field<JpaRepository<?, ?>, String, JpaRepository<?, ?>>) new com.github.appreciated.vortex_crud.core.config.model.fields.IntegerField<JpaRepository<?, ?>, String, JpaRepository<?, ?>>(required, null)
                    )).or(() -> getAnnotation(entityField, com.github.appreciated.vortex_crud.jpa.service.annoations.TextAreaField.class).map(ann ->
                            (com.github.appreciated.vortex_crud.core.config.model.Field<JpaRepository<?, ?>, String, JpaRepository<?, ?>>) new com.github.appreciated.vortex_crud.core.config.model.fields.TextAreaField<JpaRepository<?, ?>, String, JpaRepository<?, ?>>(required, null)
                    )).or(() -> getAnnotation(entityField, com.github.appreciated.vortex_crud.jpa.service.annoations.TextField.class).map(ann ->
                            (com.github.appreciated.vortex_crud.core.config.model.Field<JpaRepository<?, ?>, String, JpaRepository<?, ?>>) new com.github.appreciated.vortex_crud.core.config.model.fields.TextField<JpaRepository<?, ?>, String, JpaRepository<?, ?>>(required, null)
                    )).or(() -> getAnnotation(entityField, com.github.appreciated.vortex_crud.jpa.service.annoations.VideoField.class).map(ann -> {
                            com.github.appreciated.vortex_crud.core.config.model.RouteRendererConfiguration<JpaRepository<?, ?>, String, JpaRepository<?, ?>> cfg = new com.github.appreciated.vortex_crud.core.config.model.RouteRendererConfiguration<>(null);
                            return (com.github.appreciated.vortex_crud.core.config.model.Field<JpaRepository<?, ?>, String, JpaRepository<?, ?>>) new com.github.appreciated.vortex_crud.core.config.model.fields.VideoField<>(cfg, required, null);
                    })).orElseThrow();
                }));
    }

}