package com.github.appreciated.vortex_crud.jpa.service;

import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFieldNameResolver;
import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;

@Service
public class JpaReflectionService implements ReflectionService<String> {

    private final VortexCrudDataStoreFieldNameResolver<String> fieldNameResolver;

    public JpaReflectionService(VortexCrudDataStoreFieldNameResolver<String> fieldNameResolver) {
        this.fieldNameResolver = fieldNameResolver;
    }

    @Override
    public <T> String getString(T entity, String fieldName) {
        Object value = getValue(entity, fieldName);
        return value != null ? value.toString() : null;
    }

    @Override
    public <T> Object getValue(T entity, String field) {
        if (entity == null) {
            return null;
        }
        String propertyName = fieldNameResolver.getKeyForFieldType(field);
        BeanWrapper beanWrapper = new BeanWrapperImpl(entity);
        if (beanWrapper.isReadableProperty(propertyName)) {
            return beanWrapper.getPropertyValue(propertyName);
        }
        return null;
    }

    @Override
    public <T> void setValue(T entity, String fieldName, Object value) {
        if (entity == null) {
            return;
        }
        String propertyName = fieldNameResolver.getKeyForFieldType(fieldName);
        BeanWrapper beanWrapper = new BeanWrapperImpl(entity);
        if (beanWrapper.isWritableProperty(propertyName)) {
            beanWrapper.setPropertyValue(propertyName, value);
        } else {
            throw new IllegalArgumentException("Could not set value for field " + propertyName);
        }
    }

    @Override
    public <T> Object getId(T entity) {
        return getValue(entity, "id");
    }

    @Override
    public <T> boolean addAll(T entity, String fieldName, Collection<?> elementsToAdd) {
        if (entity == null || elementsToAdd == null || elementsToAdd.isEmpty()) {
            return false;
        }

        Object collectionObj = getValue(entity, fieldName);

        // If collection is null, initialize it with a new Collection based on field type if possible
        // But BeanWrapper doesn't auto-init collections usually unless configured.
        // For now, assume initialized or init it.
        // Jpa entities usually have init collections.

        if (collectionObj == null) {
            // Try to initialize?
            // The original implementation did: collectionObj = new java.util.ArrayList<>(); setValue(...)
            // Let's stick to that simple behavior for now, defaulting to ArrayList.
            collectionObj = new java.util.ArrayList<>();
            setValue(entity, fieldName, collectionObj);
        }

        if (!(collectionObj instanceof Collection)) {
            return false;
        }

        @SuppressWarnings("unchecked")
        Collection<Object> collection = (Collection<Object>) collectionObj;
        return collection.addAll(elementsToAdd);
    }

    @Override
    public <T> boolean removeAll(T entity, String fieldName, Collection<?> elementsToRemove) {
        if (entity == null || elementsToRemove == null || elementsToRemove.isEmpty()) {
            return false;
        }

        Object collectionObj = getValue(entity, fieldName);

        if (collectionObj == null) {
            return false;
        }

        if (!(collectionObj instanceof Collection)) {
            return false;
        }

        @SuppressWarnings("unchecked")
        Collection<Object> collection = (Collection<Object>) collectionObj;
        return collection.removeAll(elementsToRemove);
    }

    @Override
    public <T> Class<?> getCollectionType(T entity, String fieldName) {
        if (entity == null) {
            return null;
        }

        String propertyName = fieldNameResolver.getKeyForFieldType(fieldName);

        try {
            Field field = entity.getClass().getDeclaredField(propertyName);
            Type genericType = field.getGenericType();

            if (genericType instanceof ParameterizedType paramType) {
                Type[] typeArgs = paramType.getActualTypeArguments();

                if (typeArgs.length > 0 && typeArgs[0] instanceof Class) {
                    return (Class<?>) typeArgs[0];
                }
            }
        } catch (Exception e) {
            // Ignore
        }
        return null;
    }
}
