package com.github.appreciated.flow_cms.config.model;

import com.typesafe.config.Optional;

public class FieldConfig {

    private String type;
    @Optional
    private boolean primary;
    @Optional
    private boolean required;
    @Optional
    private int max_length;
    @Optional
    private String default_value;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isPrimary() {
        return primary;
    }

    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public int getMax_length() {
        return max_length;
    }

    public void setMax_length(int max_length) {
        this.max_length = max_length;
    }

    public String getDefault_value() {
        return default_value;
    }

    public void setDefault_value(String default_value) {
        this.default_value = default_value;
    }
}
