package com.github.appreciated.flow_cms.service;

import com.typesafe.config.*;
import org.springframework.stereotype.Service;

@Service
public class FlowCmsConfigService {

    private Config config;

    public FlowCmsConfigService() {
        this.config = ConfigFactory.parseResources(FlowCmsConfigService.class.getClassLoader(),"flow-cms-config.conf");
        if (this.config.isEmpty()){
            throw new IllegalStateException("No flow cms config found");
        }
    }

    public ConfigObject getRoutes() {
        return config.getObject("application.routes");
    }

    public ConfigObject getConfigForRoute(String viewName) {
        return (ConfigObject) getRoutes().get(viewName);
    }

    public String getApplicationName() {
        return config.getString("application.name");
    }
}
