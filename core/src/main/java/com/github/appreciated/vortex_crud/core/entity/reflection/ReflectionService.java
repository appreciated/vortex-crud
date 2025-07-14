package com.github.appreciated.vortex_crud.core.entity.reflection;

import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFieldNameResolver;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Service that provides property access to entity properties using Spring's BeanWrapper.
 */
@Service
public class ReflectionService<FieldId> {

    private final VortexCrudDataStoreFieldNameResolver<FieldId> fieldNameResolver;

    public ReflectionService(VortexCrudDataStoreFieldNameResolver<FieldId> fieldNameResolver) {
        this.fieldNameResolver = fieldNameResolver;
    }

    public <T> String getString(T entity, FieldId fieldName) {
        Object value = getValue(entity, fieldName);
        return value != null ? value.toString() : null;
    }

    public <T> Object getValue(T entity, FieldId field) {
        return getValueInternal(entity, fieldNameResolver.getKeyForFieldId(field));
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

    private <T> Object getValueByGetter(T entity, String fieldName) {
        try {
            String getterName;
            if (fieldName.startsWith("is")) {
                getterName = fieldName;
            } else {
                getterName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            }
            Method getter = entity.getClass().getMethod(getterName);
            return getter.invoke(entity);
        } catch (NoSuchMethodException e) {
            // Try boolean getter
            try {
                String booleanGetter = "is" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
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

    public <T> void setValue(T entity, FieldId fieldName, Object value) {
        if (entity == null) {
            return;
        }
        String propertyName = fieldNameResolver.getKeyForFieldId(fieldName);

        // First try setter method
        if (setValueBySetter(entity, propertyName, value)) {
            return;
        }

        // Then try direct field access
        setValueByField(entity, propertyName, value);
    }

    private <T> boolean setValueBySetter(T entity, String fieldName, Object value) {
        try {
            String setterName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            Method[] methods = entity.getClass().getMethods();
            for (Method method : methods) {
                if (method.getName().equals(setterName) && method.getParameterCount() == 1) {
                    method.invoke(entity, value);
                    return true;
                }
            }
        } catch (Exception e) {
            // Ignore and try field access
        }
        return false;
    }

    private <T> void setValueByField(T entity, String fieldName, Object value) {
        try {
            Field field = entity.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(entity, value);
        } catch (Exception e) {
            // Ignore if we can't set the value
        }
    }

    public <T> String getId(T entity) {
        if (entity == null) {
            return null;
        }
        Object id = getValueInternal(entity, "id");
        return id != null ? id.toString() : null;
    }
}
