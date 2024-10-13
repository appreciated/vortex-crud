package com.github.appreciated.flow_cms.service;

import com.github.appreciated.flow_cms.config.model.ApplicationConfig;
import com.github.appreciated.flow_cms.config.model.RouteConfig;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigBeanFactory;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigParseOptions;
import org.springframework.stereotype.Service;

/**
 * Service for loading and providing access to the CMS configuration.
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
            throw new IllegalStateException("No flow cms config found");
        }
    }

    public ApplicationConfig getConfiguration() {
        return configuration;
    }

    public RouteConfig getConfigForRoute(String viewName) {
        return configuration.getRoutesConfig().get(viewName.split("/")[0]);
    }

    public String getApplicationName() {
        return configuration.getName();
    }
}
