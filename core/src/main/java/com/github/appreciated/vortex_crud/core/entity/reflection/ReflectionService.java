package com.github.appreciated.vortex_crud.core.entity.reflection;

import java.util.Collection;

/**
 * Service that provides property access to entity properties.
 */
public interface ReflectionService<FieldType> {

    <T> String getString(T entity, FieldType fieldName);

    <T> Object getValue(T entity, FieldType field);

    <T> void setValue(T entity, FieldType fieldName, Object value);

    <T> Object getId(T entity);

    /**
     * Adds all elements from the source collection to the target collection.
     *
     * @param entity        The entity containing the collection field
     * @param fieldName     The name of the collection field
     * @param elementsToAdd The elements to add to the collection
     * @param <T>           The entity type
     * @return true if the collection was modified, false otherwise
     */
    <T> boolean addAll(T entity, FieldType fieldName, Collection<?> elementsToAdd);

    /**
     * Removes all elements in the specified collection from the target collection.
     *
     * @param entity           The entity containing the collection field
     * @param fieldName        The name of the collection field
     * @param elementsToRemove The elements to remove from the collection
     * @param <T>              The entity type
     * @return true if the collection was modified, false otherwise
     */
    <T> boolean removeAll(T entity, FieldType fieldName, Collection<?> elementsToRemove);

    /**
     * Gets the collection type for a field.
     *
     * @param entity    The entity containing the collection field
     * @param fieldName The name of the collection field
     * @param <T>       The entity type
     * @return The class of the collection elements, or null if not a collection or type cannot be determined
     */
    <T> Class<?> getCollectionType(T entity, FieldType fieldName);
}
