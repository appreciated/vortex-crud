package com.github.appreciated.turbo_crud.config.model;

import java.util.List;

public class FormElement {
    private String field;
    private String factory;
    private boolean readOnly;
    private List<String> readOnlyForRoles;
    private String label;
    private String type;
    private Integer span = null;
    Collection configuration;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getFactory() {
        return factory;
    }

    public void setFactory(String factory) {
        this.factory = factory;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public List<String> getReadOnlyForRoles() {
        return readOnlyForRoles;
    }

    public void setReadOnlyForRoles(List<String> readOnlyForRoles) {
        this.readOnlyForRoles = readOnlyForRoles;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getSpan() {
        return span;
    }

    public void setSpan(Integer span) {
        this.span = span;
    }

    public Collection getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Collection configuration) {
        this.configuration = configuration;
    }
}
