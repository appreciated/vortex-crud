package com.github.appreciated.vortex_crud.core.entity.reflection;

import java.util.Collection;

/**
 * Interface that provides property access to entity properties.
 */
public interface ReflectionService<FieldType> {

    <T> String getString(T entity, FieldType fieldName);

    <T> Object getValue(T entity, FieldType field);

    <T> void setValue(T entity, FieldType fieldName, Object value);

    <T> Object getId(T entity);

    <T> boolean addAll(T entity, FieldType fieldName, Collection<?> elementsToAdd);

    <T> boolean removeAll(T entity, FieldType fieldName, Collection<?> elementsToRemove);

    <T> Class<?> getCollectionType(T entity, FieldType fieldName);
}
