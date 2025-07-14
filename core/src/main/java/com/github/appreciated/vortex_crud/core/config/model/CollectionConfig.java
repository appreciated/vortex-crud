package com.github.appreciated.vortex_crud.core.config.model;

public class CollectionConfig<FieldId> {
    private final FieldId titleField;

    public CollectionConfig(FieldId titleField) {
        this.titleField = titleField;
    }

    public FieldId getTitleField() {
        return titleField;
    }
}
