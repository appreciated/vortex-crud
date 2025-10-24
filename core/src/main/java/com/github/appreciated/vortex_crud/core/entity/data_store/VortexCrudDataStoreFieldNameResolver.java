package com.github.appreciated.vortex_crud.core.entity.data_store;

public interface VortexCrudDataStoreFieldNameResolver<FieldType> {

    String getKeyForFieldType(FieldType fieldId);
}
