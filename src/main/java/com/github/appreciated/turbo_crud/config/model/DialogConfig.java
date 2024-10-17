package com.github.appreciated.turbo_crud.config.model;

public class DialogConfig {

    private String factory;
    private String emptyMessage;
    private Route child;

    public String getFactory() {
        return factory;
    }

    public void setFactory(String factory) {
        this.factory = factory;
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
