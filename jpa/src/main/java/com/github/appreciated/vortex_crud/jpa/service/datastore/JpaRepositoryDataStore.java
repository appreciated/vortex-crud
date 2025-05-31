package com.github.appreciated.vortex_crud.jpa.service.datastore;

import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.model.GenericEntity;
import com.github.appreciated.vortex_crud.jpa.service.Field;
import com.github.appreciated.vortex_crud.jpa.service.JpaGenericEntityMapper;
import jakarta.persistence.Id;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Service for managing dynamic entities using an EntityManager.
 * Provides methods for CRUD operations and lazy loading data from the database.
 */

public class JpaRepositoryDataStore<T> implements VortexCrudDataStore<String> {

    private final JpaRepository<T, Object> repository;
    private final Class<T> repositoryModelClass;
    private final JpaGenericEntityMapper mapper;
    private final List<java.lang.reflect.Field> fields;

    public JpaRepositoryDataStore(JpaRepository<T, ?> repository, JpaGenericEntityMapper mapper) {
        this.repository = (JpaRepository<T, Object>) repository;
        this.repositoryModelClass = getEntityClass(repository);
        this.mapper = mapper;
        this.fields = getModelFields();
    }

    private List<java.lang.reflect.Field> getModelFields() {
        return Arrays.stream(repositoryModelClass.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Field.class) || field.isAnnotationPresent(Id.class))
                .toList();
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
    public Object insertRecord(GenericEntity entity) {
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
                .map(t -> mapper.mapFromEntity(t, fields))
                .toList();
    }

    @Override
    public List<GenericEntity> getRecordsFromTableWhereColumnEquals(String filterField, Object filterValue, int offset, int limit) {
        HashMap<String, Object> properties = new HashMap<>();
        properties.put(filterField, filterValue);
        Example<T> example = Example.of(
                mapper.mapToEntity(new GenericEntity(properties), getModelClass()),
                ExampleMatcher.matchingAny().withIgnoreCase().withStringMatcher(ExampleMatcher.StringMatcher.EXACT)
        );
        return repository.findAll(example, Pageable.ofSize(limit).withPage(offset / limit))
                .map(t -> mapper.mapFromEntity(t, fields))
                .toList();
    }

    @Override
    public List<GenericEntity> getRecordsFromTableWhereColumnIn(String filterField, List<String> filterValues, int offset, int limit) {
        return repository.findAll(Pageable.ofSize(limit).withPage(offset / limit))
                .map(t -> mapper.mapFromEntity(t, fields))
                .toList();
    }

    @Override
    public List<GenericEntity> getRecordsFromTableWhereColumnLike(String filterField, String filterValue, int offset, int limit) {
        HashMap<String, Object> properties = new HashMap<>();
        properties.put(filterField, filterValue);
        Example<T> example = Example.of(
                mapper.mapToEntity(new GenericEntity(properties), getModelClass()),
                ExampleMatcher.matchingAny().withIgnoreCase().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
        );
        return repository.findAll(example, Pageable.ofSize(limit).withPage(offset / limit))
                .map(t -> mapper.mapFromEntity(t, fields))
                .toList();
    }

    /**
     * Read a specific record by ID (assuming the ID column is "id").
     *
     * @param id The ID of the record to fetch.
     * @return The record (as a map of column names and values) or null if not found.
     */
    public GenericEntity getRecordById(Object id) {
        return repository.findById(id)
                .map(t -> mapper.mapFromEntity(t, fields))
                .orElse(null);
    }

    /**
     * Update a record in the given table by ID.
     *
     * @param id     The ID of the record to update.
     * @param entity A map of column names and new entity to update.
     */
    @Transactional
    public void updateRecordById(Object id, GenericEntity entity) {
        entity.put("id", id);
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
        Example<T> example = Example.of(
                mapper.mapToEntity(new GenericEntity(new HashMap<>()), getModelClass()),
                ExampleMatcher.matchingAny().withIgnoreCase().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
        );
        return (int) repository.count(example);
    }

    public List<java.lang.reflect.Field> getFields() {
        return fields;
    }

    public Class<T> getModelClass() {
        return this.repositoryModelClass;
    }
}
