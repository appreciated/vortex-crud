package com.github.appreciated.vortex_crud.security.userstore.local.test;

import com.github.appreciated.vortex_crud.core.entity.VortexCrudQueryDataStoreUtilStrategy;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Objects;

@Component
public class StringDataStoreUtilStrategy implements VortexCrudQueryDataStoreUtilStrategy {
    public String getId(Object record) {
        if (record == null) {
            return null;
        }
        try {
            Field field = record.getClass().getDeclaredField("id");
            field.setAccessible(true);
            Object id = field.get(record);
            return id == null ? null : id.toString();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return null;
        }
    }

    public boolean isNew(Object entity) {
        return getId(entity) == null;
    }

    public boolean equals(Object item, String comparing) {
        return Objects.equals(getId(item), comparing);
    }
}
