package com.github.appreciated.turbo_crud.config.model;

import java.util.List;

public class VersioningConfig {

    private boolean enabled;
    private List<String> repositories;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<String> getRepositories() {
        return repositories;
    }

    public void setRepositories(List<String> repositories) {
        this.repositories = repositories;
    }

}
