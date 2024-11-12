package com.github.appreciated.turbo_crud.config.model;

import com.typesafe.config.Optional;

public class CollectionConfiguration {

    private Route child;
    private String emptyMessage;
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
}
