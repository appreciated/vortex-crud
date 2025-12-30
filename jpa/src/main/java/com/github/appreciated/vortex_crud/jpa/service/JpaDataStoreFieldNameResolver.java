package com.github.appreciated.vortex_crud.jpa.service;

import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudQueryDataStoreFieldNameResolver;
import org.springframework.stereotype.Service;

@Service
public class JpaDataStoreFieldNameResolver implements VortexCrudQueryDataStoreFieldNameResolver<String> {

    @Override
    public String getKeyForFieldType(String tableField) {
        return tableField;
    }
}
