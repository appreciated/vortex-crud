package com.github.appreciated.turbo_crud.core.service;

import com.github.appreciated.turbo_crud.core.config.model.Application;
import com.github.appreciated.turbo_crud.core.config.model.Route;
import org.springframework.stereotype.Service;

/**
 * Service for loading and providing access to the Turbo CRUD configuration.
 * Reads configuration from a resource file and offers methods to retrieve route and application settings.
 */

@Service
public class TurboCrudConfigService {

    private final Application configuration;

    public TurboCrudConfigService(TurboCrudConfigurationProvider configurationProvider) {
        configuration = configurationProvider.get();
    }

    public Application getConfiguration() {
        return configuration;
    }

    public Route getConfigForRoute(String viewName) {
        return configuration.getRoutes().get(viewName.split("/")[0]);
    }

    public String getApplicationName() {
        return configuration.getName();
    }
}
