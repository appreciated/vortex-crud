package com.github.appreciated.vortex_crud.security.userstore.local.test;

import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFieldNameResolver;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudForeignKeyResolutionStrategy;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
public class StringForeignKeyResolutionStrategy implements VortexCrudForeignKeyResolutionStrategy<String> {

    @Override
    public void resolveForeignKey(Object entity, String foreignKeyField, Object foreignKeyValue, VortexCrudDataStore<String, ?> dataStore, VortexCrudDataStoreFieldNameResolver<String> fieldNameResolver) {
        // Empty implementation for test purposes
    }
}
