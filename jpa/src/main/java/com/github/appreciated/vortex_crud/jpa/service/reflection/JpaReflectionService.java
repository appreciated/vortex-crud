package com.github.appreciated.vortex_crud.jpa.service.reflection;

import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Service;

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
        return wrapper.getPropertyValue(field);
    }

    @Override
    public <T> void setValue(T entity, String fieldName, Object value) {
        if (entity == null) {
            return;
        }
        BeanWrapper wrapper = new BeanWrapperImpl(entity);
        wrapper.setPropertyValue(fieldName, value);
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

        // If collection is null, initialize it with a new ArrayList (assuming List, but JPA should init it)
        if (collectionObj == null) {
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

        if (collectionObj == null || !(collectionObj instanceof Collection)) {
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
        BeanWrapper wrapper = new BeanWrapperImpl(entity);
        var descriptor = wrapper.getPropertyDescriptor(fieldName);
        if (descriptor == null) {
            return null;
        }
        // This part is tricky with just BeanWrapper. We might need to access the field or method to get generics.
        // BeanWrapper doesn't directly expose generic type arguments of properties easily without TypeDescriptor

        // Let's use TypeDescriptor if available via PropertyAccessor, but wrapper.getPropertyTypeDescriptor(fieldName) exists
        var typeDescriptor = wrapper.getPropertyTypeDescriptor(fieldName);
        if (typeDescriptor != null && Collection.class.isAssignableFrom(typeDescriptor.getType())) {
             return typeDescriptor.getResolvableType().getGeneric(0).resolve();
        }
        return null;
    }
}
