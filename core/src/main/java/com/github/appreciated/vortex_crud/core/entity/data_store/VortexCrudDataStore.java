package com.github.appreciated.vortex_crud.core.entity.data_store;

/**
 * Base interface for data store operations.
 * Now contains only basic CRUD methods (insert, update, delete, getRecordById, count).
 *
 * @param <FieldType>  The type used to identify fields in the data store (e.g., TableField, String)
 * @param <ModelClass> The type of entity this data store manages
 */
public interface VortexCrudDataStore<FieldType, ModelClass> extends VortexCrudBaseDataStore<ModelClass> {
}
