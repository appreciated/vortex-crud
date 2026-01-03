package com.github.appreciated.vortex_crud.jpa.service;

import com.github.appreciated.vortex_crud.core.entity.VortexCrudDataStoreUtilStrategy;
import jakarta.persistence.Id;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Objects;

@Component
public class JpaVortexCrudDataStoreUtilStrategy implements VortexCrudDataStoreUtilStrategy {
    public String getId(Object record) {
        if (record == null) {
            return null;
        }
        try {
            Field field = findIdField(record.getClass());
            if (field == null) {
                throw new IllegalStateException("No @Id field found in class " + record.getClass().getName());
            }
            field.setAccessible(true);
            Object id = field.get(record);
            return id == null ? null : id.toString();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Find the ID field in the given entity class by looking for the @Id annotation.
     * Walks up the class hierarchy to support inheritance.
     *
     * @param entityClass The entity class to search in.
     * @return The ID field, or null if not found.
     */
    private Field findIdField(Class<?> entityClass) {
        Class<?> currentClass = entityClass;
        while (currentClass != null && currentClass != Object.class) {
            for (Field field : currentClass.getDeclaredFields()) {
                if (field.isAnnotationPresent(Id.class)) {
                    return field;
                }
            }
            currentClass = currentClass.getSuperclass();
        }
        return null;
    }

    public boolean isNew(Object entity) {
        return getId(entity) == null;
    }

    public boolean equals(Object item, String comparing) {
        return Objects.equals(getId(item), comparing);
    }
}