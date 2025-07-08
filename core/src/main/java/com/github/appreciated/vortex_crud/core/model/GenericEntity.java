package com.github.appreciated.vortex_crud.core.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A generic entity class representing a map of properties.
 * Provides methods for accessing, updating, and retrieving properties within the entity.
 */

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

    public String getString(String name) {
        Object obj = properties.get(name);
        if (obj instanceof Integer){
            return String.valueOf(obj);
        }
        return (String) obj;
    }

    public void put(String name, Object value) {
        this.properties.put(name, value);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof GenericEntity that)) return false;
        return Objects.equals(getProperties(), that.getProperties());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getProperties());
    }
}
