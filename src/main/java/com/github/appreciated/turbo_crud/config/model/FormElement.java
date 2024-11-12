package com.github.appreciated.turbo_crud.config.model;

import com.typesafe.config.Optional;

import java.util.List;

public class FormElement {

    @Optional
    private String field;
    @Optional
    private String factory;
    @Optional
    private boolean readOnly;
    @Optional
    private List<String> readOnlyForRoles;
    @Optional
    private String label;
    private String type;
    @Optional
    private Integer span = null;
    @Optional
    CollectionConfiguration configuration;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
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

    public String getFactory() {
        return factory;
    }

    public void setFactory(String factory) {
        this.factory = factory;
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

    public CollectionConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(CollectionConfiguration configuration) {
        this.configuration = configuration;
    }
}
