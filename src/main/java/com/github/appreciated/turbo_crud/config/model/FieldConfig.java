package com.github.appreciated.turbo_crud.config.model;

import com.typesafe.config.Optional;

import java.util.List;

public class FieldConfig {

    private String factory;
    @Optional
    private boolean primary;
    @Optional
    private boolean required;
    @Optional
    private ValidationConfig validation;
    @Optional
    private String defaultValue;
    @Optional
    private String values;
    @Optional
    private String table;
    @Optional
    private String column;
    @Optional
    private String filterColumn;
    @Optional
    private List<String> children;

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

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getValues() {
        return values;
    }

    public void setValues(String values) {
        this.values = values;
    }

    public ValidationConfig getValidation() {
        return validation;
    }

    public void setValidation(ValidationConfig validation) {
        this.validation = validation;
    }

    public String getFactory() {
        return factory;
    }

    public void setFactory(String factory) {
        this.factory = factory;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public List<String> getChildren() {
        return children;
    }

    public void setChildren(List<String> children) {
        this.children = children;
    }

    public String getFilterColumn() {
        return filterColumn;
    }

    public void setFilterColumn(String filterColumn) {
        this.filterColumn = filterColumn;
    }
}
