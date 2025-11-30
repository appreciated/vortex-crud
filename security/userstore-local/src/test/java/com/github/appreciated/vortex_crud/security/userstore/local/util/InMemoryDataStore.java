package com.github.appreciated.vortex_crud.security.userstore.local.util;

import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class InMemoryDataStore<T> implements VortexCrudDataStore<String, T> {

    private final List<T> data = new ArrayList<>();
    private final Class<T> modelClass;
    private int idCounter = 1;

    public InMemoryDataStore(Class<T> modelClass) {
        this.modelClass = modelClass;
    }

    @Override
    public Object insertRecord(T entity) {
        // Assume ID field is "id" and Integer
        try {
            Field idField = ReflectionUtils.findField(modelClass, "id");
            if (idField != null) {
                ReflectionUtils.makeAccessible(idField);
                if (idField.get(entity) == null) {
                    idField.set(entity, idCounter++);
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        data.add(entity);
        return getId(entity);
    }

    private Object getId(T entity) {
        try {
            Field idField = ReflectionUtils.findField(modelClass, "id");
            if (idField != null) {
                ReflectionUtils.makeAccessible(idField);
                return idField.get(entity);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private Object getValue(T entity, String fieldName) {
        try {
            Field field = ReflectionUtils.findField(modelClass, fieldName);
            if (field != null) {
                ReflectionUtils.makeAccessible(field);
                return field.get(entity);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public List<T> getRecordsFromTable(int offset, int limit) {
        return data.stream().skip(offset).limit(limit).collect(Collectors.toList());
    }

    @Override
    public List<T> getRecordsFromTableWhereColumnEquals(String filterField, Object filterValue, int offset, int limit) {
        return data.stream()
                .filter(item -> Objects.equals(getValue(item, filterField), filterValue))
                .skip(offset)
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public List<T> getRecordsFromTableWhereColumnEqualsOrdered(String filterField, Object filterValue, String orderField, int offset, int limit) {
         // Ignoring order for now
         return getRecordsFromTableWhereColumnEquals(filterField, filterValue, offset, limit);
    }

    @Override
    public List<T> getRecordsFromTableWhereColumnIn(String filterField, List<String> filterValue, int offset, int limit) {
         return data.stream()
                .filter(item -> filterValue.contains(String.valueOf(getValue(item, filterField))))
                .skip(offset)
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public List<T> getRecordsFromTableWhereColumnLike(String filterField, Object filterValue, int offset, int limit) {
        // Simple string contains
        String filter = String.valueOf(filterValue);
        return data.stream()
                .filter(item -> String.valueOf(getValue(item, filterField)).contains(filter))
                .skip(offset)
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public T getRecordById(Object id) {
        // Handle ID type conversion (Integer vs String vs Long)
        // For simplicity, assume ID is Integer
        Integer intId = (id instanceof Integer) ? (Integer) id : Integer.parseInt(id.toString());

        return data.stream()
                .filter(item -> Objects.equals(getId(item), intId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void updateRecordById(T entity) {
        Object id = getId(entity);
        deleteRecordById(id);
        data.add(entity);
    }

    @Override
    public void deleteRecordById(Object id) {
        Integer intId = (id instanceof Integer) ? (Integer) id : Integer.parseInt(id.toString());
        data.removeIf(item -> Objects.equals(getId(item), intId));
    }

    @Override
    public int count() {
        return data.size();
    }

    @Override
    public int countWhereColumnLike(String filterField, String filterValue) {
        return (int) data.stream()
                .filter(item -> String.valueOf(getValue(item, filterField)).contains(filterValue))
                .count();
    }

    @Override
    public Class<T> getModelClass() {
        return modelClass;
    }

    @Override
    public void updateRecord(T entity) {
        updateRecordById(entity);
    }

    @Override
    public void deleteRecord(T entity) {
        deleteRecordById(getId(entity));
    }

    @Override
    public T newInstance() {
        try {
            return modelClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteAll() {
        data.clear();
        idCounter = 1;
    }
}
