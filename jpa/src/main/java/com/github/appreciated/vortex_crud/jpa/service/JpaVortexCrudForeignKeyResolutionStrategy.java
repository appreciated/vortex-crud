package com.github.appreciated.vortex_crud.jpa.service;

import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFieldNameResolver;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudForeignKeyResolutionStrategy;
import com.github.appreciated.vortex_crud.jpa.service.reflection.JpaReflectionService;
import org.springframework.stereotype.Service;

/**
 * Default implementation of the ForeignKeyResolutionStrategy interface.
 * This implementation sets the foreign key value in the entity using reflection.
 */
@Service
public class JpaVortexCrudForeignKeyResolutionStrategy implements VortexCrudForeignKeyResolutionStrategy<String> {

    private final JpaReflectionService reflectionService;

    public JpaVortexCrudForeignKeyResolutionStrategy(JpaReflectionService reflectionService) {
        this.reflectionService = reflectionService;
    }

    @Override
    public void resolveForeignKey(Object entity, String foreignKeyField, Object foreignKeyValue, VortexCrudDataStore<String, ?> dataStore, VortexCrudDataStoreFieldNameResolver<String> fieldNameResolver) {
        if (foreignKeyField != null && foreignKeyValue != null) {
            reflectionService.setValue(entity, foreignKeyField, foreignKeyValue);
        }
    }
}