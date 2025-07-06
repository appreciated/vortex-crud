package com.github.appreciated.vortex_crud.core.entity.data_store;

/**
 * Strategy interface for resolving foreign key values in entities.
 * This interface is used to extract the logic for resolving foreign key values
 * into a separate strategy pattern.
 *
 * @param <FieldId> The type of the field identifier
 * @param <ModelClass> The type of the model class
 */
public interface VortexCrudForeignKeyResolutionStrategy<FieldId, ModelClass> {

    /**
     * Resolves a foreign key value and applies it to the entity.
     *
     * @param entity The entity to which the foreign key value should be applied
     * @param foreignKeyField The field identifier of the foreign key
     * @param foreignKeyValue The value of the foreign key
     * @param dataStore The data store to use for resolving the foreign key
     * @param fieldNameResolver The resolver to get the field name from the field identifier
     */
    void resolveForeignKey(ModelClass entity, 
                          FieldId foreignKeyField, 
                          String foreignKeyValue,
                          VortexCrudDataStore<FieldId, ModelClass> dataStore,
                          VortexCrudDataStoreFieldNameResolver<FieldId> fieldNameResolver);
}
