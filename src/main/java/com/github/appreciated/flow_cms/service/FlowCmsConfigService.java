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

    public ConfigObject getViews() {
        return config.getObject("application.routes");
    }

    public ConfigObject getForRoute(String viewName) {
        return (ConfigObject) getViews().get(viewName);
    }

    public ConfigObject getComponentForRoute(String viewName) {
        ConfigObject viewConfig = getForRoute(viewName);
        return (ConfigObject) viewConfig.get("component");
    }

    public ConfigList getViewForRoute(String viewName) {
        ConfigObject viewConfig = getForRoute(viewName);
        return (ConfigList)((ConfigObject) viewConfig.get("access_control")).get("roles");
    }

    // Weitere Methoden für Beziehungen, Versionierung, Auditing...
}
