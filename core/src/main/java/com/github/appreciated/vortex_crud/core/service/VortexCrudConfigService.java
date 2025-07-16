package com.github.appreciated.vortex_crud.core.service;

import com.github.appreciated.vortex_crud.core.config.model.Application;

/**
 * Service for loading and providing access to the vortex-crud configuration.
 * Reads configuration from a resource file and offers methods to retrieve route and application settings.
 */

public interface VortexCrudConfigService<DataStoreId, FieldId, KeyType> {

    Application<DataStoreId, FieldId, KeyType> getConfiguration();

    String getApplicationName();

}
