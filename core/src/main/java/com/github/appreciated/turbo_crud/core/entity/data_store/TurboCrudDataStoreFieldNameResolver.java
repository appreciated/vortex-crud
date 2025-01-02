package com.github.appreciated.turbo_crud.core.entity.data_store;

public interface TurboCrudDataStoreFieldNameResolver<FieldId> {

    String getKeyForFieldId(FieldId fieldId);
}
