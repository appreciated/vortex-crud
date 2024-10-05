package com.github.appreciated.flow_cms.config.model;

import java.util.List;

public class VersioningConfig {
    private boolean enabled;
    private List<String> tables;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<String> getTables() {
        return tables;
    }

    public void setTables(List<String> tables) {
        this.tables = tables;
    }

// Getters and Setters
}
