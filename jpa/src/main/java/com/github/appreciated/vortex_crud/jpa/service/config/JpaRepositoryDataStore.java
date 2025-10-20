package com.github.appreciated.vortex_crud.jpa.service.config;

import com.github.appreciated.vortex_crud.core.config.model.annotations.Field;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.ReferenceFieldFactory;
import com.github.appreciated.vortex_crud.jpa.service.JpaFieldAnnotationRegistryService;
import com.github.appreciated.vortex_crud.jpa.service.datastore.JpaFieldTypeResolverService;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import org.springframework.data.domain.*;
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

public class JpaRepositoryDataStore<ModelClass> implements VortexCrudDataStore<String, ModelClass> {

    private final JpaRepository<ModelClass, Object> repository;
    private final Class<ModelClass> repositoryModelClass;
    private final JpaFieldTypeResolverService resolverService;
    private JpaFieldAnnotationRegistryService jpaFieldAnnotationRegistryService;
    private final Map<String, java.lang.reflect.Field> fields;
    private final java.lang.reflect.Field idField;

    public JpaRepositoryDataStore(JpaRepository<ModelClass, ?> repository,
                                  JpaFieldTypeResolverService resolverService,
                                  JpaFieldAnnotationRegistryService jpaFieldAnnotationRegistryService) {
        this.repository = (JpaRepository<ModelClass, Object>) repository;
        this.repositoryModelClass = getEntityClass(repository);
        this.resolverService = resolverService;
        this.jpaFieldAnnotationRegistryService = jpaFieldAnnotationRegistryService;
        this.fields = getModelFields();
        this.idField = findIdField(repositoryModelClass);
    }

    /**
     * Constructor that accepts a mapper parameter for backward compatibility with tests.
     * The mapper is ignored as it's no longer needed.
     */
    public JpaRepositoryDataStore(JpaRepository<ModelClass, ?> repository,
                                  Object mapper,
                                  JpaFieldTypeResolverService resolverService, JpaFieldAnnotationRegistryService jpaFieldAnnotationRegistryService) {
        this(repository, resolverService, jpaFieldAnnotationRegistryService);
    }

    private Map<String, java.lang.reflect.Field> getModelFields() {
        return Arrays.stream(repositoryModelClass.getDeclaredFields())
                .filter(field -> jpaFieldAnnotationRegistryService.hasFieldAnnotation(field) || field.isAnnotationPresent(Id.class))
                .collect(Collectors.toMap(java.lang.reflect.Field::getName, field -> field));
    }

