package com.github.appreciated.vortex_crud.jpa.service.datastore;

import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.model.GenericEntity;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.ReferenceFieldFactory;
import com.github.appreciated.vortex_crud.jpa.service.Field;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for managing dynamic entities using an EntityManager.
 * Provides methods for CRUD operations and lazy loading data from the database.
 */

public class JpaRepositoryDataStore<T> implements VortexCrudDataStore<String> {

    private final JpaRepository<T, Object> repository;
    private final Class<T> repositoryModelClass;
    private final JpaGenericEntityMapper mapper;
    private final JpaFieldTypeResolverService resolverService;
    private final Map<String, java.lang.reflect.Field> fields;

    public JpaRepositoryDataStore(JpaRepository<T, ?> repository,
                                  JpaGenericEntityMapper mapper,
                                  JpaFieldTypeResolverService resolverService) {
        this.repository = (JpaRepository<T, Object>) repository;
        this.repositoryModelClass = getEntityClass(repository);
        this.mapper = mapper;
        this.resolverService = resolverService;
        this.fields = getModelFields();
    }

    private Map<String, java.lang.reflect.Field> getModelFields() {
        return Arrays.stream(repositoryModelClass.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Field.class) || field.isAnnotationPresent(Id.class))
                .collect(Collectors.toMap(java.lang.reflect.Field::getName, field -> field));
    }

    public Class<T> getEntityClass(JpaRepository<?, ?> repository) {
        try {
            Class<?> repoInterfaceOpt = Arrays.stream(repository.getClass().getInterfaces())
                    .filter(i -> i.isInterface() && JpaRepository.class.isAssignableFrom(i))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Could not find JpaRepository interface in " + repository.getClass().getName()));

            return Arrays.stream(repoInterfaceOpt.getGenericInterfaces())
                    .filter(type -> type instanceof ParameterizedType)
                    .map(type -> (ParameterizedType) type)
                    .filter(paramType -> {
                        Type rawType = paramType.getRawType();
                        return rawType instanceof Class &&
                               JpaRepository.class.isAssignableFrom((Class<?>) rawType);
                    })
                    .map(paramType -> paramType.getActualTypeArguments()[0])
                    .filter(t -> t instanceof Class<?>)
                    .map(t -> (Class<T>) t)
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Could not determine entity class from repository " + repository.getClass().getName() + ". For testing with mocks, use the constructor that accepts an explicit entity class."));
        } catch (Exception e) {
            throw new IllegalArgumentException("Error determining entity class from repository " + repository.getClass().getName() + ". For testing with mocks, use the constructor that accepts an explicit entity class.", e);
        }
    }


    @Transactional
    public Object insertRecord(ModelClass entity) {
        // Check if entity has an ID (for update case)
        Object id = entity.get("id");
        if (id != null) {
            // Fetch the current state of the entity
            Optional<T> existingEntityOpt = repository.findById(id);
            if (existingEntityOpt.isPresent()) {
                T existingEntity = existingEntityOpt.get();
                T mappedEntity = mapper.mapToEntity(entity, getModelClass());

                // Update only non-relationship fields
                for (java.lang.reflect.Field field : getModelClass().getDeclaredFields()) {
                    field.setAccessible(true);
                    // Skip ManyToMany and OneToMany fields
                    if (field.isAnnotationPresent(jakarta.persistence.ManyToMany.class) ||
                        field.isAnnotationPresent(jakarta.persistence.OneToMany.class)) {
                        try {
                            // Keep the original value for relationship fields
                            field.set(mappedEntity, field.get(existingEntity));
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException("Error preserving relationship field: " + field.getName(), e);
                        }
                    }
                }
                return repository.save(mappedEntity);
            }
        }

        // For new entities or if existing entity not found
        T mappedEntity = mapper.mapToEntity(entity, getModelClass());
        return repository.save(mappedEntity);
    }

    /**
     * Read (Select) records from a table with pagination for lazy loading.
     *
     * @param offset The starting position of the first result.
     * @param limit  The maximum number of results to return.
     * @return A list of records (as a map of column names and values).
     */
    public List<GenericEntity> getRecordsFromTable(int offset, int limit) {
        return repository.findAll(Pageable.ofSize(limit).withPage(offset / limit))
                .stream()
                .map(t -> mapper.mapFromEntity(t, fields.values()))
                .toList();
    }

    @Override
    public List<GenericEntity> getRecordsFromTableWhereColumnEquals(String filterField, Object filterValue, int offset, int limit) {
        return getRecordsForFieldAndValueAndMatcher(
                filterField,
                filterValue,
                ExampleMatcher.matchingAny().withIgnoreCase().withStringMatcher(ExampleMatcher.StringMatcher.EXACT),
                limit,
                offset
        );
    }

    @Override
    public List<GenericEntity> getRecordsFromTableWhereColumnIn(String filterField, List<String> filterValues, int offset, int limit) {
        return repository.findAll(Pageable.ofSize(limit).withPage(offset / limit))
                .map(t -> mapper.mapFromEntity(t, fields.values()))
                .toList();
    }

    @Override
    public List<GenericEntity> getRecordsFromTableWhereColumnLike(String filterField, Object filterValue, int offset, int limit) {
        return getRecordsForFieldAndValueAndMatcher(
                filterField,
                filterValue,
                ExampleMatcher.matchingAny().withIgnoreCase().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING),
                limit,
                offset
        );
    }

    private List<GenericEntity> getRecordsForFieldAndValueAndMatcher(String filterField, Object filterValue, ExampleMatcher matcher, int limit, int offset) {
        Example<T> example = getExample(filterField, filterValue, matcher);
        return repository.findAll(example, Pageable.ofSize(limit).withPage(offset / limit))
                .map(t -> mapper.mapFromEntity(t, fields.values()))
                .toList();
    }

    private Example<T> getExample(String filterField, Object filterValue, ExampleMatcher matcher) {
        GenericEntity genericEntity = new GenericEntity();
        updatePropertiesForField(genericEntity, filterField, filterValue);
        return Example.of(mapper.mapToEntity(genericEntity, getModelClass()), matcher);
    }

    /**
     * Read a specific record by ID (assuming the ID column is "id").
     *
     * @param id The ID of the record to fetch.
     * @return The record (as a map of column names and values) or null if not found.
     */
    @Transactional(readOnly = true)
    public GenericEntity getRecordById(Object id) {
        if (id == null) {
            return null;
        }
        return repository.findById(id)
                .map(t -> mapper.mapFromEntity(t, fields.values()))
                .orElse(null);
    }

    /**
     * Update a record in the given table by ID.
     *
     * @param id     The ID of the record to update.
     * @param entity A map of column names and new entity to update.
     */
    @Transactional
    public void updateRecordById(Object id, ModelClass entity) {
        entity.put("id", mapper.convertToFieldType(id, fields.get("id").getType()));
        insertRecord(entity);
    }

    /**
     * Delete a record from the table by ID.
     *
     * @param id The ID of the record to delete.
     */
    @Transactional
    public void deleteRecordById(Object id) {
        repository.deleteById(id);
    }

    /**
     * Delete all records from the table.
     */
    @Transactional
    public void deleteAllRecords() {
        repository.deleteAll();
    }

    public int count() {
        return (int) repository.count();
    }

    @Override
    public int countWhereColumnLike(String filterField, String filterValue) {
        Example<T> example = getExample(
                filterField,
                filterValue,
                ExampleMatcher.matchingAny().withIgnoreCase().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
        );
        return (int) repository.count(example);
    }

    private void updatePropertiesForField(ModelClass entity, String targetField, Object targetValue) {
        java.lang.reflect.Field field = fields.get(targetField);
        if (field.isAnnotationPresent(Field.class) && field.getAnnotation(Field.class).value() == ReferenceFieldFactory.class) {
            Class<?> targetFieldType = resolverService.resolveTargetClass(this, field);
            try {
                Object value = targetFieldType.getDeclaredConstructor().newInstance();
                // Find the ID field in the target entity and set it
                java.lang.reflect.Field idField = findIdField(targetFieldType);
                if (idField != null) {
                    idField.setAccessible(true);
                    // Convert the targetValue to the appropriate type for the ID field
                    Object convertedValue = mapper.convertToFieldType(targetValue, idField.getType());
                    idField.set(value, convertedValue);
                    if (field.isAnnotationPresent(OneToMany.class)) {
                        entity.put(targetField, List.of(value));
                    } else {
                        entity.put(targetField, value);
                    }
                } else {
                    throw new RuntimeException("Could not find ID field in " + targetFieldType.getName());
                }
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                     InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (Exception e) {
                throw e;
            }
        } else {
            entity.put(targetField, targetValue);
        }
    }

    public Collection<java.lang.reflect.Field> getFields() {
        return fields.values();
    }

    @Override
    public java.lang.reflect.Field getField(String foreignKeyField) {
        return fields.get(foreignKeyField);
    }

    public Class<T> getModelClass() {
        return this.repositoryModelClass;
    }

    /**
     * Find the ID field in the given entity class.
     *
     * @param entityClass The entity class to search in.
     * @return The ID field, or null if not found.
     */
    private java.lang.reflect.Field findIdField(Class<?> entityClass) {
        for (java.lang.reflect.Field field : entityClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                return field;
            }
        }
        return null;
    }
}
