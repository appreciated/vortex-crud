package com.github.appreciated.vortex_crud.security.userstore.local.test;

import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFieldNameResolver;
import org.springframework.stereotype.Service;

@Service
public class StringFieldNameResolver implements VortexCrudDataStoreFieldNameResolver<String> {
    @Override
    public String getKeyForFieldType(String fieldName) {
        return fieldName;
    }
}
