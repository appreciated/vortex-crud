package com.github.appreciated.vortex_crud.core.config.model;

public class CollectionConfig<FieldType> {
    private final FieldType titleField;

    public CollectionConfig(FieldType titleField) {
        this.titleField = titleField;
    }

    public FieldType titleField() {
        return titleField;
    }
}
