package com.github.appreciated.turbo_crud.config.model;

import com.typesafe.config.Optional;

import java.util.List;

public class FormField {

    @Optional
    private String column;
    @Optional
    private String type;
    @Optional
    private boolean readOnly;
    @Optional
    private List<String> readOnlyForRoles;
    @Optional
    private String label;
    @Optional
    private String factory;
    @Optional
    private CollectionFactoryConfig collectionFactory;

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
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

    public String getFactory() {
        return factory;
    }

    public void setFactory(String factory) {
        this.factory = factory;
    }

    public CollectionFactoryConfig getCollectionFactory() {
        return collectionFactory;
    }

    public void setCollectionFactory(CollectionFactoryConfig collectionFactory) {
        this.collectionFactory = collectionFactory;
    }
}
