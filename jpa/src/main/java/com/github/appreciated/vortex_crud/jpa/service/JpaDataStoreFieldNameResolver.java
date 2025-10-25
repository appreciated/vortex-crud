package com.github.appreciated.vortex_crud.jpa.service;

import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFieldNameResolver;
import org.springframework.stereotype.Service;

@Service
public class JpaDataStoreFieldNameResolver implements VortexCrudDataStoreFieldNameResolver<String> {

    @Override
    public String getKeyForFieldType(String tableField) {
        return tableField;
    }
}
