package com.github.appreciated.vortex_crud.core.entity.reflection;

import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFieldNameResolver;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Service that provides reflection-based access to entity properties.
 * This replaces the direct get/put/getString methods with reflection-based access.
 */
@Service
public class ReflectionService<FieldId> {

    private final VortexCrudDataStoreFieldNameResolver<FieldId> fieldNameResolver;

    public ReflectionService(VortexCrudDataStoreFieldNameResolver<FieldId> fieldNameResolver) {
        this.fieldNameResolver = fieldNameResolver;
    }

    // Cache for fields and methods to improve performance
    private final Map<Class<?>, Map<String, Field>> fieldCache = new HashMap<>();
    private final Map<Class<?>, Map<String, Method>> getterCache = new HashMap<>();
    private final Map<Class<?>, Map<String, Method>> setterCache = new HashMap<>();

    /**
     * Gets a property value as a String from an entity using reflection.
     *
     * @param entity    The entity object
     * @param fieldName The name of the field to get
     * @param <T>       The type of the entity
     * @return The value of the field as a String
     */
    public <T> String getString(T entity, FieldId fieldName) {
        return getStringInternal(entity, fieldNameResolver.getKeyForFieldId(fieldName));
    }

    /**
     * Gets a property value as a String from an entity using reflection.
     *
     * @param entity    The entity object
     * @param fieldName The name of the field to get
     * @param <T>       The type of the entity
     * @return The value of the field as a String
     */
    private <T> String getStringInternal(T entity, String fieldName) {
        Object value = getValueInternal(entity, fieldName);
        return value == null ? null : value.toString();
    }

    public <T> Object getValue(T entity, FieldId field) {
        return getValueInternal(entity, fieldNameResolver.getKeyForFieldId(field));
    }

    /**
     * Gets a property value from an entity using reflection.
     *
     * @param entity    The entity object
     * @param fieldName The name of the field to get
     * @param <T>       The type of the entity
     * @return The value of the field
     */
    public <T> Object getValueInternal(T entity, String fieldName) {
        if (entity == null) {
            return null;
        }

        // Try direct field access
        try {
            Field field = entity.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(entity);
        } catch (NoSuchFieldException e) {
            // If field not found in the class, try superclass
            try {
                Field field = entity.getClass().getField(fieldName);
                field.setAccessible(true);
                return field.get(entity);
            } catch (NoSuchFieldException | IllegalAccessException ex) {
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error getting field " + fieldName + " from " + entity.getClass().getName(), e);
        }
    }

    public <T> void setValueInternal(T entity, FieldId fieldName, Object value) {
        setValueInternal(entity, fieldNameResolver.getKeyForFieldId(fieldName), value);
    }

    /**
     * Sets a property value on an entity using reflection.
     *
     * @param entity    The entity object
     * @param fieldName The name of the field to set
     * @param value     The value to set
     * @param <T>       The type of the entity
     */
    private <T> void setValueInternal(T entity, String fieldName, Object value) {
        if (entity == null) {
            return;
        }

        // Try direct field access
        try {
            Field field = entity.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(entity, value);
        } catch (NoSuchFieldException e) {
            // If field not found in the class, try superclass
            try {
                Field field = entity.getClass().getField(fieldName);
                field.setAccessible(true);
                field.set(entity, value);
            } catch (NoSuchFieldException | IllegalAccessException ex) {
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error setting field " + fieldName + " on " + entity.getClass().getName(), e);
        }
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

        Object id = getValueInternal(entity, "id");
        return id == null ? null : id.toString();
    }

}
