package com.github.appreciated.vortex_crud.core.service;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service for loading and providing access to the Turbo CRUD configuration.
 * Reads configuration from a resource file and offers methods to retrieve route and application settings.
 */

@Service
public class VortexCrudConfigService<DataStoreId, FieldId> {

    private final Application<DataStoreId, FieldId> configuration;

    public VortexCrudConfigService(@Autowired VortexCrudConfigurationProvider<DataStoreId, FieldId> configurationProvider) {
        configuration = configurationProvider.get();
    }

    public Application<DataStoreId, FieldId> getConfiguration() {
        return configuration;
    }

    public String getApplicationName() {
        return configuration.getName();
    }
}
