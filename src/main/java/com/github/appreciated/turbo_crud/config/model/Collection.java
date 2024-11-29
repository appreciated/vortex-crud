package com.github.appreciated.turbo_crud.config.model;

public class Collection {
    private String label;
    private String factory;
    private CollectionData data;
    private String emptyMessage;
    private Route child;

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

    public CollectionData getData() {
        return data;
    }

    public void setData(CollectionData data) {
        this.data = data;
    }

    public String getEmptyMessage() {
        return emptyMessage;
    }

    public void setEmptyMessage(String emptyMessage) {
        this.emptyMessage = emptyMessage;
    }

    public Route getChild() {
        return child;
    }

    public void setChild(Route child) {
        this.child = child;
    }
}