package com.github.appreciated.flow_cms.service;

import com.github.appreciated.flow_cms.config.model.ApplicationConfig;
import com.github.appreciated.flow_cms.config.model.RouteConfig;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigBeanFactory;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigObject;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FlowCmsConfigService {

    private final ApplicationConfig configuration;

    public FlowCmsConfigService() {
        Config config = ConfigFactory.parseResources(FlowCmsConfigService.class.getClassLoader(), "flow-cms-config.conf");
        this.configuration = ConfigBeanFactory.create(config.getObject("application").toConfig(), ApplicationConfig.class);
        if (config.isEmpty()) {
            throw new IllegalStateException("No flow cms config found");
        }
    }

    public ApplicationConfig getConfiguration() {
        return configuration;
    }

    public RouteConfig getConfigForRoute(String viewName) {
        return configuration.getRoutesConfig().get(viewName);
    }

    public String getApplicationName() {
        return configuration.getName();
    }
}
