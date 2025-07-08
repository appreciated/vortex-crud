package com.github.appreciated.vortex_crud.core.entity.reflection;

import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Service that provides reflection-based access to entity properties.
 * This replaces the direct get/put/getString methods with reflection-based access.
 */
@Service
public class ReflectionService {

    // Cache for fields and methods to improve performance
    private final Map<Class<?>, Map<String, Field>> fieldCache = new HashMap<>();
    private final Map<Class<?>, Map<String, Method>> getterCache = new HashMap<>();
    private final Map<Class<?>, Map<String, Method>> setterCache = new HashMap<>();

    /**
     * Gets a property value from an entity using reflection.
     *
     * @param entity    The entity object
     * @param fieldName The name of the field to get
     * @param <T>       The type of the entity
     * @return The value of the field
     */
    public <T> Object getValue(T entity, String fieldName) {
        if (entity == null) {
            return null;
        }

        // First try using get method if entity implements Map
        if (entity instanceof Map) {
            return ((Map<?, ?>) entity).get(fieldName);
        }

        // Then try using a getter method
        Method getter = findGetter(entity.getClass(), fieldName);
        if (getter != null) {
            try {
                return getter.invoke(entity);
            } catch (Exception e) {
                // Fall through to field access
            }
        }

        // Finally try direct field access
        Field field = findField(entity.getClass(), fieldName);
        if (field != null) {
            try {
                field.setAccessible(true);
                return field.get(entity);
            } catch (Exception e) {
                throw new RuntimeException("Error getting field " + fieldName + " from " + entity.getClass().getName(), e);
            }
        }

        throw new RuntimeException("Field " + fieldName + " not found in " + entity.getClass().getName());
    }

    /**
     * Gets a property value as a String from an entity using reflection.
     *
     * @param entity    The entity object
     * @param fieldName The name of the field to get
     * @param <T>       The type of the entity
     * @return The value of the field as a String
     */
    public <T> String getString(T entity, String fieldName) {
        Object value = getValue(entity, fieldName);
        return value == null ? null : value.toString();
    }

    /**
     * Sets a property value on an entity using reflection.
     *
     * @param entity    The entity object
     * @param fieldName The name of the field to set
     * @param value     The value to set
     * @param <T>       The type of the entity
     */
    public <T> void setValue(T entity, String fieldName, Object value) {
        if (entity == null) {
            return;
        }

        // First try using put method if entity implements Map
        if (entity instanceof Map) {
            ((Map<String, Object>) entity).put(fieldName, value);
            return;
        }

        // Then try using a setter method
        Method setter = findSetter(entity.getClass(), fieldName, value != null ? value.getClass() : Object.class);
        if (setter != null) {
            try {
                setter.invoke(entity, value);
                return;
            } catch (Exception e) {
                // Fall through to field access
            }
        }

        // Finally try direct field access
        Field field = findField(entity.getClass(), fieldName);
        if (field != null) {
            try {
                field.setAccessible(true);
                field.set(entity, value);
                return;
            } catch (Exception e) {
                throw new RuntimeException("Error setting field " + fieldName + " on " + entity.getClass().getName(), e);
            }
        }

        throw new RuntimeException("Field " + fieldName + " not found in " + entity.getClass().getName());
    }

    /**
     * Gets the ID of an entity using reflection.
     * This replaces DataStoreUtil.getId(entity).
     *
     * @param entity The entity object
     * @param <T>    The type of the entity
     * @return The ID of the entity as a String
     */
    public <T> String getId(T entity) {
        if (entity == null) {
            return null;
        }
        
        Object id = getValue(entity, "id");
        return id == null ? null : id.toString();
    }

    /**
     * Checks if an entity is new (has no ID) using reflection.
     * This replaces DataStoreUtil.isNew(entity).
     *
     * @param entity The entity object
     * @param <T>    The type of the entity
     * @return True if the entity is new, false otherwise
     */
    public <T> boolean isNew(T entity) {
        return getValue(entity, "id") == null;
    }

    /**
     * Compares an entity's ID with a string.
     * This replaces DataStoreUtil.equals(item, comparing).
     *
     * @param entity    The entity object
     * @param comparing The ID to compare with
     * @param <T>       The type of the entity
     * @return True if the entity's ID equals the comparing string, false otherwise
     */
    public <T> boolean equals(T entity, String comparing) {
        return Objects.equals(getId(entity), comparing);
    }

    // Helper methods for finding fields and methods

    private Field findField(Class<?> clazz, String fieldName) {
        Map<String, Field> fields = fieldCache.computeIfAbsent(clazz, k -> new HashMap<>());
        
        return fields.computeIfAbsent(fieldName, k -> {
            Class<?> currentClass = clazz;
            while (currentClass != null) {
                try {
                    return currentClass.getDeclaredField(fieldName);
                } catch (NoSuchFieldException e) {
                    currentClass = currentClass.getSuperclass();
                }
            }
            return null;
        });
    }

    private Method findGetter(Class<?> clazz, String fieldName) {
        Map<String, Method> getters = getterCache.computeIfAbsent(clazz, k -> new HashMap<>());
        
        return getters.computeIfAbsent(fieldName, k -> {
            String capitalizedFieldName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            String getterName = "get" + capitalizedFieldName;
            String isGetterName = "is" + capitalizedFieldName;
            
            Class<?> currentClass = clazz;
            while (currentClass != null) {
                try {
                    return currentClass.getMethod(getterName);
                } catch (NoSuchMethodException e) {
                    try {
                        return currentClass.getMethod(isGetterName);
                    } catch (NoSuchMethodException e2) {
                        currentClass = currentClass.getSuperclass();
                    }
                }
            }
            return null;
        });
    }

    private Method findSetter(Class<?> clazz, String fieldName, Class<?> paramType) {
        Map<String, Method> setters = setterCache.computeIfAbsent(clazz, k -> new HashMap<>());
        
        String key = fieldName + "_" + paramType.getName();
        return setters.computeIfAbsent(key, k -> {
            String capitalizedFieldName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            String setterName = "set" + capitalizedFieldName;
            
            Class<?> currentClass = clazz;
            while (currentClass != null) {
                try {
                    return currentClass.getMethod(setterName, paramType);
                } catch (NoSuchMethodException e) {
                    // Try with superclasses of the parameter type
                    for (Method method : currentClass.getMethods()) {
                        if (method.getName().equals(setterName) && method.getParameterCount() == 1 && 
                            method.getParameterTypes()[0].isAssignableFrom(paramType)) {
                            return method;
                        }
                    }
                    currentClass = currentClass.getSuperclass();
                }
            }
            return null;
        });
    }
}