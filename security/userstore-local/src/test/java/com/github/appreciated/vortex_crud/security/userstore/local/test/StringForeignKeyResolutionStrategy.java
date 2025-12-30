package com.github.appreciated.vortex_crud.security.userstore.local.test;

import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudQueryDataStore;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudQueryDataStoreFieldNameResolver;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudForeignKeyResolutionStrategy;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
public class StringForeignKeyResolutionStrategy implements VortexCrudForeignKeyResolutionStrategy<String> {

    @Override
    public void resolveForeignKey(Object entity, String foreignKeyField, Object foreignKeyValue, VortexCrudQueryDataStore<String, ?> dataStore, VortexCrudQueryDataStoreFieldNameResolver<String> fieldNameResolver) {
        // Empty implementation for test purposes
    }
}
