package com.github.appreciated.vortex_crud.core.entity.data_store;

/**
 * Base interface for data store operations.
 * Extends VortexCrudBaseDataStore without advanced query capabilities.
 *
 * @param <FieldType>  The type used to identify fields in the data store (e.g., TableField, String)
 * @param <ModelClass> The type of entity this data store manages
 */
public interface VortexCrudDataStore<FieldType, ModelClass> extends VortexCrudBaseDataStore<ModelClass> {
}
