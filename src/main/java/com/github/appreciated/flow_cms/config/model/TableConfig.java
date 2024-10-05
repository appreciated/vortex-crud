package com.github.appreciated.flow_cms.config.model;

import java.util.Map;

public class TableConfig {
    private Map<String, FieldConfig> fields;

    public Map<String, FieldConfig> getFields() {
        return fields;
    }

    public void setFields(Map<String, FieldConfig> fields) {
        this.fields = fields;
    }
}

