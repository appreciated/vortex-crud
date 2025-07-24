package com.github.appreciated.vortex_crud.jooq.service;

import com.github.appreciated.vortex_crud.core.entity.VortexCrudDataStoreUtilStrategy;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Objects;

@Component
public class JooqVortexCrudDataStoreUtilStrategy implements VortexCrudDataStoreUtilStrategy {
    @Override
    public String getId(Object record) {
        if (record == null) {
            return null;
        }

        // First try to use getId() method if available
        try {
            java.lang.reflect.Method getIdMethod = record.getClass().getMethod("getId");
            Object id = getIdMethod.invoke(record);
            return id == null ? null : id.toString();
        } catch (Exception e) {
            // If getId() method is not available or fails, fall back to field access
            try {
                Field field = record.getClass().getDeclaredField("id");
                field.setAccessible(true);
                Object id = field.get(record);
                return id == null ? null : id.toString();
            } catch (NoSuchFieldException | IllegalAccessException ex) {
                return null;
            }
        }
    }

    @Override
    public boolean isNew(Object entity) {
        return getId(entity) == null;
    }

    @Override
    public boolean equals(Object item, String comparing) {
        return Objects.equals(getId(item), comparing);
    }
}