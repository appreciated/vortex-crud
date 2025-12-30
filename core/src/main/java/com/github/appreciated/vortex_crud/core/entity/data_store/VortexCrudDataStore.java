package com.github.appreciated.vortex_crud.core.entity.data_store;

/**
 * Interface for simple data store operations.
 * Provides basic CRUD operations without complex query requirements.
 * Perfect for custom implementations like file systems, in-memory stores, or external APIs.
 *
 * @param <FieldType>  The type used to identify fields in the data store (kept for compatibility)
 * @param <ModelClass> The type of entity this data store manages
 */
public interface VortexCrudDataStore<FieldType, ModelClass> extends VortexCrudBaseDataStore<ModelClass> {
    // Basic methods are inherited from VortexCrudBaseDataStore.
    // Complex query methods have been moved to VortexCrudQueryDataStore.
}
