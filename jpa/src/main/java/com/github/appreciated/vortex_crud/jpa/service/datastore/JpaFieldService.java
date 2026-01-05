package com.github.appreciated.vortex_crud.jpa.service.datastore;

import com.github.appreciated.vortex_crud.core.config.model.fields.BigDecimalField;
import com.github.appreciated.vortex_crud.core.config.model.fields.DoubleField;
import com.github.appreciated.vortex_crud.core.config.model.fields.IntegerField;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.file_provider.VortexCrudResourceProvider;
import com.github.appreciated.vortex_crud.jpa.service.JpaFieldAnnotationRegistryService;
import com.github.appreciated.vortex_crud.jpa.service.annoations.*;
import com.github.appreciated.vortex_crud.jpa.service.config.JpaRepositoryDataStore;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for processing fields from JpaRepositoryDataStore and converting them to {@link com.github.appreciated.vortex_crud.core.config.model.Field} instances
 * based on their Vortex CRUD JPA annotations.
 */
@Service
public class JpaFieldService {

    private final JpaFieldTypeResolverService fieldTypeResolver;
    private final JpaFieldAnnotationRegistryService jpaFieldAnnotationRegistryService;

    public JpaFieldService(JpaFieldTypeResolverService fieldTypeResolver, JpaFieldAnnotationRegistryService jpaFieldAnnotationRegistryService) {
        this.fieldTypeResolver = fieldTypeResolver;
        this.jpaFieldAnnotationRegistryService = jpaFieldAnnotationRegistryService;
    }

    private <T extends Annotation> Optional<T> getAnnotation(Field field, Class<T> annotationClass) {
        if (field.isAnnotationPresent(annotationClass)) {
            return Optional.of(field.getAnnotation(annotationClass));
        }
        return Optional.empty();
    }

    private VortexCrudResourceProvider instantiateProvider(Class<? extends VortexCrudResourceProvider> providerClass) {
        try {
            Constructor<? extends VortexCrudResourceProvider> constructor = providerClass.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Failed to instantiate resource provider: " + providerClass.getName(), e);
        }
    }

