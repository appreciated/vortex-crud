package com.github.appreciated.flow_cms.config.model;

import java.util.List;

public class AuditingConfig {
    private boolean enabled;
    private List<String> actions;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<String> getActions() {
        return actions;
    }

    public void setActions(List<String> actions) {
        this.actions = actions;
    }
}
