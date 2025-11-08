package com.github.appreciated.vortex_crud.core.entity.reflection;

import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFieldNameResolver;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;

/**
 * Service that provides property access to entity properties using Spring's BeanWrapper.
 */
@Service
public class ReflectionService<FieldType> {

    private final VortexCrudDataStoreFieldNameResolver<FieldType> fieldNameResolver;

    public ReflectionService(VortexCrudDataStoreFieldNameResolver<FieldType> fieldNameResolver) {
        this.fieldNameResolver = fieldNameResolver;
    }

    public <T> String getString(T entity, FieldType fieldName) {
        Object value = getValue(entity, fieldName);
        return value != null ? value.toString() : null;
    }

    public <T> Object getValue(T entity, FieldType field) {
        return getValueInternal(entity, fieldNameResolver.getKeyForFieldType(field));
    }

    protected <T> Object getValueInternal(T entity, String fieldName) {
        if (entity == null) {
            return null;
        }

        // First try getter method
        Object value = getValueByGetter(entity, fieldName);
        if (value != null) {
            return value;
        }

        // Then try direct field access
        return getValueByField(entity, fieldName);
    }

    /**
     * Converts a field name to camelCase format.
     * For example, "start_date" becomes "startDate".
     */
    private String toCamelCase(String fieldName) {
        if (fieldName == null || !fieldName.contains("_")) {
            return fieldName;
        }

        StringBuilder result = new StringBuilder();
        boolean capitalizeNext = false;

        for (int i = 0; i < fieldName.length(); i++) {
            char c = fieldName.charAt(i);
            if (c == '_') {
                capitalizeNext = true;
            } else if (capitalizeNext) {
                result.append(Character.toUpperCase(c));
                capitalizeNext = false;
            } else {
                result.append(c);
            }
        }

        return result.toString();
    }

    /**
     * Creates a method name for a getter or setter based on the field name.
     * Handles conversion from snake_case to camelCase.
     */
    private String determineMethodName(String fieldName, String prefix) {
        String camelCaseField = toCamelCase(fieldName);

        if (prefix.equals("is") && camelCaseField.startsWith("is") &&
            camelCaseField.length() > 2 && Character.isUpperCase(camelCaseField.charAt(2))) {
            return camelCaseField;
        }

        return prefix + Character.toUpperCase(camelCaseField.charAt(0)) + camelCaseField.substring(1);
    }

    private <T> Object getValueByGetter(T entity, String fieldName) {
        try {
            String getterName = determineMethodName(fieldName, "get");
            Method getter = entity.getClass().getMethod(getterName);
            return getter.invoke(entity);
        } catch (NoSuchMethodException e) {
            // Try boolean getter
            try {
                String booleanGetter = determineMethodName(fieldName, "is");
                Method getter = entity.getClass().getMethod(booleanGetter);
                return getter.invoke(entity);
            } catch (Exception ex) {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    private <T> Object getValueByField(T entity, String fieldName) {
        try {
            Field field = entity.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(entity);
        } catch (Exception e) {
            return null;
        }
    }

    public <T> void setValue(T entity, FieldType fieldName, Object value) {
        if (entity == null) {
            return;
        }
        String propertyName = fieldNameResolver.getKeyForFieldType(fieldName);

        // First try setter method
        if (setValueBySetter(entity, propertyName, value)) {
            return;
        }

        // Then try direct field access
        if (setValueByField(entity, propertyName, value)) {
            return;
        }

        throw new IllegalArgumentException("Could not set value for field " + propertyName);
    }

    private <T> boolean setValueBySetter(T entity, String fieldName, Object value) {
        try {
            String setterName = determineMethodName(fieldName, "set");
            Method[] methods = entity.getClass().getMethods();
            for (Method method : methods) {
                if (method.getName().equals(setterName) && method.getParameterCount() == 1) {
                    method.invoke(entity, value);
                    return true;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    private <T> boolean setValueByField(T entity, String fieldName, Object value) {
        try {
            Field field = entity.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(entity, value);
        } catch (Exception e) {
            // Ignore if we can't set the value
            return false;
        }
        return true;
    }

    public <T> Object getId(T entity) {
        if (entity == null) {
            return null;
        }
        return getValueInternal(entity, "id");
    }

    /**
     * Adds all elements from the source collection to the target collection.
     *
     * @param entity        The entity containing the collection field
     * @param fieldName     The name of the collection field
     * @param elementsToAdd The elements to add to the collection
     * @param <T>           The entity type
     * @return true if the collection was modified, false otherwise
     */
    public <T> boolean addAll(T entity, FieldType fieldName, Collection<?> elementsToAdd) {
        if (entity == null || elementsToAdd == null || elementsToAdd.isEmpty()) {
            return false;
        }

        Object collectionObj = getValue(entity, fieldName);

        // If collection is null, initialize it with a new ArrayList
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

    /**
     * Removes all elements in the specified collection from the target collection.
     *
     * @param entity           The entity containing the collection field
     * @param fieldName        The name of the collection field
     * @param elementsToRemove The elements to remove from the collection
     * @param <T>              The entity type
     * @return true if the collection was modified, false otherwise
     */
    public <T> boolean removeAll(T entity, FieldType fieldName, Collection<?> elementsToRemove) {
        if (entity == null || elementsToRemove == null || elementsToRemove.isEmpty()) {
            return false;
        }

        Object collectionObj = getValue(entity, fieldName);

        // If collection is null, there's nothing to remove from
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

    /**
     * Gets the collection type for a field.
     *
     * @param entity    The entity containing the collection field
     * @param fieldName The name of the collection field
     * @param <T>       The entity type
     * @return The class of the collection elements, or null if not a collection or type cannot be determined
     */
    public <T> Class<?> getCollectionType(T entity, FieldType fieldName) {
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
            // Ignore and return null
        }

        return null;
    }
}
