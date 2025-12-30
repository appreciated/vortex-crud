package com.github.appreciated.vortex_crud.core.entity.data_store;

/**
 * Extended interface for data store operations.
 * Extends VortexCrudBaseDataStore.
 * This interface represents the basic contract for a data store in Vortex CRUD.
 *
 * @param <FieldType>  The type used to identify fields in the data store (e.g., TableField, String)
 * @param <ModelClass> The type of entity this data store manages
 */
public interface VortexCrudDataStore<FieldType, ModelClass> extends VortexCrudBaseDataStore<ModelClass> {
}
