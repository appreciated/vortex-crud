package com.github.appreciated.vortex_crud.core.entity.data_store;

/**
 * Strategy interface for resolving foreign key values in entities.
 * This interface is used to extract the logic for resolving foreign key values
 * into a separate strategy pattern.
 *
 * @param <FieldType> The type of the field identifier
 */
public interface VortexCrudForeignKeyResolutionStrategy<FieldType> {

    /**
     * Resolves a foreign key value and applies it to the entity.
     *
     * @param entity            The entity to which the foreign key value should be applied
     * @param foreignKeyField   The field identifier of the foreign key
     * @param foreignKeyValue   The value of the foreign key
     * @param fieldNameResolver The resolver to get the field name from the field identifier
     */
    void resolveForeignKey(Object entity,
                           FieldType foreignKeyField,
                           Object foreignKeyValue,
                           VortexCrudDataStore<FieldType, ?> dataStore,
                           VortexCrudDataStoreFieldNameResolver<FieldType> fieldNameResolver);
}