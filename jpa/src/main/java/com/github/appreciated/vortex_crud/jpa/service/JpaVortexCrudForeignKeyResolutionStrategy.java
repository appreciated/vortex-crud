package com.github.appreciated.vortex_crud.jpa.service;

import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudQueryDataStore;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudQueryDataStoreFieldNameResolver;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudForeignKeyResolutionStrategy;
import org.springframework.stereotype.Service;

/**
 * Default implementation of the ForeignKeyResolutionStrategy interface.
 * This implementation simply sets the foreign key value in the entity using the field name resolver.
 */
@Service
public class JpaVortexCrudForeignKeyResolutionStrategy implements VortexCrudForeignKeyResolutionStrategy<String> {

    public JpaVortexCrudForeignKeyResolutionStrategy() {
    }

    @Override
    public void resolveForeignKey(Object entity, String foreignKeyField, Object foreignKeyValue, VortexCrudQueryDataStore<String, ?> dataStore, VortexCrudQueryDataStoreFieldNameResolver<String> fieldNameResolver) {
    }
}