package com.github.appreciated.turbo_crud.config.model;

import com.typesafe.config.ConfigObject;

import java.util.Map;

import static com.github.appreciated.turbo_crud.config.model.ConfigModelUtil.toStringMapWithValueType;

public class RepositoryConfig {

    private ConfigObject fields;

    public ConfigObject getFields() {
        return fields;
    }

    public void setFields(ConfigObject fields) {
        this.fields = fields;
    }

    public Map<String, FieldConfig> getFieldsConfig() {
        return toStringMapWithValueType(fields, FieldConfig.class);
    }
}

