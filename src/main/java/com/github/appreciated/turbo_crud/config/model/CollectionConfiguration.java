package com.github.appreciated.turbo_crud.config.model;

import com.typesafe.config.Optional;

public class CollectionConfiguration {

    @Optional
    private Route child;
    private String emptyMessage;
    private String factory;
    private CollectionData data;

    public Route getChild() {
        return child;
    }

    public void setChild(Route child) {
        this.child = child;
    }

    public String getEmptyMessage() {
        return emptyMessage;
    }

    public void setEmptyMessage(String emptyMessage) {
        this.emptyMessage = emptyMessage;
    }

    public CollectionData getData() {
        return data;
    }

    public void setData(CollectionData data) {
        this.data = data;
    }

    public String getFactory() {
        return factory;
    }

    public void setFactory(String factory) {
        this.factory = factory;
    }
}