    public Class<ModelClass> getEntityClass(JpaRepository<?, ?> repository) {
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
                    .map(t -> (Class<ModelClass>) t)
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Could not determine entity class from repository " + repository.getClass().getName() + ". For testing with mocks, use the constructor that accepts an explicit entity class."));
        } catch (Exception e) {
            throw new IllegalArgumentException("Error determining entity class from repository " + repository.getClass().getName() + ". For testing with mocks, use the constructor that accepts an explicit entity class.", e);
        }
    }

    @Transactional
    public Object insertRecord(ModelClass entity) {
        // Check if entity has an ID (for update case)
        Object id = null;
        try {
            java.lang.reflect.Field idField = findIdField(repositoryModelClass);
            if (idField != null) {
                idField.setAccessible(true);
                id = idField.get(entity);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Error accessing ID field", e);
        }

        if (id != null) {
            // Fetch the current state of the entity
            Optional<ModelClass> existingEntityOpt = repository.findById(id);
            if (existingEntityOpt.isPresent()) {
                ModelClass existingEntity = existingEntityOpt.get();

                // Update only non-relationship fields
                for (java.lang.reflect.Field field : repositoryModelClass.getDeclaredFields()) {
                    field.setAccessible(true);
                    // Skip ManyToMany and OneToMany fields
                    if (field.isAnnotationPresent(jakarta.persistence.ManyToMany.class) ||
                        field.isAnnotationPresent(jakarta.persistence.OneToMany.class)) {
                        try {
                            // Keep the original value for relationship fields
                            field.set(entity, field.get(existingEntity));
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException("Error preserving relationship field: " + field.getName(), e);
                        }
                    }
                }
                return getId(repository.save(entity));
            }
        }

        // For new entities or if existing entity not found
        return getId(repository.save(entity));
    }

    private Object getId(ModelClass save) {
        try {
            idField.setAccessible(true);
            return idField.get(save);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Read (Select) records from a table with pagination for lazy loading.
     *
     * @param offset The starting position of the first result.
     * @param limit  The maximum number of results to return.
     * @return A list of records.
     */
    public List<ModelClass> getRecordsFromTable(int offset, int limit) {
        return repository.findAll(Pageable.ofSize(limit).withPage(offset / limit))
                .getContent();
    }

    @Override
    public List<ModelClass> getRecordsFromTableWhereColumnEquals(String filterField, Object filterValue, int offset, int limit) {
        return getRecordsForFieldAndValueAndMatcher(
                filterField,
                filterValue,
                ExampleMatcher.matchingAny().withIgnoreCase().withStringMatcher(ExampleMatcher.StringMatcher.EXACT),
                limit,
                offset
        );
    }

    @Override
    public List<ModelClass> getRecordsFromTableWhereColumnEqualsOrdered(String filterField,
                                                                        Object filterValue,
                                                                        String orderField,
                                                                        int offset,
                                                                        int limit) {
        Example<ModelClass> example = getExample(filterField, filterValue,
                ExampleMatcher.matchingAny().withIgnoreCase().withStringMatcher(ExampleMatcher.StringMatcher.EXACT));

        return repository.findAll(
                example,
                PageRequest.of(offset / limit, limit, Sort.by(orderField))
        ).getContent();
    }

    @Override
    public List<ModelClass> getRecordsFromTableWhereColumnIn(String filterField, List<String> filterValues, int offset, int limit) {
        return repository.findAll(Pageable.ofSize(limit).withPage(offset / limit))
                .getContent();
    }

    @Override
    public List<ModelClass> getRecordsFromTableWhereColumnLike(String filterField, Object filterValue, int offset, int limit) {
        return getRecordsForFieldAndValueAndMatcher(
                filterField,
                filterValue,
                ExampleMatcher.matchingAny().withIgnoreCase().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING),
                limit,
                offset
        );
    }

    private List<ModelClass> getRecordsForFieldAndValueAndMatcher(String filterField, Object filterValue, ExampleMatcher matcher, int limit, int offset) {
        Example<ModelClass> example = getExample(filterField, filterValue, matcher);
        return repository.findAll(example, Pageable.ofSize(limit).withPage(offset / limit))
                .getContent();
    }

    private Example<ModelClass> getExample(String filterField, Object filterValue, ExampleMatcher matcher) {
        try {
            ModelClass probe = repositoryModelClass.getDeclaredConstructor().newInstance();
            java.lang.reflect.Field field = fields.get(filterField);
            if (field != null) {
                field.setAccessible(true);
                if (filterValue != null) {
                    // Convert filterValue to the appropriate type if needed
                    Object convertedValue = convertToFieldType(filterValue, field.getType());
                    field.set(probe, convertedValue);
                }
            }
            return Example.of(probe, matcher);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                 InvocationTargetException e) {
            throw new RuntimeException("Error creating example entity", e);
        }
    }

    private Object convertToFieldType(Object value, Class<?> targetType) {
        if (value == null) {
            return null;
        }

        // If the value is already of the target type, return it
        if (targetType.isInstance(value)) {
            return value;
        }

        // Handle common type conversions
        if (targetType == String.class) {
            return value.toString();
        } else if (targetType == Integer.class || targetType == int.class) {
            if (value instanceof Number) {
                return ((Number) value).intValue();
            }
            return Integer.parseInt(value.toString());
        } else if (targetType == Long.class || targetType == long.class) {
            if (value instanceof Number) {
                return ((Number) value).longValue();
            }
            return Long.parseLong(value.toString());
        } else if (targetType == Double.class || targetType == double.class) {
            if (value instanceof Number) {
                return ((Number) value).doubleValue();
            }
            return Double.parseDouble(value.toString());
        } else if (targetType == Boolean.class || targetType == boolean.class) {
            if (value instanceof Boolean) {
                return value;
            }
            return Boolean.parseBoolean(value.toString());
        }

        // Handle entity relationships - if the target type is an entity class
        // and the value is a primitive or String, create an entity instance with its ID set
        if (targetType.isAnnotationPresent(jakarta.persistence.Entity.class)) {
            try {
                // Create a new instance of the entity
                Object entity = targetType.getDeclaredConstructor().newInstance();

                // Find the ID field
                java.lang.reflect.Field idField = findIdField(targetType);
                if (idField != null) {
                    idField.setAccessible(true);
                    // Convert the value to the ID field's type and set it
                    Object convertedId = convertToFieldType(value, idField.getType());
                    idField.set(entity, convertedId);
                    return entity;
                }
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                     InvocationTargetException e) {
                // Log the error but continue with other conversion attempts
                System.err.println("Error creating entity instance for " + targetType.getName() + ": " + e.getMessage());
            }
        }

        // For other types, try to find a constructor that takes the value's type
        try {
            if (value instanceof String) {
                return targetType.getDeclaredConstructor(String.class).newInstance(value);
            }
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            // Ignore and try other methods
        }

        // If all else fails, return the original value and hope for the best
        return value;
    }

    /**
     * Read a specific record by ID.
     *
     * @param id The ID of the record to fetch.
     * @return The record or null if not found.
     */
    @Transactional(readOnly = true)
    public ModelClass getRecordById(Object id) {
        if (id == null) {
            return null;
        }
        return repository.findById(id).orElse(null);
    }

    /**
     * Update a record in the given table by ID.
     *
     * @param entity The entity to update.
     */
    @Transactional
    public void updateRecordById(ModelClass entity) {
        insertRecord(entity);
    }

    /**
     * Update a record in the given table by ID.
     * This method is provided for backward compatibility with tests.
     *
     * @param id     The ID of the record to update.
     * @param entity The entity with updated values.
     */
    @Transactional
    public void updateRecordById(Object id, ModelClass entity) {
        try {
            java.lang.reflect.Field idField = findIdField(repositoryModelClass);
            if (idField != null) {
                idField.setAccessible(true);
                // Set the ID on the entity
                idField.set(entity, convertToFieldType(id, idField.getType()));
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Error setting ID field", e);
        }

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
        Example<ModelClass> example = getExample(
                filterField,
                filterValue,
                ExampleMatcher.matchingAny().withIgnoreCase().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
        );
        return (int) repository.count(example);
    }

    private void updatePropertiesForField(ModelClass entity, String targetField, Object targetValue) {
        java.lang.reflect.Field field = fields.get(targetField);
        if (field == null) {
            return;
        }

        field.setAccessible(true);

        if (field.isAnnotationPresent(Field.class) && field.getAnnotation(Field.class).value() == ReferenceFieldFactory.class) {
            Class<?> targetFieldType = resolverService.resolveTargetClass(this, field);
            try {
                Object value = targetFieldType.getDeclaredConstructor().newInstance();
                // Find the ID field in the target entity and set it
                java.lang.reflect.Field idField = findIdField(targetFieldType);
                if (idField != null) {
                    idField.setAccessible(true);
                    // Convert the targetValue to the appropriate type for the ID field
                    Object convertedValue = convertToFieldType(targetValue, idField.getType());
                    idField.set(value, convertedValue);
                    if (field.isAnnotationPresent(OneToMany.class)) {
                        field.set(entity, List.of(value));
                    } else {
                        field.set(entity, value);
                    }
                } else {
                    throw new RuntimeException("Could not find ID field in " + targetFieldType.getName());
                }
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                     InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                Object convertedValue = convertToFieldType(targetValue, field.getType());
                field.set(entity, convertedValue);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Error setting field value: " + field.getName(), e);
            }
        }
    }

    public Collection<java.lang.reflect.Field> getFields() {
        return fields.values();
    }

    @Override
    public java.lang.reflect.Field getField(String foreignKeyField) {
        return fields.get(foreignKeyField);
    }

    public Class<ModelClass> getModelClass() {
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

    @Override
    @Transactional
    public void updateRecord(ModelClass entity) {
        insertRecord(entity);
    }

    @Override
    public void deleteRecord(ModelClass entity) {
        repository.delete(entity);
    }

    @Override
    public ModelClass newInstance() {
        try {
            return repositoryModelClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                 InvocationTargetException e) {
            throw new RuntimeException("Error creating new instance of " + repositoryModelClass.getName(), e);
        }
    }
}
