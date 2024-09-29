package com.github.appreciated.flow_cms.service;

import java.util.HashMap;
import java.util.Map;

public class GenericEntity {
    private final Map<String, Object> properties;

    public GenericEntity(Map<String, Object> properties) {
        this.properties = properties;
    }

    public GenericEntity() {
        this.properties = new HashMap<>();
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public Object get(String name) {
        return properties.get(name);
    }

    public void put(String name, Object value) {
        this.properties.put(name, value);
    }

    public String getSecondProperty() {
        return null;
    }

    public String getFirstProperty() {
        return null;
    }
}
