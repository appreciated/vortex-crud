package com.github.appreciated.turbo_crud.service;

import com.github.appreciated.turbo_crud.config.model.ApplicationConfig;
import com.github.appreciated.turbo_crud.config.model.Route;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigBeanFactory;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigParseOptions;
import org.springframework.stereotype.Service;

/**
 * Service for loading and providing access to the Turbo CRUD configuration.
 * Reads configuration from a resource file and offers methods to retrieve route and application settings.
 */

@Service
public class TurboCrudConfigService {

    private final ApplicationConfig configuration;

    public TurboCrudConfigService() {
        ConfigParseOptions defaults = ConfigParseOptions.defaults();
        Config config = ConfigFactory.parseResources(TurboCrudConfigService.class.getClassLoader(), "turbo-crud-config.conf", defaults);
        this.configuration = ConfigBeanFactory.create(config.getObject("application").toConfig(), ApplicationConfig.class);
        if (config.isEmpty()) {
            throw new IllegalStateException("No TurboCRUD config found");
        }
    }

    public ApplicationConfig getConfiguration() {
        return configuration;
    }

    public Route getConfigForRoute(String viewName) {
        return configuration.getRoutesConfig().get(viewName.split("/")[0]);
    }

    public String getApplicationName() {
        return configuration.getName();
    }
}
