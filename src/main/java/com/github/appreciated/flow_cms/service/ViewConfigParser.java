package com.github.appreciated.flow_cms.service;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import java.util.List;

public class ViewConfigParser {

    private Config config;

    public ViewConfigParser(String hoconConfigFile) {
        this.config = ConfigFactory.parseResources(hoconConfigFile);
    }

    public List<? extends Config> getViews() {
        return config.getConfigList("application.views");
    }

    public Config getView(String viewName) {
        return config.getConfigList("application.views").stream()
                .filter(view -> view.getString("name").equals(viewName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("View not found: " + viewName));
    }

    public List<? extends Config> getFieldsForView(String viewName) {
        Config viewConfig = getView(viewName);
        return viewConfig.getConfigList("layout");
    }

    public List<String> getRolesForView(String viewName) {
        Config viewConfig = getView(viewName);
        return viewConfig.getConfigList("access_control").stream()
                .map(role -> role.getString("roles"))
                .toList();
    }

    // Weitere Methoden für Beziehungen, Versionierung, Auditing...
}
