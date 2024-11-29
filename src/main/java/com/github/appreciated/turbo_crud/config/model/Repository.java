package com.github.appreciated.turbo_crud.config.model;

import java.util.Map;

public class Repository {
    private String factory;
    private Map<String, Field> fields;

    public String getFactory() {
        return factory;
    }

    public void setFactory(String factory) {
        this.factory = factory;
    }

    public Map<String, Field> getFields() {
        return fields;
    }

    public void setFields(Map<String, Field> fields) {
        this.fields = fields;
    }
}


