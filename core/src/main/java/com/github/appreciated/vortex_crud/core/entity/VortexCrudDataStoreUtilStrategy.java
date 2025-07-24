package com.github.appreciated.vortex_crud.core.entity;

public interface VortexCrudDataStoreUtilStrategy {
    String getId(Object record);

    boolean isNew(Object entity);

    boolean equals(Object item, String comparing);
}
