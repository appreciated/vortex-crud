package com.github.appreciated.turbo_crud.core.config.model;

public class CollectionConfig {
    private final String titleField;

    public CollectionConfig(String titleField) {
        this.titleField = titleField;
    }

    public String getTitleField() {
        return titleField;
    }
}
