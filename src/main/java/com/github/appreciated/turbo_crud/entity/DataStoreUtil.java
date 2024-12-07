package com.github.appreciated.turbo_crud.entity;

import com.github.appreciated.turbo_crud.model.GenericEntity;

import java.util.Objects;

public class DataStoreUtil {
    public static String getId(GenericEntity record) {
        return record.get("id") == null ? null : ("" + record.get("id")); //TODO Cleanup, Column name and type needs to be declared in config
    }

    public static boolean isNew(GenericEntity entity) {
        return entity.get("id") == null; //TODO Cleanup, Column name needs to be declared in config
    }

    public static boolean equals(GenericEntity item, String comparing) {
        return Objects.equals(DataStoreUtil.getId(item), comparing);
    }
}
