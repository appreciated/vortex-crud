package com.github.appreciated.vortex_crud.jpa.service;

import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFieldNameResolver;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudForeignKeyResolutionStrategy;
import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import com.github.appreciated.vortex_crud.jpa.service.datastore.JpaDataStoreFactoryRegistry;
import org.springframework.stereotype.Service;

/**
 * Default implementation of the ForeignKeyResolutionStrategy interface.
 * This implementation simply sets the foreign key value in the entity using the field name resolver.
 */
@Service
public class JpaVortexCrudForeignKeyResolutionStrategy implements VortexCrudForeignKeyResolutionStrategy<String> {

    private final JpaDataStoreFactoryRegistry dataStoreRegistryFactory;
    private final ReflectionService<String> reflectionService;

    public JpaVortexCrudForeignKeyResolutionStrategy(JpaDataStoreFactoryRegistry dataStoreRegistryFactory, ReflectionService<String> reflectionService) {
        this.dataStoreRegistryFactory = dataStoreRegistryFactory;
        this.reflectionService = reflectionService;
    }

    @Override
    public void resolveForeignKey(Object entity, String foreignKeyField, Object foreignKeyValue, VortexCrudDataStore<String, ?> dataStore, VortexCrudDataStoreFieldNameResolver<String> fieldNameResolver) {
    }
}