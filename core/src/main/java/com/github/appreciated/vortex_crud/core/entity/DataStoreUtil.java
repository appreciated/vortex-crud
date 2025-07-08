package com.github.appreciated.vortex_crud.core.entity;

import java.lang.reflect.Field;
import java.util.Objects;

public class DataStoreUtil {
    public static String getId(Object record) {
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

    public static boolean isNew(Object entity) {
        return getId(entity) == null;
    }

    public static boolean equals(Object item, String comparing) {
        return Objects.equals(getId(item), comparing);
    }
}