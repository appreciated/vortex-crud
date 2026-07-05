package com.github.appreciated.vortex_crud.core.testsupport;

import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;

import java.util.Collection;
import java.util.Map;

/**
 * {@link ReflectionService} over plain maps so unit tests need no real entity classes:
 * entities are {@code Map<String, Object>} and field names are the map keys.
 */
public class MapReflectionService implements ReflectionService<String> {

    @Override
    @SuppressWarnings("unchecked")
    public <T> String getString(T entity, String fieldName) {
        Object value = ((Map<String, Object>) entity).get(fieldName);
        return value != null ? value.toString() : null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Object getValue(T entity, String field) {
        return ((Map<String, Object>) entity).get(field);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> void setValue(T entity, String fieldName, Object value) {
        ((Map<String, Object>) entity).put(fieldName, value);
    }

    @Override
    public <T> Object getId(T entity) {
        return getValue(entity, "id");
    }

    @Override
    public <T> boolean addAll(T entity, String fieldName, Collection<?> elementsToAdd) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> boolean removeAll(T entity, String fieldName, Collection<?> elementsToRemove) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> Class<?> getCollectionType(T entity, String fieldName) {
        throw new UnsupportedOperationException();
    }
}