    /**
     * Extracts and processes fields from a JpaRepositoryDataStore
     *
     * @param dataStore The data store containing fields to process
     * @return A map of field names to configure Field objects
     */
    public Map<String, com.github.appreciated.vortex_crud.core.config.model.Field<JpaRepository<?, ?>, String, JpaRepository<?, ?>>> getFieldsForDataStore(JpaRepositoryDataStore<?> dataStore, Map<Class<?>, VortexCrudDataStore> storeMap) {
        Collection<Field> fields = dataStore.getFields();
        return fields.stream()
                .filter(jpaFieldAnnotationRegistryService::hasFieldAnnotation)
                .collect(Collectors.toMap(Field::getName, entityField -> {
                    boolean isPrimary = entityField.isAnnotationPresent(Id.class);
                    boolean isNullable = !entityField.isAnnotationPresent(Column.class) || entityField.getAnnotation(Column.class).nullable();
                    boolean required = !isNullable && !isPrimary;

                    return getAnnotation(entityField, ReferenceField.class).map(referenceField -> {
                        Class<?> targetEntityClass = fieldTypeResolver.resolveTargetClass(dataStore, entityField);
                        VortexCrudDataStore store = storeMap.get(targetEntityClass);
                        if (store == null) {
                            throw new IllegalStateException("No store found for class " + targetEntityClass);
                        }
                        // If fields() is not specified, use value() as a single child
                        List<String> children = referenceField.fields().length > 0
                                ? Arrays.asList(referenceField.fields())
                                : (referenceField.value().isEmpty() ? List.of() : List.of(referenceField.value()));
                        String filterField = referenceField.value();
                        String fieldName = entityField.getName();
                        return (com.github.appreciated.vortex_crud.core.config.model.Field<JpaRepository<?, ?>, String, JpaRepository<?, ?>>) com.github.appreciated.vortex_crud.core.config.model.fields.ReferenceField
                                .<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder()
                                .dataStore(store)
                                .field(fieldName)
                                .filterField(filterField)
                                .children(children)
                                .required(required)
                                .build();
                    }).or(() -> getAnnotation(entityField, MultiSelectField.class).map(multiSelectField -> {
                        Class<?> targetEntityClass = fieldTypeResolver.resolveTargetClass(dataStore, entityField);
                        VortexCrudDataStore store = storeMap.get(targetEntityClass);
                        if (store == null) {
                            throw new IllegalStateException("No store found for class " + targetEntityClass);
                        }
                        // If fields() is not specified, use value() as a single child
                        List<String> children = multiSelectField.fields().length > 0
                                ? Arrays.asList(multiSelectField.fields())
                                : (multiSelectField.value().isEmpty() ? List.of() : List.of(multiSelectField.value()));
                        String filterField = multiSelectField.value();
                        String fieldName = entityField.getName();
                        return (com.github.appreciated.vortex_crud.core.config.model.Field<JpaRepository<?, ?>, String, JpaRepository<?, ?>>) com.github.appreciated.vortex_crud.core.config.model.fields.MultiSelectField
                                .<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder()
                                .dataStore(store)
                                .field(fieldName)
                                .filterField(filterField)
                                .children(children)
                                .required(required)
                                .build();
                    })).or(() -> getAnnotation(entityField, ImageField.class).map(imageField -> {
                        return (com.github.appreciated.vortex_crud.core.config.model.Field<JpaRepository<?, ?>, String, JpaRepository<?, ?>>) com.github.appreciated.vortex_crud.core.config.model.fields.ImageField
                                .<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder()
                                .resourceProvider(instantiateProvider(imageField.value()))
                                .required(required)
                                .build();
                    })).or(() -> getAnnotation(entityField, VideoField.class).map(videoField -> {
                        return (com.github.appreciated.vortex_crud.core.config.model.Field<JpaRepository<?, ?>, String, JpaRepository<?, ?>>) com.github.appreciated.vortex_crud.core.config.model.fields.VideoField
                                .<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder()
                                .resourceProvider(instantiateProvider(videoField.value()))
                                .required(required)
                                .build();
                    })).or(() -> getAnnotation(entityField, PdfField.class).map(pdfField -> {
                        return (com.github.appreciated.vortex_crud.core.config.model.Field<JpaRepository<?, ?>, String, JpaRepository<?, ?>>) com.github.appreciated.vortex_crud.core.config.model.fields.PdfField
                                .<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder()
                                .resourceProvider(instantiateProvider(pdfField.value()))
                                .required(required)
                                .build();
                    })).or(() -> getAnnotation(entityField, FileField.class).map(fileField -> {
                        return (com.github.appreciated.vortex_crud.core.config.model.Field<JpaRepository<?, ?>, String, JpaRepository<?, ?>>) com.github.appreciated.vortex_crud.core.config.model.fields.FileField
                                .<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder()
                                .resourceProvider(instantiateProvider(fileField.value()))
                                .required(required)
                                .build();
                    })).or(() -> getAnnotation(entityField, SelectField.class).map(selectField ->
                            (com.github.appreciated.vortex_crud.core.config.model.Field<JpaRepository<?, ?>, String, JpaRepository<?, ?>>) com.github.appreciated.vortex_crud.core.config.model.fields.SelectField
                                    .<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder()
                                    .values(selectField.value())
                                    .required(required)
                                    .build()
                    )).or(() -> getAnnotation(entityField, MultiSelectValueField.class).map(multiSelectValueField ->
                            (com.github.appreciated.vortex_crud.core.config.model.Field<JpaRepository<?, ?>, String, JpaRepository<?, ?>>) com.github.appreciated.vortex_crud.core.config.model.fields.MultiSelectValueField
                                    .<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder()
                                    .values(multiSelectValueField.value())
                                    .required(required)
                                    .build()
                    )).or(() -> getAnnotation(entityField, BigDecimalNumberField.class).map(ann ->
                            (com.github.appreciated.vortex_crud.core.config.model.Field<JpaRepository<?, ?>, String, JpaRepository<?, ?>>) BigDecimalField
                                    .<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder()
                                    .required(required)
                                    .build()
                    )).or(() -> getAnnotation(entityField, CheckboxField.class).map(ann ->
                            (com.github.appreciated.vortex_crud.core.config.model.Field<JpaRepository<?, ?>, String, JpaRepository<?, ?>>) com.github.appreciated.vortex_crud.core.config.model.fields.CheckboxField
                                    .<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder()
                                    .required(required)
                                    .build()
                    )).or(() -> getAnnotation(entityField, DateField.class).map(ann ->
                            (com.github.appreciated.vortex_crud.core.config.model.Field<JpaRepository<?, ?>, String, JpaRepository<?, ?>>) com.github.appreciated.vortex_crud.core.config.model.fields.DateField
                                    .<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder()
                                    .required(required)
                                    .build()
                    )).or(() -> getAnnotation(entityField, DateTimePickerField.class).map(ann ->
                            (com.github.appreciated.vortex_crud.core.config.model.Field<JpaRepository<?, ?>, String, JpaRepository<?, ?>>) com.github.appreciated.vortex_crud.core.config.model.fields.DateTimePickerField
                                    .<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder()
                                    .required(required)
                                    .build()
                    )).or(() -> getAnnotation(entityField, DoubleNumberField.class).map(ann ->
                            (com.github.appreciated.vortex_crud.core.config.model.Field<JpaRepository<?, ?>, String, JpaRepository<?, ?>>) DoubleField
                                    .<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder()
                                    .required(required)
                                    .build()
                    )).or(() -> getAnnotation(entityField, NumericIdField.class).map(ann ->
                            (com.github.appreciated.vortex_crud.core.config.model.Field<JpaRepository<?, ?>, String, JpaRepository<?, ?>>) com.github.appreciated.vortex_crud.core.config.model.fields.NumericIdField
                                    .<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder()
                                    .required(required)
                                    .build()
                    )).or(() -> getAnnotation(entityField, StringIdField.class).map(ann ->
                            (com.github.appreciated.vortex_crud.core.config.model.Field<JpaRepository<?, ?>, String, JpaRepository<?, ?>>) com.github.appreciated.vortex_crud.core.config.model.fields.StringIdField
                                    .<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder()
                                    .required(required)
                                    .build()
                    )).or(() -> getAnnotation(entityField, IntegerNumberField.class).map(ann ->
                            (com.github.appreciated.vortex_crud.core.config.model.Field<JpaRepository<?, ?>, String, JpaRepository<?, ?>>) IntegerField
                                    .<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder()
                                    .required(required)
                                    .build()
                    )).or(() -> getAnnotation(entityField, TextAreaField.class).map(ann ->
                            (com.github.appreciated.vortex_crud.core.config.model.Field<JpaRepository<?, ?>, String, JpaRepository<?, ?>>) com.github.appreciated.vortex_crud.core.config.model.fields.TextAreaField
                                    .<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder()
                                    .required(required)
                                    .build()
                    )).or(() -> getAnnotation(entityField, TextField.class).map(ann ->
                            (com.github.appreciated.vortex_crud.core.config.model.Field<JpaRepository<?, ?>, String, JpaRepository<?, ?>>) com.github.appreciated.vortex_crud.core.config.model.fields.TextField
                                    .<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder()
                                    .required(required)
                                    .build()
                    )).or(() -> getAnnotation(entityField, PasswordField.class).map(ann ->
                            (com.github.appreciated.vortex_crud.core.config.model.Field<JpaRepository<?, ?>, String, JpaRepository<?, ?>>) com.github.appreciated.vortex_crud.core.config.model.fields.PasswordField
                                    .<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder()
                                    .required(required)
                                    .build()
                    )).or(() -> getAnnotation(entityField, EmailField.class).map(ann ->
                            (com.github.appreciated.vortex_crud.core.config.model.Field<JpaRepository<?, ?>, String, JpaRepository<?, ?>>) com.github.appreciated.vortex_crud.core.config.model.fields.EmailField
                                    .<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder()
                                    .required(required)
                                    .build()
                    )).or(() -> getAnnotation(entityField, MarkDownField.class).map(ann ->
                            (com.github.appreciated.vortex_crud.core.config.model.Field<JpaRepository<?, ?>, String, JpaRepository<?, ?>>) com.github.appreciated.vortex_crud.core.config.model.fields.MarkDownField
                                    .<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder()
                                    .required(required)
                                    .build()
                    )).orElseThrow();
                }));
    }

}
