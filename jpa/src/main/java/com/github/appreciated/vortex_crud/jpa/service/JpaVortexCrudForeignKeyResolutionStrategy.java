package com.github.appreciated.vortex_crud.jpa.service;

import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFieldNameResolver;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudForeignKeyResolutionStrategy;
import com.github.appreciated.vortex_crud.core.model.GenericEntity;
import com.github.appreciated.vortex_crud.jpa.service.datastore.JpaDataStoreFactoryRegistry;
import com.github.appreciated.vortex_crud.jpa.service.datastore.JpaRepositoryDataStore;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;

/**
 * Default implementation of the ForeignKeyResolutionStrategy interface.
 * This implementation simply sets the foreign key value in the entity using the field name resolver.
 *
 */
@Service
public class JpaVortexCrudForeignKeyResolutionStrategy implements VortexCrudForeignKeyResolutionStrategy<String> {


    private final JpaDataStoreFactoryRegistry dataStoreRegistryFactory;

    public JpaVortexCrudForeignKeyResolutionStrategy(JpaDataStoreFactoryRegistry dataStoreRegistryFactory) {
        this.dataStoreRegistryFactory = dataStoreRegistryFactory;
    }

    @Override
    public void resolveForeignKey(GenericEntity entity,
                                  String foreignKeyField,
                                  String foreignKeyValue,
                                  VortexCrudDataStore<String> dataStore,
                                  VortexCrudDataStoreFieldNameResolver<String> fieldNameResolver) {
        if (foreignKeyField != null && foreignKeyValue != null) {
            Field field = dataStore.getField(foreignKeyField);
            JpaRepositoryDataStore<String> fieldDataStore = dataStoreRegistryFactory.getDataStore(field.getType());
            GenericEntity recordById = fieldDataStore.getRecordById(foreignKeyValue);
            entity.put(fieldNameResolver.getKeyForFieldId(foreignKeyField), recordById);
        }
    }
}