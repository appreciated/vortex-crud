package com.github.appreciated.flow_cms.config.model;

import com.typesafe.config.ConfigObject;

import java.util.Map;

import static com.github.appreciated.flow_cms.config.model.ConfigModelUtil.toStringMapWithValueType;

public class TableConfig {

    private ConfigObject columns;

    public ConfigObject getColumns() {
        return columns;
    }

    public void setColumns(ConfigObject columns) {
        this.columns = columns;
    }

    public Map<String, FieldConfig> getFieldsConfig() {
        return toStringMapWithValueType(columns, FieldConfig.class);
    }
}

