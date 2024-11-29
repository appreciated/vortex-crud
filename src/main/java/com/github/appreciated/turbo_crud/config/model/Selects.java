package com.github.appreciated.turbo_crud.config.model;

import java.util.Map;

public class Selects {
    private Map<String, Map<String, String>> configs;

    public Map<String, Map<String, String>> getConfigs() {
        return configs;
    }

    public void setConfigs(Map<String, Map<String, String>> configs) {
        this.configs = configs;
    }
}