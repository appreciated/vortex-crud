package com.github.appreciated.vortex_crud.core.entity;

import java.util.Objects;

public class DataStoreUtil {
    public static <ModelClass> String getId(ModelClass record) {
        return record.get("id") == null ? null : ("" + record.get("id")); //TODO Cleanup, Column name and type needs to be declared in config
    }

    public static <ModelClass> boolean isNew(ModelClass entity) {
        return entity.get("id") == null; //TODO Cleanup, Column name needs to be declared in config
    }

    public static <ModelClass> boolean equals(ModelClass item, String comparing) {
        return Objects.equals(DataStoreUtil.getId(item), comparing);
    }
}
