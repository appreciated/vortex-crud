package com.github.appreciated.vortex_crud.jpa.service.reflection;

import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

@Service
public class JpaReflectionService implements ReflectionService<String> {

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
        BeanWrapper wrapper = new BeanWrapperImpl(entity);
        try {
            return wrapper.getPropertyValue(field);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public <T> void setValue(T entity, String fieldName, Object value) {
        if (entity == null) {
            return;
        }
        BeanWrapper wrapper = new BeanWrapperImpl(entity);
        try {
            wrapper.setPropertyValue(fieldName, value);
        } catch (Exception e) {
            // Log? Throw? Original implementation threw IllegalArgumentException if failed to set by setter/field
            // But also returned boolean or void.
            // Interface says void.
             throw new IllegalArgumentException("Could not set value for field " + fieldName, e);
        }
    }

    @Override
    public <T> Object getId(T entity) {
        // JPA entities usually have "id" property.
        // Or we could check for @Id annotation but that requires EntityManager or checking annotations manually.
        // The original implementation used "id" hardcoded string.
        return getValue(entity, "id");
    }

    @Override
    public <T> boolean addAll(T entity, String fieldName, Collection<?> elementsToAdd) {
        if (entity == null || elementsToAdd == null || elementsToAdd.isEmpty()) {
            return false;
        }

        Object collectionObj = getValue(entity, fieldName);

        if (collectionObj == null) {
            collectionObj = new ArrayList<>();
            setValue(entity, fieldName, collectionObj);
        }

        if (collectionObj instanceof Collection) {
             @SuppressWarnings("unchecked")
             Collection<Object> collection = (Collection<Object>) collectionObj;
             return collection.addAll(elementsToAdd);
        }
        return false;
    }

    @Override
    public <T> boolean removeAll(T entity, String fieldName, Collection<?> elementsToRemove) {
        if (entity == null || elementsToRemove == null || elementsToRemove.isEmpty()) {
            return false;
        }

        Object collectionObj = getValue(entity, fieldName);
        if (collectionObj instanceof Collection) {
             @SuppressWarnings("unchecked")
             Collection<Object> collection = (Collection<Object>) collectionObj;
             return collection.removeAll(elementsToRemove);
        }
        return false;
    }

    @Override
    public <T> Class<?> getCollectionType(T entity, String fieldName) {
        if (entity == null) {
            return null;
        }
        try {
            Field field = entity.getClass().getDeclaredField(fieldName);
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
