package com.github.appreciated.vortex_crud.security.userstore.local.test;

import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudQueryDataStoreFieldNameResolver;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class StringFieldNameResolver implements VortexCrudQueryDataStoreFieldNameResolver<String> {
    @Override
    public String getKeyForFieldType(String fieldName) {
        return fieldName;
    }
}
