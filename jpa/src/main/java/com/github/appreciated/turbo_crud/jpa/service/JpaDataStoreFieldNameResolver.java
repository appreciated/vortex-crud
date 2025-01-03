package com.github.appreciated.turbo_crud.jpa.service;

import com.github.appreciated.turbo_crud.core.entity.data_store.TurboCrudDataStoreFieldNameResolver;
import org.springframework.stereotype.Service;

@Service
public class JpaDataStoreFieldNameResolver implements TurboCrudDataStoreFieldNameResolver<String> {
    
    @Override
    public String getKeyForFieldId(String tableField) {
        return tableField;
    }
}
